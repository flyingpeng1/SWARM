package com.nextcentury.SWARMTopology.Bolts;

import java.util.Map;

import com.nextcentury.SWARMTopology.SWARMTupleSchema;
import com.twitter.heron.api.bolt.BaseRichBolt;
import com.twitter.heron.api.bolt.OutputCollector;
import com.twitter.heron.api.topology.OutputFieldsDeclarer;
import com.twitter.heron.api.topology.TopologyContext;
import com.twitter.heron.api.tuple.Tuple;

public class DigestRouterBolt extends BaseRichBolt {

	public final static String DIGEST_ROUTER_NODE = "DigestRouterNode";

	public static final String SIM_GPS_STREAM = DIGEST_ROUTER_NODE + ".SimGPSStream";
	public static final String SIM_HEART_RATE_STREAM = DIGEST_ROUTER_NODE + ".SimHeartRateStream";

	OutputCollector oc;
	public void execute(Tuple t) {
		String sensorType = t.getStringByField(SWARMTupleSchema.SCHEMA_SENSOR_TYPE);
		String streamName = "";

		if (sensorType.equals("PullHeartRateMonitor")) {
			streamName = SIM_HEART_RATE_STREAM;
		} else if (sensorType.equals("PullGPS") || sensorType.equals("PushGPS")) {
			streamName = SIM_GPS_STREAM;
		} else {
			//Handle unsupported tuple!!!
			oc.fail(t);
			return;
		}

		oc.emit(streamName, t, t.getValues());
		oc.ack(t);
	}

	public void prepare(Map<String, Object> heronConf, TopologyContext context, OutputCollector collector) {
		oc = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(SIM_GPS_STREAM, SWARMTupleSchema.getRawSchema());
		declarer.declareStream(SIM_HEART_RATE_STREAM, SWARMTupleSchema.getRawSchema());
	}

}
