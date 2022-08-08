package com.zcdy.dsc.modules.monitor.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.monitor.quartz.entity.QuartzJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 描述: 定时任务在线管理
 @author : songguang.jiao
 
 * 版本号: V1.1
 */
public interface IQuartzJobService extends IService<QuartzJob> {

	List<QuartzJob> findByJobClassName(String jobClassName);

	boolean saveAndScheduleJob(QuartzJob quartzJob);

	boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException;

	boolean deleteAndStopJob(QuartzJob quartzJob);

	boolean resumeJob(QuartzJob quartzJob);
}
