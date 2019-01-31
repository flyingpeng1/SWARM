package com.nextcentury.SWARMTopology.Bolts;

import java.util.Properties;

import javax.annotation.Resource;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.nextcentury.SWARMTopology.Util.KafkaConfig;

@Service
@Scope("prototype")
@Lazy
public class NormalizedPubSubBolt extends KafkaPublisher {

    @Resource
    KafkaConfig config;
    
	private static final long serialVersionUID = 1L;
	public final static String NORMALIZED_PUBSUB_NODE = "NormalizedPubSubNode";

	@Override
	protected Properties getKafkaProps() {
		final Properties kafkaProps = new Properties();
		kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getNormOutKafkaServers());
		kafkaProps.put(ProducerConfig.CLIENT_ID_CONFIG, config.getNormOutKafkaGroupName());
		kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return kafkaProps;
	}

	@Override
	protected String getKafkaTopic() {
		return config.getNormOutKafkaTopicName();
	}

}
