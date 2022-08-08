package com.zcdy.dsc.collector.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ioserver.bean.Struct_TagInfo;
import com.ioserver.dll.IOServerAPICilent;
import com.sun.jna.WString;
import com.zcdy.dsc.collector.ioserver.IOServerClientBean;
import com.zcdy.dsc.collector.ioserver.entity.VariableData;
import com.zcdy.dsc.collector.kafka.producer.KafkaProducerBean;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: Roberto
 * @CreateTime:2019年12月16日 下午2:56:23
 * @Description: <p></p>
 */
@Component
@Slf4j
@Order(10)
public class VariableCollectTask implements InitializingBean {

	// IOServer连接器bean
	@Resource
	private IOServerClientBean ioServerBean;

	@Autowired
	private KafkaProducerBean kafkaProducerBean;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Value("${kafka.consumer.topic}")
	private String kafkaTopic;

	@Value("${com.zcdy.dsc.kafka.key}")
	private String kafkaKey;

	private final Random random = new Random(10L);

	public VariableCollectTask() {
	}

	public VariableCollectTask(IOServerClientBean ioServerBean) {
		this.ioServerBean = ioServerBean;
	}

	/**
	 * @author:Roberto
	 * @create:2019年12月31日 下午4:44:00
	 * @Description:<p></p>
	 */
	public void fetchData(List<String> varNames) {
		IOServerAPICilent client = ioServerBean.getIoServer();
		if (null == client) {
			if (random.nextInt() % 3 == 0) {
				try {
					this.ioServerBean.connect();
				} catch (IOException e) {
				}
				client = ioServerBean.getIoServer();
			}
		}

		if (null == client) {
			return;
		}

		if (null == varNames || varNames.size() == 0) {
			log.warn("待采集的变量数组是空的……");
			return;
		}
		int size = varNames.size();
		WString[] arrays = new WString[size];
		for (int index = 0; index < size; index++) {
			arrays[index] = new WString(varNames.get(index));
		}
		List<VariableData> varDatas = new ArrayList<>(size);
		Struct_TagInfo[] values = client.SyncReadTagsValueByNames(client.getHandle(), arrays, size, 0);

		long nowTimeStamp = System.currentTimeMillis();
		for (int index = 0; index < size; index++) {
			Struct_TagInfo value = values[index];
			VariableData bean = VariableData.template.clone();
			switch ((int) value.TagValue.ValueType) {
			case 1:
				bean.setVarValue(value.TagValue.TagValue.bitVal.toString());
				break;
			case 2:
				bean.setVarValue(value.TagValue.TagValue.i1Val.toString());
				break;
			case 3:
				bean.setVarValue(value.TagValue.TagValue.i1Val.toString());
				break;
			case 4:
				bean.setVarValue(value.TagValue.TagValue.i2Val.toString());
				break;
			case 5:
				bean.setVarValue(value.TagValue.TagValue.i2Val.toString());
				break;
			case 6:
				bean.setVarValue(value.TagValue.TagValue.i4Val.toString());
				break;
			case 7:
				bean.setVarValue(value.TagValue.TagValue.i4Val.toString());
				break;
			case 8:
				bean.setVarValue(value.TagValue.TagValue.i8Val.toString());
				break;
			case 9:
				bean.setVarValue(value.TagValue.TagValue.r4Val.toString());
				break;
			case 10:
				bean.setVarValue(value.TagValue.TagValue.r8Val.toString());
				break;
			case 11:
				bean.setVarValue(value.TagValue.TagValue.wstrVal.toString());
				break;
			default:
				bean.setVarValue("0");
				break;
			}
			bean.setVarId(value.TagID);
			bean.setVarName(varNames.get(index));
			//不能使用此方式，如果IOServer采集变量周期过快会导致一个设备的相关变量采集的时间戳不在同一个时间点，
			//无法匹配为一个采集点数据 
			//bean.setTimestamp(value.TimeStamp.Seconds.longValue() * 1000 + value.TimeStamp.MillSeconds);
			bean.setTimestamp(nowTimeStamp);
			bean.setCreated(nowTimeStamp);
			bean.setQualityStamp(value.QualityStamp);
			varDatas.add(bean);

			if (varDatas.size() >= 20) {
				String messageContent = JSON.toJSONString(varDatas);
				log.info("Kafka send message content-> {}", messageContent);
				if (log.isDebugEnabled()) {
					log.debug("Kafka send message content-> {}", messageContent);
				}
				kafkaSend(messageContent);
				varDatas.clear();
			}
		}

		if (varDatas.size() > 0) {
			String messageContent = JSON.toJSONString(varDatas);
			log.info("Kafka send message content-> {}", messageContent);
			if (log.isDebugEnabled()) {
				log.debug("Kafka send message content-> {}", messageContent);
			}
			kafkaSend(messageContent);
		}
	}

	/**
	 * @author:Roberto
	 * @create:2019年12月31日 下午5:26:19
	 * @Description:<p>发送kafka消息</p>
	 */
	private void kafkaSend(String messageContent) {

		kafkaProducerBean.send(kafkaTopic, kafkaKey, messageContent);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
