package NextCentury;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.nextcentury.SWARMTopology.Util.SpringScanner;
import com.nextcentury.SWARMTopology.Util.KafkaConfig;
import com.nextcentury.SWARMTopology.Util.RawDataObj;
import com.twitter.heron.api.generated.TopologyAPI.StreamId;
import com.twitter.heron.api.tuple.Fields;
import com.twitter.heron.api.tuple.Tuple;
import com.twitter.heron.api.utils.Utils;

@Service
@Scope("prototype")
@Lazy
public class KafkaTest2 {

	private static final long serialVersionUID = 1L;

    @Resource
    KafkaConfig config;

	private KafkaProducer<Long, String> producer;

	public static void main(String args[]) {
		KafkaTest2 t = new KafkaTest2();
		t.run();
	}

	public void run() {
		SpringScanner.initializeSpring();
		config = SpringScanner.getBean(KafkaConfig.class);
		
		
		//prepare
		producer = new KafkaProducer<Long, String>(this.getKafkaProps());
		
		//excecute
		String payload = buildJsonFromTuple(new MockTuple());
		System.out.println(buildJsonFromTuple(new MockTuple()));
		
		ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(getKafkaTopic(), payload);
		
		try {
			RecordMetadata m = producer.send(record).get();
			System.out.println(m.timestamp());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
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
	

	protected Properties getKafkaProps() {
		final Properties kafkaProps = new Properties();
		kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getNormOutKafkaServers());
		kafkaProps.put(ProducerConfig.CLIENT_ID_CONFIG, config.getNormOutKafkaGroupName());
		kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return kafkaProps;
	}

	protected String getKafkaTopic() {
		return config.getNormOutKafkaTopicName();
	}

}

