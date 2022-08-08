package com.zcdy.dsc.modules.collection.iot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.common.framework.influxdb.entity.PointData;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntityPool;
import com.zcdy.dsc.common.framework.kafka.service.KafkaMessageService;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.modules.collection.iot.entity.IOTVarData;
import com.zcdy.dsc.modules.collection.iot.entity.VariableData;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author： Roberto
 * 创建时间：2020年3月4日 下午6:27:20
 * 描述: <p>kafka消息处理采集变量采集信息</p>
 */
@Service("kafkaMessageService")
public class KafkaMessageVariableServiceImpl implements KafkaMessageService {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	//influxdb操作类
	@Resource
	private InfluxService influxService;
	
	@Resource
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Resource
	private ValueEntityPool valueEntityPool;
	
	private final int NUMBER_SCALE = 6;
	
	/**
	 * @see
	 * com.zcdy.dsc.common.framework.kafka.service.KafkaMessageService#handle(java.lang.String)
	 */
	@Override
	public void handle(String messageContent) {
		List<VariableData> datas = null;
		if (!StringUtils.isEmpty(messageContent)) {
			datas = JSON.parseObject(messageContent, new TypeReference<List<VariableData>>() {
			});
		} else{
			return;
		}
		List<PointData> pointDatas = new ArrayList<>();
		for (VariableData data : datas) {
			String varValue = data.getVarValue();
			int qualityStamp = data.getQualityStamp();
			varValue = dealValue(varValue);
			//ValueEntity bean = ValueEntity.template.clone();
			ValueEntity bean = this.valueEntityPool.get();
			
			bean.setTimestamp(data.getTimestamp()+"");
			bean.setValue(varValue);
			bean.setVariableId(data.getVarId());
			String vname = data.getVarName();
			bean.setVariableName(vname);
			bean.setQualityStamp(qualityStamp+"");
			//获取模型管理端模型设备id
			if(StringUtils.isEmpty(vname)){
				continue;
			}
			
			//redis始终存储最新的变量采集
			// 始终是最新的一条
			this.stringRedisTemplate.opsForValue().set(String.format(RedisKeyConstantV2.REDISDATAKEY, vname), JSON.toJSONString(bean));
			
			this.valueEntityPool.release(bean);
			
			IOTVarData item = new IOTVarData();
			item.setCreateTime(new Date());
			item.setTimestamp(data.getTimestamp());
			item.setVarName(vname);
			item.setVarValue(varValue);
			item.setQualityStamp(qualityStamp+"");
			
			PointData pointData = PointData.POINT_DATA.clone();
			pointData.setTime(data.getTimestamp());
			pointData.setVarName(vname);
			pointData.setVarValue(Double.valueOf(varValue));
			pointData.setQualityStamp(qualityStamp);
			pointDatas.add(pointData);
			
			if (pointDatas.size() >= 100) {
				this.influxService.writeBatchWithTime(pointDatas, PointData.class);
				pointDatas.clear();
			}
		}
		
		if (pointDatas.size() > 0) {
			this.influxService.writeBatchWithTime(pointDatas, PointData.class);
		}
	}

	/**
	 * @author：Roberto
	 * @create:2020年3月25日 上午9:47:16
	 * 描述:<p>处理采集数据，将true和false转换为1,0，空串转为0</p>
	 */
	private String dealValue(String varValue) {
		String result = "0";
		if (null == varValue || "".equals(varValue)) {
			return result;
		}
		switch (varValue.toLowerCase()) {
		case "true":
			result = "1";
			break;
		case "false":
			result = "0";
			break;
		default:
			BigDecimal bg = new BigDecimal(varValue);
			result = bg.setScale(NUMBER_SCALE,RoundingMode.HALF_UP).toString();
			break;
		}
		return result;
	}
}
