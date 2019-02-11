package com.nextcentury.SWARMTopology.Bolts;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.nextcentury.SWARMTopology.SWARMTupleSchema;
import com.twitter.heron.api.bolt.BaseRichBolt;
import com.twitter.heron.api.bolt.OutputCollector;
import com.twitter.heron.api.topology.OutputFieldsDeclarer;
import com.twitter.heron.api.topology.TopologyContext;
import com.twitter.heron.api.tuple.Tuple;

public class MetricsBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	public final static String METRICS_NODE = "MetricsNode";

	double timeSum;
	double count;
	double startTime;
	double currentTime;
	double elapsedMinTotalDub;
	int elapsedMinTotal = 0;
	int latestLoggedMinute;


	@Override
	public void execute(Tuple input) {
		try {
			//start trial when heron finishes its first tuple
			if (count > 0 && startTime == 0) {
				Logger.getLogger(MetricsBolt.class.getName()).log(Level.INFO, "Starting metric tracking...");
				startTime = new Long(System.currentTimeMillis()).doubleValue()/1000d;
				Logger.getLogger(MetricsBolt.class.getName()).log(Level.INFO, "Start time: " + startTime);
			}
			
			currentTime = new Long(System.currentTimeMillis()).doubleValue()/1000d;
			timeSum += (currentTime - input.getDoubleByField(SWARMTupleSchema.SCHEMA_TIMESTAMP));
			count++;

			elapsedMinTotalDub = ((double)(currentTime - startTime) / 60d);
			elapsedMinTotal = (int)elapsedMinTotalDub;
			
			
			//Logger.getLogger(MetricsBolt.class.getName()).log(Level.INFO, "elapsedMinTotalDub" + elapsedMinTotalDub);
			//Logger.getLogger(MetricsBolt.class.getName()).log(Level.INFO, "elapsedMinTotal" + elapsedMinTotal);
			//Logger.getLogger(MetricsBolt.class.getName()).log(Level.INFO, "latestLoggedMinute" + latestLoggedMinute);
			//Logger.getLogger(MetricsBolt.class.getName()).log(Level.INFO, "count" + count);
			if (elapsedMinTotal > (int)latestLoggedMinute) {
				Logger.getLogger(MetricsBolt.class.getName()).log(Level.INFO, "\nElapsed simulation time (min): " + elapsedMinTotal + "\nAverage Latencey per tuple (ms):" + (timeSum / count) + "\nMost recent latencey (ms)" + (currentTime - input.getDoubleByField(SWARMTupleSchema.SCHEMA_TIMESTAMP)) + "\nTotal tuples processed (#):" + count);
				latestLoggedMinute++;
			}
		} catch (Exception e){
			Logger.getLogger(MetricsBolt.class.getName()).log(Level.SEVERE, e.getStackTrace().toString());
		}
	}

	@Override
	public void prepare(Map<String, Object> heronConf, TopologyContext context, OutputCollector collector) {
		timeSum = 0;
		count = 0;
		latestLoggedMinute = -1;
		startTime = 0;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//nothing to output!!!
	}

}
