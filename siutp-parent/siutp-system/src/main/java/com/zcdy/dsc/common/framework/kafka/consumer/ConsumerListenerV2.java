package com.zcdy.dsc.common.framework.kafka.consumer;

import java.util.List;

import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import com.zcdy.dsc.common.framework.kafka.service.KafkaMessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author： Roberto
 * 创建时间：2019年12月24日 上午11:07:15
 * 描述: <p> kafka消费者处理2.0版 </p>
 */
@Slf4j
public class ConsumerListenerV2 implements InitializingBean {

	@Resource
	private KafkaMessageService kafkaMessageService;
	
	/**
	 * @author：Roberto
	 * 描述: <p> 逐条消费处理 </p>
	 */
	@KafkaListener(id = "iot_consumer_${com.zcdy.dsc.kafka.consumer.tail}", 
			topics = {"${kafka.consumer.topic}"})
	public void onMessage(ConsumerRecord<String, String> record) {
		log.info("消费数据");
		String messageContent = record.value();
		log.info("messageContent=========="+messageContent);
		this.kafkaMessageService.handle(messageContent);
	}

	/**
	 * @author：Roberto
	 * 描述: <p> 批量消费 </p>
	 * <p>暂时不用，预留接口；如果需要使用，请开放注释并配置主题</p>
	 */
	// @KafkaListener(topics = "${kafka.consumer.topic}", containerFactory = "batchFactory")
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

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("kafka消费者启动完毕……");
	}
}
