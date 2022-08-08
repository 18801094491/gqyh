package com.zcdy.dsc.modules.collection.gis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.collection.gis.vo.IotVariableInfoVo;

/**
 * gis 模型跟变量相关接口
 * 描述: 
 * @author：  songguang.jiao
 * 创建时间：  2020年1月6日 上午9:54:29
 * 版本号: V1.0
 */
public interface IGisIotService {

	
	/**
	 * 描述: 查询变量是否被绑定
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月6日 上午9:58:43
	 * 版本号: V1.0
	 */
	public Integer bindStatus(String variableId);
	
	/**
	 * 描述: 查询模型已经绑定变量
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月6日 上午11:18:17
	 * 版本号: V1.0
	 */
	public IPage<IotVariableInfoVo> getModelBindList(IPage<IotVariableInfoVo> page,String modelId);
	
	/**
	 * 描述: Gis模型跟变量解除绑定
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月6日 上午11:42:04
	 * 版本号: V1.0
	 */
	public void unBindIotVar(String variableId);
	
	/**
	 * 描述: 查询所有变量列表
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月6日 上午11:18:17
	 * 版本号: V1.0
	 */
	public IPage<IotVariableInfoVo> getIotInfoList(IPage<IotVariableInfoVo> page,String variableName,String variableTitle);
}
