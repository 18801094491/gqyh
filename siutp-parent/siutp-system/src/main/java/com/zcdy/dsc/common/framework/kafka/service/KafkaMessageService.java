package com.zcdy.dsc.common.framework.kafka.service;

/**
 * @author： Roberto
 * 创建时间：2020年3月4日 下午6:19:10
 * 描述: <p>kafka消息处理接口</p>
 */
public interface KafkaMessageService {

	/**
	 * @author：Roberto
	 * @create:2020年3月4日 下午6:20:20
	 * 描述:<p>kafka消息处理</p>
	 */
	public void handle(String messageContent);
}
