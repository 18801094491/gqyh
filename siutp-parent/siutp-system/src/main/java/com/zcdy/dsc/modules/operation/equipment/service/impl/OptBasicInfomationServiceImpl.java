package com.zcdy.dsc.modules.operation.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.entity.OptBasicInfomation;
import com.zcdy.dsc.modules.operation.equipment.entity.OptBom;
import com.zcdy.dsc.modules.operation.equipment.entity.OptEquipment;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptBasicInfomationMapper;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptBomMapper;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptEquipmentMapper;
import com.zcdy.dsc.modules.operation.equipment.param.OptBaseInfoPageParam;
import com.zcdy.dsc.modules.operation.equipment.service.OptBasicInfomationService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptBaseInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Description: 设备台账基本信息
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-05-28
 * @Version: V1.0
 */
@Service
public class OptBasicInfomationServiceImpl extends ServiceImpl<OptBasicInfomationMapper, OptBasicInfomation> implements OptBasicInfomationService {

    @Resource
    private OptBasicInfomationMapper optBasicInfomationMapper;

    @Resource
    private OptEquipmentMapper optEquipmentMapper;

    @Resource
    private OptBomMapper optBomMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEquipInfo(OptBasicInfomation basicInfomation) {
        //更新设备表
        OptEquipment optEquipment = new OptEquipment();
        optEquipment.setEquipmentMode(basicInfomation.getEquipmentModel());
        optEquipment.setEquipmentSpecs(basicInfomation.getEquipmentSpecs());
        optEquipment.setEquipmentType(basicInfomation.getEquipmentType());
        optEquipment.setEquipmentSupplier(basicInfomation.getEquipmentSupplier());
        LambdaUpdateWrapper<OptEquipment> updateWrapper = new LambdaUpdateWrapper<OptEquipment>().eq(OptEquipment::getBasicId, basicInfomation.getId());
        optEquipmentMapper.update(optEquipment, updateWrapper);
        //更新基础表
        optBasicInfomationMapper.updateById(basicInfomation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBaseInfo(String id) {
        //删除基础信息表
        optBasicInfomationMapper.deleteById(id);
        //删除设备资产表
        OptEquipment optEquipment = new OptEquipment();
        LambdaUpdateWrapper<OptEquipment> updateWrapper = new LambdaUpdateWrapper<OptEquipment>().eq(OptEquipment::getBasicId, id);
        optEquipment.setDelFlag(DelFlagConstant.DELETED);
        optEquipmentMapper.update(optEquipment, updateWrapper);
        //删除bom清单
        optBomMapper.delete(new LambdaQueryWrapper<OptBom>().eq(OptBom::getBasicId,id));
    }

    @Override
    public IPage<OptBaseInfoVo> queryPageData(Page<OptBaseInfoVo> page, OptBaseInfoPageParam param) {
        return optBasicInfomationMapper.selectPageData(page, param);
    }

}
