package com.zcdy.dsc.collector.kafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author: Roberto
 * @CreateTime:2019年12月24日 上午11:01:38
 * @Description: <p></p>
 */
@ConditionalOnClass(value=KafkaProducerConfig.class)
@Component
public class KafkaProducerBean {

	@Autowired
	private KafkaTemplate<String,String> kafkaTemplate;
	
	public void send(String topic, String key, String message){

		this.kafkaTemplate.send(new ProducerRecord<String, String>(topic, key, message));
	}
}
