//Jack Wilcom
//Next Century Corporation
//SWARM data spout.
//-------------------------

package com.nextcentury.SWARMTopology.Spouts;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.nextcentury.SWARMTopology.SWARMTupleSchema;
import com.nextcentury.SWARMTopology.Util.KafkaConfig;
import com.nextcentury.SWARMTopology.Util.RawDataObj;
import com.nextcentury.SWARMTopology.Util.SpringScanner;
import com.nextcentury.SWARMTopology.Util.TopologyConfig;
import com.twitter.heron.api.spout.BaseRichSpout;
import com.twitter.heron.api.spout.SpoutOutputCollector;
import com.twitter.heron.api.topology.OutputFieldsDeclarer;
import com.twitter.heron.api.topology.TopologyContext;
import com.twitter.heron.api.tuple.Values;
import com.twitter.heron.api.utils.Utils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

@Service
@Scope("prototype")
@Lazy
public class SensorDataSpout extends BaseRichSpout {


	public static final String SENSOR_DATA_SPOUT = "SensorDataSpout";
	private static final long serialVersionUID = 1L;

	@Resource
	KafkaConfig config;

	@Resource
	TopologyConfig TConf;

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

			if (TConf.getUseIngestTime()) {
				collector.emit(new Values(
						Rdo.getUID(),
						(double)(new Long(System.currentTimeMillis()).doubleValue()/1000d),
						Rdo.getSensorType(),
						Rdo.getPayload()),
						messageUUID);
			} else {
				collector.emit(new Values(
						Rdo.getUID(),
						Rdo.getTimestamp(),
						Rdo.getSensorType(),
						Rdo.getPayload()),
						messageUUID);
			}
		}
	}

	//---------------------------------------
	//Called by Heron to initialize the node.
	//---------------------------------------
	public void open(Map<String, Object> map, TopologyContext tc, SpoutOutputCollector soc) {
		SpringScanner.initializeSpring();
		config = SpringScanner.getBean(KafkaConfig.class);
		TConf = SpringScanner.getBean(TopologyConfig.class);
		collector = soc;
		gson = new Gson();

		final Properties kafkaProps = new Properties();
		kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getSpoutKafkaServers());
		kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, config.getSpoutKafkaGroupName());
		kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		consumer = new KafkaConsumer<Long, String>(kafkaProps);
		consumer.subscribe(Collections.singletonList(config.getSpoutKafkaTopicName()));
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
