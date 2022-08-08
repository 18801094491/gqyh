package com.zcdy.dsc.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.system.entity.SysLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统日志表 Mapper 接口
 * </p>
 *@author : songguang.jiao
 
 * @since 2018-12-26
 */
public interface SysLogMapper extends BaseMapper<SysLog> {

	/**
	 * 功能：清空所有日志记录
	 */
	public void removeAll();

	/**
	 * 获取系统总访问次数
	 *
	 * @return Long
	 */
	Long findTotalVisitCount();

	//update-begin--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数

	/**
	 * 获取系统今日访问次数
	 * @param dayStart 开始日期
	 * @param dayEnd 结算日期
	 * @return
	 */
	Long findTodayVisitCount(@Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd);

	/**
	 * 获取系统今日访问 IP数
	 * @param dayStart 开始日期
	 * @param dayEnd 结束日期
	 * @return
	 */
	Long findTodayIp(@Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd);
	//update-end--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数

	/**
	 * 首页：根据时间统计访问数量/ip数量
	 * @param dayStart 开始日期
	 * @param dayEnd 结束日期
	 * @param dbType 类型
	 * @return
	 */
	List<Map<String,Object>> findVisitCount(@Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd, @Param("dbType") String dbType);
}
