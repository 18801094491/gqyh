package com.zcdy.dsc.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.system.entity.SysDataLog;

/**
 * @author : songguang.jiao
 */
public interface ISysDataLogService extends IService<SysDataLog> {
	
	/**
	 * 添加数据日志
	 * @param tableName
	 * @param dataId
	 * @param dataContent
	 */
	public void addDataLog(String tableName, String dataId, String dataContent);

}
