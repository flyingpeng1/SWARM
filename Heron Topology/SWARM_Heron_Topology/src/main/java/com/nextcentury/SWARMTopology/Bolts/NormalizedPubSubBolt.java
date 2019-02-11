package com.nextcentury.SWARMTopology.Bolts;

import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.nextcentury.SWARMTopology.SWARMTupleSchema;
import com.nextcentury.SWARMTopology.Util.KafkaConfig;
import com.nextcentury.SWARMTopology.Util.SpringScanner;
import com.nextcentury.SWARMTopology.Util.TopologyConfig;
import com.twitter.heron.api.bolt.OutputCollector;
import com.twitter.heron.api.topology.OutputFieldsDeclarer;
import com.twitter.heron.api.topology.TopologyContext;
import com.twitter.heron.api.tuple.Tuple;

@Service
@Scope("prototype")
@Lazy
public class NormalizedPubSubBolt extends KafkaPublisher {

	@Resource
	KafkaConfig config;
	@Resource
	TopologyConfig TConf;
	OutputCollector oc;

	private static final long serialVersionUID = 1L;
	public final static String NORMALIZED_PUBSUB_NODE = "NormalizedPubSubNode";

	@Override
	public void prepare(Map<String, Object> heronConf, TopologyContext context, OutputCollector collector) {
		SpringScanner.initializeSpring();
		config = SpringScanner.getBean(KafkaConfig.class);
		TConf = SpringScanner.getBean(TopologyConfig.class);
		super.prepare(heronConf, context, collector);
		oc = collector;
	}

	@Override
	public void execute(Tuple input) {
		super.execute(input);
		if (TConf.getEnableMetricsNode()) {
			oc.emit(input.getValues());
		}
	}

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

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(SWARMTupleSchema.getNormalizedSchema());
	}

}
