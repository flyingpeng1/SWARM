package com.nextcentury.SWARMTopology;

import com.twitter.heron.api.topology.TopologyBuilder;
import com.twitter.heron.common.basics.ByteAmount;
import com.twitter.heron.api.HeronSubmitter;

import com.nextcentury.SWARMTopology.Bolts.*;
import com.nextcentury.SWARMTopology.Spouts.*;
import com.nextcentury.SWARMTopology.Util.KafkaConfig;
import com.nextcentury.SWARMTopology.Util.SpringScanner;
import com.nextcentury.SWARMTopology.Util.TopologyConfig;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.nextcentury.SWARMTopology.KryoSerializer;
import com.twitter.heron.api.Config;
import com.twitter.heron.api.topology.BoltDeclarer;

@Service
@Scope("prototype")
@Lazy
public class SWARMTopology {
	
    @Resource
	static TopologyConfig springConfig;

	public static void main(String args[]) throws Exception {
		SpringScanner.initializeSpring();
		springConfig = SpringScanner.getBean(TopologyConfig.class);
		TopologyBuilder builder = new TopologyBuilder();

		//gather JSON from Pub/Sub
		builder.setSpout(SensorDataSpout.SENSOR_DATA_SPOUT, new SensorDataSpout(), springConfig.getSensorDataSpoutInstanceNum());

		//Send sensor data to corresponding adapter
		builder.setBolt(DigestRouterBolt.DIGEST_ROUTER_NODE, new DigestRouterBolt(), springConfig.getDigestRouterBoltInstanceNum())
		.shuffleGrouping(SensorDataSpout.SENSOR_DATA_SPOUT);

		//adapter nodes; different nodes handle different sensors
		builder.setBolt(SimGPSAdapterBolt.SIMGPS_ADAPTER_NODE, new SimGPSAdapterBolt(), springConfig.getSimGPSAdapterBoltInstanceNum())
		.shuffleGrouping(DigestRouterBolt.DIGEST_ROUTER_NODE, DigestRouterBolt.SIM_GPS_STREAM);
		builder.setBolt(SimHeartRateAdapterBolt.SIM_HEART_RATE_ADAPTER_NODE, new SimHeartRateAdapterBolt(), springConfig.getSimHeartRateAdapterBoltInstanceNum())
		.shuffleGrouping(DigestRouterBolt.DIGEST_ROUTER_NODE, DigestRouterBolt.SIM_HEART_RATE_STREAM);

		//collects and distributes normalized tuples
		BoltDeclarer NormalizedRouter = builder.setBolt(NormalizedRouterBolt.NORMALIZED_ROUTER_NODE, new NormalizedRouterBolt(), springConfig.getNormalizedRouterBoltInstanceNum());
		NormalizedRouter.shuffleGrouping(SimGPSAdapterBolt.SIMGPS_ADAPTER_NODE, SimGPSAdapterBolt.OUTPUT_STREAM_ID);
		NormalizedRouter.shuffleGrouping(SimHeartRateAdapterBolt.SIM_HEART_RATE_ADAPTER_NODE, SimHeartRateAdapterBolt.OUTPUT_STREAM_ID);

		//nodes for using normalized data
		builder.setBolt(NormalizedAnalyticsPlaceholderBolt.NORMALIZED_ANALYTICS_PLACEHOLDER_NODE, new NormalizedAnalyticsPlaceholderBolt(), springConfig.getNormalizedAnalyticsPlaceholderBoltInstanceNum())
		.shuffleGrouping(NormalizedRouterBolt.NORMALIZED_ROUTER_NODE, NormalizedRouterBolt.ANALYTICS_DATA_STREAM);
		builder.setBolt(NormalizedDatabaseBolt.NORMALIZED_DATABASE_NODE, new NormalizedDatabaseBolt(), springConfig.getNormalizedDatabaseBoltInstanceNum())
		.shuffleGrouping(NormalizedRouterBolt.NORMALIZED_ROUTER_NODE, NormalizedRouterBolt.NORMALIZED_DATABASE_STREAM);
		builder.setBolt(NormalizedPubSubBolt.NORMALIZED_PUBSUB_NODE, new NormalizedPubSubBolt(), springConfig.getNormalizedPubSubBoltInstanceNum())
		.shuffleGrouping(NormalizedRouterBolt.NORMALIZED_ROUTER_NODE, NormalizedRouterBolt.NORMALIZED_KAFKA_STREAM);
		
		//optional metrics node
		if (springConfig.getEnableMetricsNode()) {
		builder.setBolt(MetricsBolt.METRICS_NODE, new MetricsBolt(), springConfig.getMetricsBoltInstanceNum())
		.shuffleGrouping(NormalizedPubSubBolt.NORMALIZED_PUBSUB_NODE);
		}
		
		//------------------------------------------------
		//Configuration. Borrowed from HeronTest Topology.
		//------------------------------------------------
		Config conf = new Config();
		conf.setDebug(true);

		try {
			//custom faster serialization
			conf.setSerializationClassName(KryoSerializer.class.getName());
		} catch (NoClassDefFoundError ncdfe) {
			//fall back on using JAVA serialization
		}

		String jvmOptions = "-XX:-CMSScavengeBeforeRemark " +       //Turn off extra logging, start
				"-XX:-PrintHeapAtGC  " +                //Turn off extra logging, end
				"-XX:-UseConcMarkSweepGC " +            //Turn off CMS
				"-XX:+UseG1GC";                         //Java 8 - use G1 GC  

		conf.setComponentJvmOptions(SensorDataSpout.SENSOR_DATA_SPOUT, jvmOptions);  
		conf.setComponentJvmOptions(DigestRouterBolt.DIGEST_ROUTER_NODE, jvmOptions);  
		conf.setComponentJvmOptions(SimGPSAdapterBolt.SIMGPS_ADAPTER_NODE, jvmOptions);  
		conf.setComponentJvmOptions(SimHeartRateAdapterBolt.SIM_HEART_RATE_ADAPTER_NODE, jvmOptions);  
		conf.setComponentJvmOptions(NormalizedRouterBolt.NORMALIZED_ROUTER_NODE, jvmOptions);  
		conf.setComponentJvmOptions(NormalizedAnalyticsPlaceholderBolt.NORMALIZED_ANALYTICS_PLACEHOLDER_NODE, jvmOptions);
		conf.setComponentJvmOptions(NormalizedDatabaseBolt.NORMALIZED_DATABASE_NODE, jvmOptions);
		conf.setComponentJvmOptions(NormalizedPubSubBolt.NORMALIZED_PUBSUB_NODE, jvmOptions);

		conf.setMaxSpoutPending(1000 * 1000 * 1000);//large number to prevent a max
		conf.setTopologyReliabilityMode(Config.TopologyReliabilityMode.ATMOST_ONCE); //Config.TopologyReliabilityMode.ATLEAST_ONCE
		conf.put(Config.TOPOLOGY_WORKER_CHILDOPTS, "-XX:+HeapDumpOnOutOfMemoryError");
		conf.setNumStmgrs(springConfig.getStreamManagerNum()); //number of stream managers        

		conf.setComponentRam(SensorDataSpout.SENSOR_DATA_SPOUT, ByteAmount.fromMegabytes(springConfig.getSensorDataSpoutRAM()));  
		conf.setComponentRam(DigestRouterBolt.DIGEST_ROUTER_NODE, ByteAmount.fromMegabytes(springConfig.getDigestRouterBoltRAM())); 
		conf.setComponentRam(SimGPSAdapterBolt.SIMGPS_ADAPTER_NODE, ByteAmount.fromMegabytes(springConfig.getSimGPSAdapterBoltRAM()));
		conf.setComponentRam(SimHeartRateAdapterBolt.SIM_HEART_RATE_ADAPTER_NODE, ByteAmount.fromMegabytes(springConfig.getSimHeartRateAdapterBoltRAM()));
		conf.setComponentRam(NormalizedRouterBolt.NORMALIZED_ROUTER_NODE, ByteAmount.fromMegabytes(springConfig.getNormalizedRouterBoltRAM())); 
		conf.setComponentRam(NormalizedAnalyticsPlaceholderBolt.NORMALIZED_ANALYTICS_PLACEHOLDER_NODE,  ByteAmount.fromMegabytes(springConfig.getNormalizedAnalyticsPlaceholderBoltRAM()));
		conf.setComponentRam(NormalizedDatabaseBolt.NORMALIZED_DATABASE_NODE,  ByteAmount.fromMegabytes(springConfig.getNormalizedDatabaseBoltRAM()));
		conf.setComponentRam(NormalizedPubSubBolt.NORMALIZED_PUBSUB_NODE,  ByteAmount.fromMegabytes(springConfig.getNormalizedPubSubBoltRAM()));
		
		if (springConfig.getEnableMetricsNode()) {
			conf.setComponentJvmOptions(MetricsBolt.METRICS_NODE, jvmOptions);
			conf.setComponentRam(MetricsBolt.METRICS_NODE,  ByteAmount.fromMegabytes(springConfig.getMetricsBoltRAM()));
		}
		
		conf.setContainerDiskRequested(ByteAmount.fromGigabytes(springConfig.getContainerDiskSpaceNum())); 
		conf.setContainerCpuRequested(springConfig.getContainerRequestedCPU());

		HeronSubmitter.submitTopology(args[0], conf, builder.createTopology());
	}
}
