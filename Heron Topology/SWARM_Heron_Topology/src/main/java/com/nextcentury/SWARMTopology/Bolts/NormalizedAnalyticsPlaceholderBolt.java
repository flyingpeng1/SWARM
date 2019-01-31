package com.nextcentury.SWARMTopology.Bolts;

import java.util.Map;

import com.twitter.heron.api.bolt.BaseRichBolt;
import com.twitter.heron.api.bolt.OutputCollector;
import com.twitter.heron.api.topology.OutputFieldsDeclarer;
import com.twitter.heron.api.topology.TopologyContext;
import com.twitter.heron.api.tuple.Tuple;

public class NormalizedAnalyticsPlaceholderBolt extends BaseRichBolt {

	public final static String NORMALIZED_ANALYTICS_PLACEHOLDER_NODE = "NormalizedAnalyticsPlaceholderNode";
	private OutputCollector c;
	
	public void execute(Tuple input) {
		//bye tuple!
		input = null;
	}

	public void prepare(Map<String, Object> heronConf, TopologyContext context, OutputCollector collector) {
		c = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//nothing to declare for now... The tuple dies here.
	}

}
