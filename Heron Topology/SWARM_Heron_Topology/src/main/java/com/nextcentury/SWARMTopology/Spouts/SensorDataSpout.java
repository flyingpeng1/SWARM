//Jack Wilcom
//Next Century Corporation
//SWARM data spout.
//-------------------------

package com.nextcentury.SWARMTopology.Spouts;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.nextcentury.SWARMTopology.SWARMTupleSchema;
import com.nextcentury.SWARMTopology.Util.ConfigManager;
import com.nextcentury.SWARMTopology.Util.RawDataObj;
import com.twitter.heron.api.spout.BaseRichSpout;
import com.twitter.heron.api.spout.SpoutOutputCollector;
import com.twitter.heron.api.topology.OutputFieldsDeclarer;
import com.twitter.heron.api.topology.TopologyContext;
import com.twitter.heron.api.tuple.Values;
import com.twitter.heron.api.utils.Utils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class SensorDataSpout extends BaseRichSpout {

	
	public static final String SENSOR_DATA_SPOUT = "SensorDataSpout";
	private static final long serialVersionUID = 1L;
	final Properties props = ConfigManager.getProperties();

	SpoutOutputCollector collector;
	Gson gson;
	Consumer<Long, String> consumer;
	Iterator<ConsumerRecord<Long, String>> recordIterator;

	//---------------------------------------
	//Called by Heron to get next data list.
	//---------------------------------------
	public void nextTuple() {
		
		String JsonString = newJson();
		
		if (JsonString!=null) {
			RawDataObj Rdo = JsonToMap(JsonString);

			String messageUUID =  "UID" + "." +  "SENSOR_TYPE" + "." + System.currentTimeMillis();

			collector.emit(new Values(
					Rdo.getUID(),
					Rdo.getTimestamp(),
					Rdo.getSensorType(),
					Rdo.getPayload()),
					messageUUID);
		}
	}

	//---------------------------------------
	//Called by Heron to initialize the node.
	//---------------------------------------
	public void open(Map<String, Object> map, TopologyContext tc, SpoutOutputCollector soc) {
		collector = soc;
		gson = new Gson();

		final Properties kafkaProps = new Properties();
		kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getProperty("KafkaServers", "127.0.0.1:9092"));
		kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, props.getProperty("GroupName", "Main"));
		kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		consumer = new KafkaConsumer<Long, String>(kafkaProps);
		consumer.subscribe(Collections.singletonList(props.getProperty("TopicName")));
		updateIterator();
	}

	//----------------------------------------------
	//Used by Heron to set schema for output tuple.
	//----------------------------------------------
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(SWARMTupleSchema.getRawSchema());
	}

	//--------------------------------
	//Parses JSON into a data object.
	//--------------------------------
	private RawDataObj JsonToMap(String JsonString) {
		RawDataObj o = gson.fromJson(JsonString, RawDataObj.class);
		return o;
	}

	//-------------------------------------------------------
	//Called to retrieve a new value from the Kafka topic.
	//Returns null if no new values are available.
	//-------------------------------------------------------
	private String newJson() {
		if (recordIterator.hasNext()) {
			return recordIterator.next().value();
		} else {
			updateIterator();
			consumer.commitAsync();
			return null;
		}
	}
	
	//---------------------------------------------------------
	//Refills the record list with new records from the topic.
	//---------------------------------------------------------
	private void updateIterator() {
		final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);
		recordIterator = consumerRecords.iterator();
		if (consumerRecords.count() == 0) {
			Utils.sleep(1000);
		}
	}
}
