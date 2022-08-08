package com.zcdy.dsc.modules.monitor.quartz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.monitor.quartz.entity.QuartzJob;

/**
 * 描述: 定时任务在线管理
 
 
 * 版本号: V1.0
 */
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {

	public List<QuartzJob> findByJobClassName(@Param("jobClassName") String jobClassName);

}
