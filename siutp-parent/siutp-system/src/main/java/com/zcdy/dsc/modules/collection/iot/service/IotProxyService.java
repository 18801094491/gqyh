package com.zcdy.dsc.modules.collection.iot.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.collection.iot.entity.IotProxy;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipVo;
import com.zcdy.dsc.modules.collection.iot.vo.IotProxyVo;

/**
 * 描述: 采集代理管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-13
 * 版本号: V1.0
 */
public interface IotProxyService extends IService<IotProxy> {

	/**
	 * 描述:  更改代理状态
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月13日 下午1:43:39 
	 * 版本号: V1.0
	 */
	void editProxyStatus(String id);

	/**
	 * 描述:  查询采集代理列表
	 * @return
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月13日 下午2:08:47 
	 * 版本号: V1.0
	 */
	List<IotProxyVo> queryData();

	/**
	 * 描述:  分页查询所有采集设备列表
	 * @return
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月13日 下午2:45:59 
	 * 版本号: V1.0
	 */
	IPage<IotEquipVo> queryAllEquip(Page<IotEquipVo> page, String iotSn, String iotName, String iotCategory);

	/**
	 * 描述:  单个代理已绑定的采集设备列表
	 * @return
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月13日 下午3:13:04 
	 * 版本号: V1.0
	 */
	IPage<IotEquipVo> getBindEquip(Page<IotEquipVo> page,String proxyId,String iotSn, String iotName, String iotCategory);

	/**
	 * 描述:  绑定全部采集设备
	 * @param proxyId 采集代理id
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月13日 下午6:07:42 
	 * 版本号: V1.0
	 */
	void bindAll(String proxyId);

	/***
	 * 描述:  按照类型绑定采集设备
	 * @param category 设备分类
	 * @param proxyId  采集代理id
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月13日 下午9:34:44 
	 * 版本号: V1.0
	 */
	void bindCate(String category, String proxyId);

	
	
}
