package com.zcdy.dsc.modules.collection.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipmentVariable;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipmentVariableVo;

/**
 * 描述: 模型设备变量绑定
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-26
 * 版本号: V1.0
 */
public interface IotEquipmentVariableService extends IService<IotEquipmentVariable> {

    void removeByIotId(String id);

    IPage<IotEquipmentVariableVo> queryVariableList(Page<IotEquipmentVariableVo> page, String iotId,String variableName,int status);
    //按周期分组查询变量名称
    void saveRedisIotEquipmentVariableVoByCycle();

}
