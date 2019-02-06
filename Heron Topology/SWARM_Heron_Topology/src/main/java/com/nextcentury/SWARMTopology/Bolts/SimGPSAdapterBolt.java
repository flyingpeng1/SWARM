package com.nextcentury.SWARMTopology.Bolts;

import java.util.Map;

import com.nextcentury.SWARMTopology.SWARMTupleSchema;
import com.twitter.heron.api.bolt.BaseRichBolt;
import com.twitter.heron.api.bolt.OutputCollector;
import com.twitter.heron.api.topology.OutputFieldsDeclarer;
import com.twitter.heron.api.topology.TopologyContext;
import com.twitter.heron.api.tuple.Tuple;
import com.twitter.heron.api.tuple.Values;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

@SuppressWarnings("restriction")
public class SimGPSAdapterBolt extends BaseRichBolt{

	private static final long serialVersionUID = 1L;

	public static final String SIMGPS_ADAPTER_NODE = "SimGPSAdapterNode";
	public static final String OUTPUT_STREAM_ID = SIMGPS_ADAPTER_NODE + ".Stream";

	OutputCollector oc;
	HexBinaryAdapter adapter;

	//------------------------------------------------------------------------------------
	//Decipher Hex string to byte[] to GPS parameters. Add parameters to normalized tuple.
	//------------------------------------------------------------------------------------
	public void execute(Tuple t) {
		String rawData = t.getStringByField(SWARMTupleSchema.SCHEMA_RAW_DATA);

		byte[] bytes = adapter.unmarshal(rawData);

		double latitude = 0;
		double longitude = 0;
		double altitude = 0;
		double bearing = 0;
		double speed = 0;

		try {
			//latitude
			String latString = "";
			if (!(unsignify(bytes[0]) > 0)) {
				latString = "-";
			}
			latString += Integer.toString(unsignify(bytes[1]));
			latString += ".";
			latString += Integer.toString(unsignify(bytes[2]));
			latString += Integer.toString(unsignify(bytes[3]));
			latString += Integer.toString(unsignify(bytes[4]));
			latitude = Double.parseDouble(latString);
			//longitude
			String longString = "";
			if (!(bytes[5] > 0)) {
				longString = "-";
			}
			longString += Integer.toString(unsignify(bytes[6]));
			longString += ".";
			longString += Integer.toString(unsignify(bytes[7]));
			longString += Integer.toString(unsignify(bytes[8]));
			longString += Integer.toString(unsignify(bytes[9]));
			longitude = Double.parseDouble(longString);
			//altitude
			altitude += unsignify(bytes[13]);
			altitude += unsignify(bytes[12]) * 256;
			altitude += unsignify(bytes[11]) * 65536;
			altitude += unsignify(bytes[10]) * 16777216;
			//bearing
			bearing += unsignify(bytes[15]);
			bearing += unsignify(bytes[14]) * 256;
			//speed
			speed += unsignify(bytes[19]);
			speed += unsignify(bytes[18]) * 256;
			speed += unsignify(bytes[17]) * 65536;
			speed += unsignify(bytes[16]) * 16777216;
		} catch (ArrayIndexOutOfBoundsException e) {
			//Handle messed up tuple
			oc.fail(t);
		} catch (Exception e) {
			//Handle unsupported tuple
			oc.fail(t);
		}

		//---------------------
		//Send along the data
		//---------------------
		try {
		oc.emit(OUTPUT_STREAM_ID, new Values(
				t.getStringByField(SWARMTupleSchema.SCHEMA_UID),
				t.getDoubleByField(SWARMTupleSchema.SCHEMA_TIMESTAMP),
				latitude,
				longitude,
				altitude,
				bearing,
				speed,
				null
				));
		oc.ack(t);
		} catch (Exception e) {
			oc.fail(t);
		}
	}

	//---------------------
	//Initializes the node.
	//---------------------
	public void prepare(Map<String, Object> heronConf, TopologyContext context, OutputCollector collector) {
		oc = collector;
		adapter = new HexBinaryAdapter();
	}

	//-------------------------------------
	//Sets node to output normalized tuple.
	//-------------------------------------
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(OUTPUT_STREAM_ID, SWARMTupleSchema.getNormalizedSchema());
	}

	//----------------------------------------
	//Prevents java from forcing signed bytes 
	//----------------------------------------
	private static int unsignify(byte b) {
		int num = b;
		if (num < 0) {
			num = num + 256;
		}
		return num;
	}

}
