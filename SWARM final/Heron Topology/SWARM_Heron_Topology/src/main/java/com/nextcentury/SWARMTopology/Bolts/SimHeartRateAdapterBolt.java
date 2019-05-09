package com.nextcentury.SWARMTopology.Bolts;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.nextcentury.SWARMTopology.SWARMTupleSchema;
import com.twitter.heron.api.bolt.BaseRichBolt;
import com.twitter.heron.api.bolt.OutputCollector;
import com.twitter.heron.api.topology.OutputFieldsDeclarer;
import com.twitter.heron.api.topology.TopologyContext;
import com.twitter.heron.api.tuple.Tuple;
import com.twitter.heron.api.tuple.Values;



public class SimHeartRateAdapterBolt extends BaseRichBolt{

	private static final long serialVersionUID = 1L;	
	public static final String SIM_HEART_RATE_ADAPTER_NODE = "SimHeartRateAdapterNode";
	public static final String OUTPUT_STREAM_ID = SIM_HEART_RATE_ADAPTER_NODE + ".Stream";

	OutputCollector oc;
	Gson gson;

	//-------------------------------------------------------------------------
	//Decipher json to BPM parameter. Add parameters to normalized tuple.
	//-------------------------------------------------------------------------
	public void execute(Tuple t) {

		try {
			String json = t.getStringByField(SWARMTupleSchema.SCHEMA_RAW_DATA);
			Map<String, Object> jsonValues = gson.fromJson(json, Map.class);


			//---------------------
			//Send along the data
			//---------------------
			oc.emit(OUTPUT_STREAM_ID, new Values(
					t.getStringByField(SWARMTupleSchema.SCHEMA_UID),
					t.getDoubleByField(SWARMTupleSchema.SCHEMA_TIMESTAMP),
					null, null, null, null, null,
					jsonValues.get("BPM")
					));
			oc.ack(t);
		} catch (Exception e) {
			//Handle unsupported tuple
			Logger.getLogger(SimHeartRateAdapterBolt.class.getName()).log(Level.SEVERE,
					"Error:"+ e.toString());

			oc.fail(t);
		}
	}

	//---------------------
	//Initializes the node.
	//---------------------
	public void prepare(Map<String, Object> heronConf, TopologyContext context, OutputCollector collector) {
		oc = collector;
		gson = new Gson();
	}

	//-------------------------------------
	//Sets node to output normalized tuple.
	//-------------------------------------
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(OUTPUT_STREAM_ID, SWARMTupleSchema.getNormalizedSchema());
	}

}
