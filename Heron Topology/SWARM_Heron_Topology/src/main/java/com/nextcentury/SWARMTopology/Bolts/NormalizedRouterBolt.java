package com.nextcentury.SWARMTopology.Bolts;

import java.util.Map;

import com.nextcentury.SWARMTopology.SWARMTupleSchema;
import com.twitter.heron.api.bolt.BaseRichBolt;
import com.twitter.heron.api.bolt.OutputCollector;
import com.twitter.heron.api.topology.OutputFieldsDeclarer;
import com.twitter.heron.api.topology.TopologyContext;
import com.twitter.heron.api.tuple.Tuple;

public class NormalizedRouterBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;
	public static final String NORMALIZED_ROUTER_NODE = "NormalizedRouterNode";

	public static final String NORMALIZED_DATABASE_STREAM = ".NormalizedDatabaseStream";
	public static final String NORMALIZED_KAFKA_STREAM = ".NormalizedDatabaseStream";
	public static final String ANALYTICS_DATA_STREAM = ".AnalyticsStream";

	OutputCollector oc;

	public void execute(Tuple t) {
		try {
			oc.emit(NORMALIZED_DATABASE_STREAM, t, t.getValues());
			oc.ack(t);
		} catch (Exception e) {
			//handle dropped tuple
			oc.fail(t);
		}
		
		try {
			oc.emit(NORMALIZED_KAFKA_STREAM, t, t.getValues());
			oc.ack(t);
		} catch (Exception e) {
			//handle dropped tuple
			oc.fail(t);
		}
		
		try {
			oc.emit(ANALYTICS_DATA_STREAM, t, t.getValues());
			oc.ack(t);
		} catch (Exception e) {
			//handle dropped tuple
			oc.fail(t);
		}

	}

	public void prepare(Map<String, Object> heronConf, TopologyContext context, OutputCollector collector) {
		oc = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(NORMALIZED_DATABASE_STREAM, SWARMTupleSchema.getNormalizedSchema());
		declarer.declareStream(NORMALIZED_KAFKA_STREAM, SWARMTupleSchema.getNormalizedSchema());
		declarer.declareStream(ANALYTICS_DATA_STREAM, SWARMTupleSchema.getNormalizedSchema());
	}

}
