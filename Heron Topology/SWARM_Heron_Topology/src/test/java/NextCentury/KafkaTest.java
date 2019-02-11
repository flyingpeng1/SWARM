package NextCentury;

import java.util.Collections;
import java.util.Iterator;
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

import com.google.gson.Gson;
import com.nextcentury.SWARMTopology.Util.SpringScanner;
import com.nextcentury.SWARMTopology.Util.KafkaConfig;
import com.nextcentury.SWARMTopology.Util.RawDataObj;
import com.twitter.heron.api.utils.Utils;

@Service
@Scope("prototype")
@Lazy
public class KafkaTest {

	private static final long serialVersionUID = 1L;

	@Resource
	static KafkaConfig config;

	Gson gson;
	Consumer<Long, String> consumer;
	Iterator<ConsumerRecord<Long, String>> recordIterator;


	public static void main(String args[]) {
		KafkaTest t = new KafkaTest();
		t.run();
	}

	public void run() {
		SpringScanner.initializeSpring();
		config = SpringScanner.getBean(KafkaConfig.class);
		gson = new Gson();

		final Properties kafkaProps = new Properties();
		kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getSpoutKafkaServers());
		kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, config.getSpoutKafkaGroupName());
		kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		consumer = new KafkaConsumer<Long, String>(kafkaProps);
		consumer.subscribe(Collections.singletonList(config.getSpoutKafkaTopicName()));
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
