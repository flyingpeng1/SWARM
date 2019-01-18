package com.nextcentury.SWARMTopology.Bolts;

import java.util.Map;

import com.nextcentury.SWARMTopology.SWARMTupleSchema;
import com.twitter.heron.api.bolt.BaseRichBolt;
import com.twitter.heron.api.bolt.OutputCollector;
import com.twitter.heron.api.topology.OutputFieldsDeclarer;
import com.twitter.heron.api.topology.TopologyContext;
import com.twitter.heron.api.tuple.Tuple;



public class SimHeartRateAdapterBolt extends BaseRichBolt{

	private static final long serialVersionUID = 1L;	
    public static final String SIM_HEART_RATE_ADAPTER_NODE = "SimHeartRateAdapterNode";

    OutputCollector oc;
    
	public void execute(Tuple input) {
		//TODO!!!!!!!!!!!!!!!!!! LOOK HERE MORON!!!!!!!!!!!!!!!
	}

	//---------------------
	//Initializes the node.
	//---------------------
	public void prepare(Map<String, Object> heronConf, TopologyContext context, OutputCollector collector) {
		oc = collector;
	}

	//-------------------------------------
	//Sets node to output normalized tuple.
	//-------------------------------------
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(SWARMTupleSchema.getNormalizedSchema());
	}
	
}
