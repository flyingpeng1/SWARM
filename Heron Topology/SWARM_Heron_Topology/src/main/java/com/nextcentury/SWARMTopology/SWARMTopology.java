package com.nextcentury.SWARMTopology;

import com.twitter.heron.api.topology.TopologyBuilder;
import com.twitter.heron.common.basics.ByteAmount;
import com.twitter.heron.api.metric.GlobalMetrics;
import com.twitter.heron.api.HeronSubmitter;
import com.nextcentury.SWARMTopology.Bolts.DigestRouterBolt;
import com.nextcentury.SWARMTopology.Bolts.SimGPSAdapterBolt;
import com.nextcentury.SWARMTopology.Bolts.SimHeartRateAdapterBolt;
import com.nextcentury.SWARMTopology.Spouts.*;
import com.twitter.heron.api.Config;
import com.twitter.heron.api.topology.BoltDeclarer;

public class SWARMTopology {
    
    public static final int STREAM_MANAGERS = 5;
    public static final int BOLT_INSTANCES = 2;    
    public static final int SPOUT_INSTANCES = 1;
    
    public static void main(String args[]) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        
        builder.setSpout(SensorDataSpout.SENSOR_DATA_SPOUT, new SensorDataSpout(), SPOUT_INSTANCES);
        
        builder.setBolt(DigestRouterBolt.DIGEST_ROUTER_NODE, new DigestRouterBolt(), BOLT_INSTANCES)
        		.shuffleGrouping(SensorDataSpout.SENSOR_DATA_SPOUT);
        
        builder.setBolt(SimGPSAdapterBolt.SIMGPS_ADAPTER_NODE, new SimGPSAdapterBolt(), BOLT_INSTANCES)
        		.shuffleGrouping(DigestRouterBolt.DIGEST_ROUTER_NODE, DigestRouterBolt.SIM_GPS_STREAM);
        builder.setBolt(SimHeartRateAdapterBolt.SIM_HEART_RATE_ADAPTER_NODE, new SimHeartRateAdapterBolt(), BOLT_INSTANCES)
				.shuffleGrouping(DigestRouterBolt.DIGEST_ROUTER_NODE, DigestRouterBolt.SIM_HEART_RATE_STREAM);
    
    }
}
