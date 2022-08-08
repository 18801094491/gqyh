package com.zcdy.dsc.modules.collection.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipment;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipmentVo;

import java.util.List;

/**
 * 描述: 模型设备维护
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-26
 * 版本号: V1.0
 */
public interface IotEquipmentService extends IService<IotEquipment> {
    List<IotEquipment> queryIotEquipmentByName(String iotSn, String iotName,String id);

    IPage<IotEquipmentVo> queryPageList(Page<IotEquipmentVo> page, IotEquipment iotEquipment);

    void updateCycleByCate(String iotCategory, String iotCycle);

	/**
	 * @author：Roberto
	 * @create:2020年3月7日 下午11:58:42
	 * 描述:<p>查询指定类型的设备的id</p>
	 */
	List<String> listIdsByType(String typeCode);

	/**
	 * 描述: 
	 * @author: songguang.jiao
	 * 创建日期:  2020年4月9日 下午7:30:23
	 * 版本: V1.0
	 */
	String queryEquipmentById(String id);

//    List<IotEquipment> queryPageList(Page<IotEquipment> page, IotEquipment iotEquipment);
}
