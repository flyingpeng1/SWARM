package NextCentury;

import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.google.gson.Gson;
import com.nextcentury.SWARMTopology.Util.ConfigManager;
import com.nextcentury.SWARMTopology.Util.RawDataObj;
import com.twitter.heron.api.spout.SpoutOutputCollector;
import com.twitter.heron.api.utils.Utils;

public class KafkaTest {

	private static final long serialVersionUID = 1L;

	final Properties props = ConfigManager.getProperties();
	
	Gson gson;
	Consumer<Long, String> consumer;
	Iterator<ConsumerRecord<Long, String>> recordIterator;


	public static void main(String args[]) {
		KafkaTest t = new KafkaTest();
		t.run();
	}

	public void run() {
		gson = new Gson();

		final Properties kafkaProps = new Properties();
		kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getProperty("KafkaServers", "127.0.0.1:9092"));
		kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, props.getProperty("GroupName", "Main"));
		kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		consumer = new KafkaConsumer<Long, String>(kafkaProps);
		consumer.subscribe(Collections.singletonList(props.getProperty("TopicName")));
		updateIterator();

		while(true) {
			String JsonString = newJson();
			if (JsonString!=null) {
			RawDataObj Rdo = JsonToMap(JsonString);
			System.out.println(Rdo);
			Utils.sleep(1);
			}
		}
	}


	private RawDataObj JsonToMap(String JsonString) {
		RawDataObj o = gson.fromJson(JsonString, RawDataObj.class);
		return o;
	}


	private String newJson() {
		if (recordIterator.hasNext()) {
			return recordIterator.next().value();
		} else {
			updateIterator();
			consumer.commitAsync();
			return null;
		}
	}


	private void updateIterator() {
		final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);
		recordIterator = consumerRecords.iterator();
		if (consumerRecords.count() == 0) {
			Utils.sleep(1000);
		}
	}

}
