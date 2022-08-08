package com.zcdy.dsc.modules.collection.iot.job;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.modules.collection.iot.constant.ModelStatusConstant;
import com.zcdy.dsc.modules.collection.iot.entity.ModelStatusEntity;
import com.zcdy.dsc.modules.collection.iot.event.AlarmAutoCloseEvent;
import com.zcdy.dsc.modules.constant.ProcessRouteConstant;
import com.zcdy.dsc.modules.operation.alarm.constant.WarnStatusConstant;
import com.zcdy.dsc.modules.operation.alarm.entity.BusinessWarn;
import com.zcdy.dsc.modules.operation.alarm.service.BusinessWarnService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 描述: <p>定时扫描将报警数据,已恢复正常的自动解除(只查询到初始化的数据,解除警报)</p> 
 * <p>只处理处于待确认状态的数据，如果已经确认需要人工干预，系统不做处理</p>
 * @author： songguang.jiao 
 * 创建时间： 2020年4月9日 上午10:01:18 
 * 版本号: V1.0
 */
public class AlarmAutoResumeJob implements Job {

	@Resource
	private BusinessWarnService businessWarnService;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Resource
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		// 查询初始化的数据
		QueryWrapper<BusinessWarn> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(BusinessWarn::getConfirmStatus, WarnStatusConstant.CONFIRM_WILL);
		List<BusinessWarn> list = businessWarnService.list(queryWrapper);
		//采集设备id
		String iotId = null;
		String statusConstant = null;
		ModelStatusEntity statusEntity = null;
		Date now = new Date();
		
		for (BusinessWarn businessWarn : list) {
			iotId = businessWarn.getIotId();
			//获取当前采集设备的状态信息，如果设备正常了，但是告警事件尚未确认，系统就关闭该告警事件
			statusConstant =stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstantV2.MODEL_LAST_STATUS_KEY, iotId));
			if(null==statusConstant){
				continue;
			}
			statusEntity = JSON.parseObject(statusConstant, ModelStatusEntity.class);
			//如果当前设备已经正常了
			if(statusEntity.getStatus().equals(ModelStatusConstant.STATUS_NORMAL)){
				//再次查询当前设备的数据库状态
				BusinessWarn one = this.businessWarnService.getById(businessWarn.getId());
				if(WarnStatusConstant.CONFIRM_WILL.equals(one.getConfirmStatus())){
					one = new BusinessWarn();
					one.setId(businessWarn.getId());
					one.setConfirmStatus(WarnStatusConstant.CLOSED);
					one.setWarnStatus(WarnStatusConstant.DEAL_CLOSED);
					one.setProcessRoute(ProcessRouteConstant.ROUTE_SYSTEM);
					one.setOperateTime(now);
					one.setDescription("系统自动关闭告警事件");
					long duration = DateUtil.getSeconds(DateUtil.convertDateToLocalDate(businessWarn.getWarnTime()),DateUtil.convertDateToLocalDate(now));
					one.setDuration(Long.toString(duration));
					this.businessWarnService.updateById(one);
					
					//发布事件自动关闭的事件
					AlarmAutoCloseEvent event = new AlarmAutoCloseEvent(this, businessWarn);
					this.applicationEventPublisher.publishEvent(event);
				}
			}
		}
	}
}
