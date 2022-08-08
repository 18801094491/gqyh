package com.zcdy.dsc.common.framework.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : songguang.jiao
 */
@ConditionalOnProperty(prefix="com.zcdy.dsc.kafka",name="consumer", havingValue="on")
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${kafka.consumer.servers}")
    private String servers;
    @Value("${kafka.consumer.enable.auto.commit}")
    private boolean enableAutoCommit;
    @Value("${kafka.consumer.session.timeout}")
    private String sessionTimeout;
    @Value("${kafka.consumer.auto.commit.interval}")
    private String autoCommitInterval;
    @Value("${kafka.consumer.group.id}")
    private String groupId;
    @Value("${kafka.consumer.auto.offset.reset}")
    private String autoOffsetReset;
    @Value("${kafka.consumer.concurrency}")
    private int concurrency;
    
    @Value("${kafka.consumer.max.poll.interval.ms:300000}")
    private int pollInterval;
    @Value("${kafka.consumer.max.poll.records:500}")
    private int pollRecords;
    
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setPollTimeout(1500);
        return factory;
    }

    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }


    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>(10);
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        //拉取的最大时间间隔
        //propsMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, autoOffsetReset);
        //消费者维护session的最大时间
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        //通知消费者重新连接的间隔时间
        //propsMap.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, autoOffsetReset);
        //设置分区拉取阈值
        //propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, autoOffsetReset);
        
        //设置轮询周期
        propsMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, this.pollInterval);
        //分区拉取阈值
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.pollRecords);
        
        return propsMap;
    }

    @Bean
    public ConsumerListenerV2 initConsumerListenerV2(){
    	ConsumerListenerV2 listener = new ConsumerListenerV2();
    	return listener;
    }
}