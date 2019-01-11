package com.nextcentury.SWARMTopology;

import com.twitter.heron.api.topology.TopologyBuilder;
import com.twitter.heron.common.basics.ByteAmount;
import com.twitter.heron.api.metric.GlobalMetrics;
import com.twitter.heron.api.HeronSubmitter;
import com.nextcentury.SWARMTopology.Spouts.*;
import com.twitter.heron.api.Config;
import com.twitter.heron.api.topology.BoltDeclarer;

public class SWARMTopology {
	
    
    public static final String KAFKA_SERVER_ADDR = "0.0.0.0"; 
    
    public static final int STREAM_MANAGERS = 5;
    public static final int BOLT_INSTANCES = 2;    
    public static final int SPOUT_INSTANCES = 1;
    
    public static void main(String args[]) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
    }
    
}
