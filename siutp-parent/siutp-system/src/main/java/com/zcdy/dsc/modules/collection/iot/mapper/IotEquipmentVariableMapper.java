package com.zcdy.dsc.modules.collection.iot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipmentVariable;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipmentVariableVo;

/**
 * 描述: 模型设备变量绑定
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-26
 * 版本号: V1.0
 */
public interface IotEquipmentVariableMapper extends BaseMapper<IotEquipmentVariable> {

    void removeByIotId(@Param("id") String id);

    IPage<IotEquipmentVariableVo> queryVariableList(Page<IotEquipmentVariableVo> page,@Param("iotId") String iotId,
                                                    @Param("variableName") String variableName,@Param("status") int status);

    List<IotEquipmentVariableVo> queryIotEquipmentVariableVoByCycle();



}
