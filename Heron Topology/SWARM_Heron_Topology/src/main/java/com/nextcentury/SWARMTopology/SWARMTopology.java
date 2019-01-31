package com.nextcentury.SWARMTopology;

import com.twitter.heron.api.topology.TopologyBuilder;
import com.twitter.heron.common.basics.ByteAmount;
import com.twitter.heron.api.HeronSubmitter;

import com.nextcentury.SWARMTopology.Bolts.*;
import com.nextcentury.SWARMTopology.Spouts.*;
import com.nextcentury.SWARMTopology.KryoSerializer;
import com.twitter.heron.api.Config;
import com.twitter.heron.api.topology.BoltDeclarer;

public class SWARMTopology {

	public static final int STREAM_MANAGERS = 5;
	public static final int BOLT_INSTANCES = 2;    
	public static final int SPOUT_INSTANCES = 1;

	public static void main(String args[]) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();

		//gather JSON from Pub/Sub
		builder.setSpout(SensorDataSpout.SENSOR_DATA_SPOUT, new SensorDataSpout(), SPOUT_INSTANCES);

		//Send sensor data to corresponding adapter
		builder.setBolt(DigestRouterBolt.DIGEST_ROUTER_NODE, new DigestRouterBolt(), BOLT_INSTANCES)
		.shuffleGrouping(SensorDataSpout.SENSOR_DATA_SPOUT);

		//adapter nodes; different nodes handle different sensors
		builder.setBolt(SimGPSAdapterBolt.SIMGPS_ADAPTER_NODE, new SimGPSAdapterBolt(), BOLT_INSTANCES)
		.shuffleGrouping(DigestRouterBolt.DIGEST_ROUTER_NODE, DigestRouterBolt.SIM_GPS_STREAM);
		builder.setBolt(SimHeartRateAdapterBolt.SIM_HEART_RATE_ADAPTER_NODE, new SimHeartRateAdapterBolt(), BOLT_INSTANCES)
		.shuffleGrouping(DigestRouterBolt.DIGEST_ROUTER_NODE, DigestRouterBolt.SIM_HEART_RATE_STREAM);

		//collects and distributes normalized tuples
		BoltDeclarer NormalizedRouter = builder.setBolt(NormalizedRouterBolt.NORMALIZED_ROUTER_NODE, new NormalizedRouterBolt(), BOLT_INSTANCES);
		NormalizedRouter.shuffleGrouping(SimGPSAdapterBolt.SIMGPS_ADAPTER_NODE, SimGPSAdapterBolt.OUTPUT_STREAM_ID);
		NormalizedRouter.shuffleGrouping(SimHeartRateAdapterBolt.SIM_HEART_RATE_ADAPTER_NODE, SimHeartRateAdapterBolt.OUTPUT_STREAM_ID);

		//nodes for using normalized data
		builder.setBolt(NormalizedAnalyticsPlaceholderBolt.NORMALIZED_ANALYTICS_PLACEHOLDER_NODE, new NormalizedAnalyticsPlaceholderBolt())
		.shuffleGrouping(NormalizedRouterBolt.NORMALIZED_ROUTER_NODE);
		builder.setBolt(NormalizedDatabaseBolt.NORMALIZED_DATABASE_NODE, new NormalizedDatabaseBolt())
		.shuffleGrouping(NormalizedRouterBolt.NORMALIZED_ROUTER_NODE);
		builder.setBolt(NormalizedPubSubBolt.NORMALIZED_PUBSUB_NODE, new NormalizedPubSubBolt())
		.shuffleGrouping(NormalizedRouterBolt.NORMALIZED_ROUTER_NODE);

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
		conf.setNumStmgrs(STREAM_MANAGERS); //number of stream managers        

		conf.setComponentRam(SensorDataSpout.SENSOR_DATA_SPOUT, ByteAmount.fromMegabytes(500));  
		conf.setComponentRam(DigestRouterBolt.DIGEST_ROUTER_NODE, ByteAmount.fromMegabytes(200)); 
		conf.setComponentRam(SimGPSAdapterBolt.SIMGPS_ADAPTER_NODE, ByteAmount.fromMegabytes(200));
		conf.setComponentRam(SimHeartRateAdapterBolt.SIM_HEART_RATE_ADAPTER_NODE, ByteAmount.fromMegabytes(200));
		conf.setComponentRam(NormalizedRouterBolt.NORMALIZED_ROUTER_NODE, ByteAmount.fromMegabytes(200)); 
		conf.setComponentRam(NormalizedAnalyticsPlaceholderBolt.NORMALIZED_ANALYTICS_PLACEHOLDER_NODE,  ByteAmount.fromMegabytes(200));
		conf.setComponentRam(NormalizedDatabaseBolt.NORMALIZED_DATABASE_NODE,  ByteAmount.fromMegabytes(200));
		conf.setComponentRam(NormalizedPubSubBolt.NORMALIZED_PUBSUB_NODE,  ByteAmount.fromMegabytes(200));

		conf.setContainerDiskRequested(ByteAmount.fromGigabytes(2)); 
		conf.setContainerCpuRequested(1);

		HeronSubmitter.submitTopology(args[0], conf, builder.createTopology());
	}
}
