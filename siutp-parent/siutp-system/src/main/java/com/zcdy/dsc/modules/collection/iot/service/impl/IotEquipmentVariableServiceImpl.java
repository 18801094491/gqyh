package com.zcdy.dsc.modules.collection.iot.service.impl;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipmentVariable;
import com.zcdy.dsc.modules.collection.iot.mapper.IotEquipmentVariableMapper;
import com.zcdy.dsc.modules.collection.iot.service.IotEquipmentVariableService;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipmentVariableVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述: 模型设备变量绑定
 * @author： 智能无人硬核心项目组
 * 创建时间： 2020-02-26
 * 版本号: V1.0
 */
@Service
public class IotEquipmentVariableServiceImpl extends ServiceImpl<IotEquipmentVariableMapper, IotEquipmentVariable>
		implements IotEquipmentVariableService {
	@Resource
	IotEquipmentVariableMapper iotEquipmentVariableMapper;
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Value("${com.zcdy.dsc.Ioserver.variable.url}")
	private String ioserverVariableUrl;

	@Override
	public void removeByIotId(String id) {
		iotEquipmentVariableMapper.removeByIotId(id);

	}

	@Override
	public IPage<IotEquipmentVariableVo> queryVariableList(Page<IotEquipmentVariableVo> page, String iotId,
			String variableName, int status) {
		return iotEquipmentVariableMapper.queryVariableList(page, iotId, variableName, status);
	}

	@Override
	public void saveRedisIotEquipmentVariableVoByCycle() {
		// 按照采集周期分组，取出需要采集的变量
		List<IotEquipmentVariableVo> listIotEquipmentVariableVo = iotEquipmentVariableMapper
				.queryIotEquipmentVariableVoByCycle();
		if (listIotEquipmentVariableVo != null && listIotEquipmentVariableVo.size() > 0) {
			Map<String, Set<String>> hash = new HashMap<String, Set<String>>(listIotEquipmentVariableVo.size());
			for (IotEquipmentVariableVo iotEquipmentVariableVo : listIotEquipmentVariableVo) {
				String iotCycle = iotEquipmentVariableVo.getIotCycle();
				Set<String> varNames = hash.get(iotCycle);
				if(null==varNames){
					varNames = new HashSet<String>();
					hash.put(iotCycle, varNames);
				}
				String variableName = iotEquipmentVariableVo.getVariableName();
				varNames.add(variableName);
			}
			StringBuilder body = new StringBuilder();
			hash.forEach((key, value)->{
				body.append(key).append("=").append(StringUtils.join(value, ',')).append("&");
			});
			if(body.length()==1){
				return;
			}
			String bodyContent = body.substring(0, body.length()-1);
			String url = ioserverVariableUrl + "/iot/task/getValuesByCycle";
			try {
				String result = HttpUtil.post(url, bodyContent);
				log.error(result);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}
}
