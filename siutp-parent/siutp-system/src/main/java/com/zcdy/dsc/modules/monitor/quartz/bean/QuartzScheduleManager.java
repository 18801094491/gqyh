package com.zcdy.dsc.modules.monitor.quartz.bean;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import com.zcdy.dsc.common.exception.BusinessException;

/**
 * @author： Roberto
 * 创建时间：2020年3月28日 上午11:46:10
 * 描述: <p>Quartz任务调度管理</p>
 */
@Component("quartzScheduleManager")
public class QuartzScheduleManager {

	// 调度器
	@Resource
	private Scheduler scheduler;

	/**
	 * @param <T>
	 * @author：Roberto
	 * @create:2020年3月28日 上午11:49:21
	 * 描述:<p> 向调度器中添加新的调度任务</p>
	 */
	public void newJob2Scheduler(Class<? extends Job> jobClass, String jobName, String cronExpression, String parameter) throws Exception{
		try {
			// 启动调度器
			scheduler.start();

			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName)
					.usingJobData("parameter", parameter).build();

			// 表达式调度构建器(即任务执行的时间)
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

			// 按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName).withSchedule(scheduleBuilder)
					.build();
			scheduler.scheduleJob(jobDetail, trigger);


		} catch (SchedulerException e) {
			throw new BusinessException("创建定时任务失败", e);
		} catch (RuntimeException e) {
			throw new BusinessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new BusinessException("后台找不到该类名：" + jobName, e);
		}
	}

	/**
	 * 
	 * @author：Roberto
	 * @create:2020年3月28日 上午11:49:05
	 * 描述:<p> 暂停定时任务</p>
	 */
	public void pauseTrigger(String jobName) throws SchedulerException {
		scheduler.pauseTrigger(TriggerKey.triggerKey(jobName));
	}

	/**
	 * @author：Roberto
	 * @create:2020年3月28日 上午11:49:02
	 * 描述:<p>删除调度任务</p>
	 */
	public void schedulerDelete(String jobName) throws SchedulerException {
		scheduler.pauseTrigger(TriggerKey.triggerKey(jobName));
		scheduler.unscheduleJob(TriggerKey.triggerKey(jobName));
		scheduler.deleteJob(JobKey.jobKey(jobName));
	}

	/**
	 * @author：Roberto
	 * @throws SchedulerException 
	 * @create:2020年3月28日 上午11:50:41
	 * 描述:<p>检测触发器是否存在</p>
	 */
	public boolean checkTriggerExists(String jobName) throws SchedulerException {
		return scheduler.checkExists(TriggerKey.triggerKey(jobName));
	}

	/**
	 * @author：Roberto
	 * @throws SchedulerException 
	 * @create:2020年3月28日 上午11:55:14
	 * 描述:<p>唤醒任务</p>
	 */
	public void resumeTrigger(String jobName) throws SchedulerException {
		this.scheduler.resumeTrigger(TriggerKey.triggerKey(jobName));
	}
}
