package com.nextcentury.SWARMTopology.Bolts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.gson.Gson;
import com.twitter.heron.api.bolt.BaseRichBolt;
import com.twitter.heron.api.bolt.OutputCollector;
import com.twitter.heron.api.topology.OutputFieldsDeclarer;
import com.twitter.heron.api.topology.TopologyContext;
import com.twitter.heron.api.tuple.Fields;
import com.twitter.heron.api.tuple.Tuple;

public abstract class KafkaPublisher extends BaseRichBolt {
    
	private KafkaProducer<Long, String> producer;
	
	public void execute(Tuple input) {
		
		String payload = buildJsonFromTuple(input);
		ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(getKafkaTopic(), payload);
		
		//Sync or ASync?
		try {
			producer.send(record).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

	}

	public void prepare(Map<String, Object> heronConf, TopologyContext context, OutputCollector collector) {
		producer = new KafkaProducer<Long, String>(this.getKafkaProps());
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//Nothing to declare, final bolt in this stream.
	}
	
	private String buildJsonFromTuple(Tuple t) {
		Fields TupleFields = t.getFields();
		List<Object> TupleData = t.getValues();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		for (int itemIndex = 0; itemIndex < t.size(); itemIndex++) {
			map.put(TupleFields.get(itemIndex), TupleData.get(itemIndex));
		}
		
		return new Gson().toJson(new HashMap<String, Object>(map)); 
	}
	
	protected abstract Properties getKafkaProps();
	protected abstract String getKafkaTopic();

}
