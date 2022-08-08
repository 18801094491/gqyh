package com.zcdy.dsc.modules.monitor.redis.service;

import com.zcdy.dsc.modules.monitor.entity.RedisInfo;
import com.zcdy.dsc.modules.monitor.exception.RedisConnectException;

import java.util.List;
import java.util.Map;
/**
 * @author： admin
 * 创建时间：2019年12月24日 下午4:53:23
 * 描述: <p>与redis服务</p>
 */
public interface RedisStatusService {

	/**
	 * 获取 redis 的详细信息
	 *
	 * @return List
	 */
	List<RedisInfo> getRedisInfo() throws RedisConnectException;

	/**
	 * 获取 redis key 数量
	 *
	 * @return Map
	 */
	Map<String, Object> getKeysSize() throws RedisConnectException;

	/**
	 * 获取 redis 内存信息
	 *
	 * @return Map
	 */
	Map<String, Object> getMemoryInfo() throws RedisConnectException;

}
