package com.zcdy.dsc.modules.collection.gis.job;

import com.alibaba.fastjson.JSON;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.common.framework.websocket.WebSocket;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.modules.collection.gis.vo.GISModelMessage;
import com.zcdy.dsc.modules.collection.gis.vo.GISModelMessage.MessageBody;
import com.zcdy.dsc.modules.collection.gis.vo.IotDataVO;
import com.zcdy.dsc.modules.collection.gis.vo.OperationSatus;
import com.zcdy.dsc.modules.collection.gis.websocket.GISUserWebsocketManager;
import com.zcdy.dsc.modules.collection.iot.constant.ModelStatusConstant;
import com.zcdy.dsc.modules.collection.iot.entity.Iot2GisEntity;
import com.zcdy.dsc.modules.collection.iot.entity.ModelStatusEntity;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.service.IOTModelService;
import com.zcdy.dsc.modules.monitor.quartz.bean.QuartzScheduleManager;
import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author： Roberto
 * 创建时间：2019年12月24日 下午3:48:36
 * 描述: <p>消费iot数据任务</p>
 */
public class GisModelJob implements Job {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Resource
	private WebSocket websocket;

	@Resource
	private IOTModelService iotModelService;
	
	@Resource
	private QuartzScheduleManager quartzScheduleManager;

	//定时任务参数
	private String parameter;
	
	@Override
	public void execute(JobExecutionContext arguments) throws JobExecutionException {
		String userId = this.parameter;

		//如果当前用户通道已经关闭，或临时关闭不发送内容
		if (!GISUserWebsocketManager.exits(userId)) {
			try {
				quartzScheduleManager.pauseTrigger("GIS-iot-" + userId);
			} catch (SchedulerException e) {
				logger.error("暂停用户【"+userId+"】的任务失败……");
				logger.error(e.getMessage());
			}
			return;
		}
		
		//获取所有的模型信息，并缓存
		// 通过资产找到采集模型
		List<Iot2GisEntity> iot2Gis = this.iotModelService.getIot2Gis();
		
		List<VariableInfo> vars = null;
		int varCount = 0;
		boolean hasVar = false;
		
		//遍历所有的模型
		for(Iot2GisEntity model : iot2Gis){
			//根据模型查询所有设备的状况(正常、异常、开、关)
			String statusConstant = stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstantV2.MODEL_LAST_STATUS_KEY, model.getIotId()));
			if(null==statusConstant){
				continue;
			}
			
			ModelStatusEntity statusEntity = JSON.parseObject(statusConstant, ModelStatusEntity.class);
			OperationSatus operationSatus = OperationSatus.OPERATION_SATUS.clone();
			operationSatus.setMessage(statusEntity.getMessage());
			operationSatus.setWorkStatus(statusEntity.getStatus());
			operationSatus.setSwitchSatus(statusEntity.getDetailStatus());
			
			MessageBody body = new MessageBody();
			
			vars = this.iotModelService.getVarsByModelId(model.getGisId());
			varCount  = vars.size();
			hasVar = varCount>0;
			if(statusEntity.getStatus().equals(ModelStatusConstant.STATUS_NORMAL) || 
					(statusEntity.getStatus().equals(ModelStatusConstant.STATUS_WARNING) 
							&& statusEntity.getDetailStatus().equals(ModelStatusConstant.STATUS_WARNING_DATAALARM))){
				//根据变量查询所有的采集信息
				
				List<IotDataVO> data = new ArrayList<>(varCount);
				if(hasVar){
					vars.forEach(item->{
						String messageStr = stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstantV2.REDISDATAKEY, item.getVarName()));
						if (!StringUtils.isEmpty(messageStr)){
							
							ValueEntity value = JSON.parseObject(messageStr, ValueEntity.class);
							IotDataVO dataVO = new IotDataVO();
							dataVO.setVariableName(item.getVarTitle());
							if(null==value.getValue()){
								dataVO.setVaribleValue("");
							}else{
								BigDecimal bg = new BigDecimal(value.getValue());
								//使用小数位控制
								int scale = null==item.getScale()?2:item.getScale().intValue();
								if(scale>=0){
									bg = bg.setScale(scale, RoundingMode.HALF_UP);
								}
								if(!StringUtils.isEmpty(item.getUnited())){
									dataVO.setVaribleValue(bg.toEngineeringString() + "["+item.getUnited()+"]");
								}else{
									dataVO.setVaribleValue(bg.toEngineeringString());
								}
							}
							data.add(dataVO);
						}
					});
					body.setData(data);
				}
			}
			
			//如果没有绑定变量，那么只考虑状态变更
			if(!hasVar){
				//设备上次的状态
				String prevStatusConstant = stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstantV2.MODEL_PREV_STATUS_KEY, model.getIotId()));
				if(null!=prevStatusConstant){
					//上次的状态
					ModelStatusEntity prevStatusEntity = JSON.parseObject(prevStatusConstant, ModelStatusEntity.class);
					//设备状态没有发生改变
					if(statusEntity.getStatus().equals(ModelStatusConstant.STATUS_NORMAL) && statusEntity.getDetailStatus().equals(ModelStatusConstant.STATUS_NORMAL_OPEN)
							&&prevStatusEntity.getStatus().equals(statusEntity.getStatus()) && prevStatusEntity.getDetailStatus().equals(statusEntity.getDetailStatus())){
						changePrevStatus(model.getIotId(), statusConstant);
						//不推送状态
						continue;
					}
				}
				changePrevStatus(model.getIotId(), statusConstant);
			}
			
			GISModelMessage message = new GISModelMessage();
			// 这里恒定是2-表示更新
			body.setStatus(2);
			body.setId(model.getGisId());
			body.setOpration(operationSatus);
			message.setBody(body);
			this.websocket.sendOneMessage(userId, JSON.toJSONString(message));
		}
	}

	/**
	 * @author：Roberto
	 * @create:2020年3月13日 上午9:35:10
	 * 描述:<p>变更设备的上次状态</p>
	 */
	private void changePrevStatus(String iotId, String statusConstant) {
		//将最新的状态记入上次状态
		stringRedisTemplate.opsForValue().set(String.format(RedisKeyConstantV2.MODEL_PREV_STATUS_KEY, iotId), statusConstant);
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
