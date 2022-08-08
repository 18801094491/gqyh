package com.zcdy.dsc.modules.collection.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.collection.iot.entity.GisIotEquipEntity;
import com.zcdy.dsc.modules.collection.iot.entity.IotOperateModeEntityDefault;
import com.zcdy.dsc.modules.collection.iot.entity.IotOperatingMode;
import com.zcdy.dsc.modules.collection.iot.vo.OperateEquipInfomation.OperateEquipInfoVo;
import com.zcdy.dsc.modules.collection.iot.vo.OperateEquipPageParam;

import java.util.List;

/**
 * @Description: 工况监控关联设备信息
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-05-21
 * @Version: V1.0
 */
public interface IotOperatingModeService extends IService<IotOperatingMode> {

    /**
     * 所有设备列表
     * @author songguang.jiao
     * @date 2020/05/21 15:32:25
     * @param param
     * @return
     */
    IPage<OperateEquipInfoVo> getAllEquip( Page<OperateEquipInfoVo> page,OperateEquipPageParam param);

    /**
     * 监控的设备列表
     * @author songguang.jiao
     * @date 2020/05/21 16:48:12
     * @return
     */
    List<GisIotEquipEntity> getOperateModeEquip();

    /**
     * 默认地区检测点
     * @return
     */
    List<IotOperateModeEntityDefault> getDefaultModeEquip();
}
