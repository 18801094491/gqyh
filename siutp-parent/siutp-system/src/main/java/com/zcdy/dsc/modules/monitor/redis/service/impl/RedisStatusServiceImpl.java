package com.zcdy.dsc.modules.monitor.redis.service.impl;

import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.monitor.entity.RedisInfo;
import com.zcdy.dsc.modules.monitor.exception.RedisConnectException;
import com.zcdy.dsc.modules.monitor.redis.service.RedisStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author： Roberto
 * 创建时间：2020年3月12日 上午9:33:51
 * 描述: <p>redis服务状态服务</p>
 */
@Service("redisStatusService")
@Slf4j
public class RedisStatusServiceImpl implements RedisStatusService {

	@Resource
	private RedisConnectionFactory redisConnectionFactory;

	/**
	 * Redis详细信息
	 */
	@Override
	public List<RedisInfo> getRedisInfo() throws RedisConnectException {
		Properties info = redisConnectionFactory.getConnection().info();
		List<RedisInfo> infoList = new ArrayList<>();
		RedisInfo redisInfo = null;
		for (Map.Entry<Object, Object> entry : info.entrySet()) {
			redisInfo = new RedisInfo();
			redisInfo.setKey(ConvertUtils.getString(entry.getKey()));
			redisInfo.setValue(ConvertUtils.getString(entry.getValue()));
			infoList.add(redisInfo);
		}
		return infoList;
	}

	@Override
	public Map<String, Object> getKeysSize() throws RedisConnectException {
		Long dbSize = redisConnectionFactory.getConnection().dbSize();
		Map<String, Object> map = new HashMap<>(2);
		map.put("create_time", System.currentTimeMillis());
		map.put("dbSize", dbSize);

		log.info("--getKeysSize--: " + map.toString());
		return map;
	}

	@Override
	public Map<String, Object> getMemoryInfo() throws RedisConnectException {
		Map<String, Object> map = null;
		Properties info = redisConnectionFactory.getConnection().info();
		for (Map.Entry<Object, Object> entry : info.entrySet()) {
			String key = ConvertUtils.getString(entry.getKey());
			if ("used_memory".equals(key)) {
				map = new HashMap<>(2);
				map.put("used_memory", entry.getValue());
				map.put("create_time", System.currentTimeMillis());
			}
		}
		log.info("--getMemoryInfo--: " + map.toString());
		return map;
	}
}
