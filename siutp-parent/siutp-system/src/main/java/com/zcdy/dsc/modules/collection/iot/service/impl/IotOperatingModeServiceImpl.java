package com.zcdy.dsc.modules.collection.iot.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.collection.iot.entity.GisIotEquipEntity;
import com.zcdy.dsc.modules.collection.iot.entity.IotOperateModeEntityDefault;
import com.zcdy.dsc.modules.collection.iot.entity.IotOperatingMode;
import com.zcdy.dsc.modules.collection.iot.mapper.IotOperatingModeMapper;
import com.zcdy.dsc.modules.collection.iot.service.IotOperatingModeService;
import com.zcdy.dsc.modules.collection.iot.vo.OperateEquipInfomation.OperateEquipInfoVo;
import com.zcdy.dsc.modules.collection.iot.vo.OperateEquipPageParam;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 工况监控关联设备信息
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-05-21
 * @Version: V1.0
 */
@Service
public class IotOperatingModeServiceImpl extends ServiceImpl<IotOperatingModeMapper, IotOperatingMode> implements IotOperatingModeService {

    @Resource
    private IotOperatingModeMapper iotOperatingModeMapper;
    
    @Override
    public IPage<OperateEquipInfoVo> getAllEquip(Page<OperateEquipInfoVo> page,OperateEquipPageParam param) {
         return iotOperatingModeMapper.selectAllEquip(page,param);
    }

    @Override
    public List<GisIotEquipEntity> getOperateModeEquip() {
         return iotOperatingModeMapper.selectOperateModeEquip();
    }

    @Override
    @Cacheable(cacheNames = "com.zcdy.dsc.iot.default",key = "#root.methodName")
    public List<IotOperateModeEntityDefault> getDefaultModeEquip() {
        return iotOperatingModeMapper.selectDefaultModeEquip();
    }


}
