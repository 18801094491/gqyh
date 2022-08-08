package com.zcdy.dsc.kafka.consumer;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: Roberto
 * @CreateTime:2019年12月24日 上午11:07:15
 * @Description: <p> 消费者 </p>
 */
@Slf4j
public class ConsumerListener implements InitializingBean {

	@KafkaListener(id="iot_data_consumer_${com.zcdy.dsc.kafka.consumer.tail}", topics = "${kafka.consumer.topic}")
	public void onMessage(ConsumerRecord<String, String> record) {
		
	}

	/**
	 * @author:admin
	 * @Description: <p> 批量消费 </p>
	 */
	// @KafkaListener(topics = "${kafka.consumer.topic}", containerFactory =
	// "batchFactory")
	public void onManyMessage(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
		for (ConsumerRecord<String, String> record : records) {
			log.debug(record.toString());
		}
		try {
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 手动提交偏移量
			ack.acknowledge();
		}
	}

	/*
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("kafka消费者启动完毕……");
	}
}
