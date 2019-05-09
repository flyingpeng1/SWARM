package com.nextcentury.SWARMTopology.Bolts;

import java.util.Map;

import com.twitter.heron.api.bolt.BaseRichBolt;
import com.twitter.heron.api.bolt.OutputCollector;
import com.twitter.heron.api.topology.OutputFieldsDeclarer;
import com.twitter.heron.api.topology.TopologyContext;
import com.twitter.heron.api.tuple.Tuple;

public class NormalizedDatabaseBolt extends BaseRichBolt {

	public final static String NORMALIZED_DATABASE_NODE = "NormalizedDatabaseNode";
	
	public void execute(Tuple input) {
		
	}

	public void prepare(Map<String, Object> heronConf, TopologyContext context, OutputCollector collector) {
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//Nothing to declare, final bolt in this stream.
	}

}
