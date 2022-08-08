package com.zcdy.dsc.modules.operation.equipment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.collection.gis.vo.GisLocationVo;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.entity.OptEquipment;
import com.zcdy.dsc.modules.operation.equipment.entity.OptPersonResponsible;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptEquipmentMapper;
import com.zcdy.dsc.modules.operation.equipment.param.OptEquipmentAddParam;
import com.zcdy.dsc.modules.operation.equipment.service.IOptEquipmentService;
import com.zcdy.dsc.modules.operation.equipment.service.OptPersonResponsibleService;
import com.zcdy.dsc.modules.operation.equipment.vo.*;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeEquipData;
import com.zcdy.dsc.modules.system.vo.DictDropdown;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: 设备资产
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-17
 * 版本号: V1.0
 */
@Service
public class OptEquipmentServiceImpl extends ServiceImpl<OptEquipmentMapper, OptEquipment> implements IOptEquipmentService {

    @Resource
    private OptEquipmentMapper optEquipmentMapper;

    @Resource
    private OptPersonResponsibleService optPersonResponsibleService;

    /**
     * 项目访问基础路径
     */
    @Value("${com.zcdy.dsc.file.request.domain}")
    protected String baseStoragePath;


    @Override
    public OptEquipmentModel getDetail(String id) {
        return optEquipmentMapper.getDetail(id);
    }

    @Override
    public IPage<GisVo> getGisVoList(IPage<GisVo> page, String name) {
        return optEquipmentMapper.getGisVoList(page, name);
    }

    @Override
    public IPage<OptEquipmentModel> getList(IPage<OptEquipmentModel> page, String equipmentSupplier, String equipmentSn,
                                            String equipmentType, String equipmentRevstop, String equipmentCategory) {
        return optEquipmentMapper.getList(page, equipmentSupplier, equipmentSn, equipmentType, equipmentRevstop, equipmentCategory);
    }

    @Override
    public GisLocationVo getLocation(String id) {
        return optEquipmentMapper.getLocation(id);
    }

    @Override
    public List<KnowlegeEquipData> queryKnowlegeData(String equipmentTypeCode, String equipmentMode,
                                                     String equipmentSpecs) {
        return optEquipmentMapper.queryKnowlegeData(equipmentTypeCode, equipmentMode, equipmentSpecs);
    }

    @Override
    public Boolean checkEquipSnExist(String id, String sn) {
        return optEquipmentMapper.checkEquipSnExist(id, sn);
    }

    @Override
    public Boolean checkHkMonitorCode(String id, String monitorCode) {
        return optEquipmentMapper.checkHkMonitorCode(id, monitorCode);
    }

    @Override
    public List<OptEquipmentModel> exportData(String equipmentSupplier, String equipmentSn, String equipmentType,
                                              String equipmentRevstop) {
        return optEquipmentMapper.exportData(equipmentSupplier, equipmentSn, equipmentType, equipmentRevstop);
    }

    @Override
    public Map<String, String> getCategoryData(String sysCode) {
        List<SysCateDropdown> categoryData = optEquipmentMapper.getCategoryData(sysCode);
        Map<String, String> map = new HashMap<String, String>(categoryData.size());
        categoryData.forEach(item -> {
            map.put(item.getTitle(), item.getCode());
        });
        return map;
    }

    @Override
    public Map<String, String> getSupplierData() {
        List<SupplierListVo> supplierData = optEquipmentMapper.getSupplierData();
        Map<String, String> map = new HashMap<String, String>(supplierData.size());
        supplierData.forEach(item -> {
            map.put(item.getSupplierName(), item.getId());
        });
        return map;
    }

    @Override
    public Map<String, String> getDictyData(String dictCode) {
        List<DictDropdown> dictyData = optEquipmentMapper.getDictyData(dictCode);
        Map<String, String> map = new HashMap<String, String>(dictyData.size());
        dictyData.forEach(item -> {
            map.put(item.getTitle(), item.getCode());
        });
        return map;
    }

    /*
     * @see com.zcdy.dsc.modules.operation.equipment.service.IOptEquipmentService#getDetailById(java.lang.String)
     */
    @Override
    public OptEquipment getDetailById(String equipmentId) {
        return this.optEquipmentMapper.selectDetailById(equipmentId);
    }

    /*
     * @see com.zcdy.dsc.modules.operation.equipment.service.IOptEquipmentService#getPageListByParam(com.baomidou.mybatisplus.extension.plugins.pagination.Page, com.zcdy.dsc.modules.operation.equipment.vo.EquipmentQueryVO)
     */
    @Override
    public IPage<OptEquipmentModel> getPageListByParam(Page<OptEquipmentModel> page, EquipmentQueryVO param) {
        return optEquipmentMapper.selectPageListByParam(page, param);
    }

    @Override
    public IPage<OptEquipmentModel> getMonitorEquipList(Page<OptEquipmentModel> page, EquipmentQueryVO param) {
        return optEquipmentMapper.getMonitorEquipList(page, param);
    }

    @Override
    public List<OptEquipmentModel> selectPageListByParam(@Param("param") EquipmentQueryVO param) {
        return optEquipmentMapper.selectPageListByParam(param);
    }

    @Override
    public List<EquipmentDropdown> equipDropdown(String equipmentSn) {
        return optEquipmentMapper.equipDropdown(equipmentSn);
    }

    @Override
    public List<EquipmentDropdown> unDispatchEquipmentDropDown(String equipmentSn) {
        return optEquipmentMapper.unDispatchEquipmentDropDown(equipmentSn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveOptEquipment(OptEquipmentAddParam param) {
        OptEquipment optEquipment = new OptEquipment();
        BeanUtil.copyProperties(param, optEquipment);
        //图片路径特殊处理
        String equipmentImg = "".equals(param.getEquipmentImg()) ? "" : param.getEquipmentImg().replace(baseStoragePath, "");
        optEquipment.setEquipmentImg(equipmentImg);
        //判断新增或者是修改
        if (StringUtils.isEmpty(param.getId())) {
            optEquipment.setBasicId(param.getBaseId());
            optEquipment.setDelFlag(DelFlagConstant.NORMAL);
            optEquipmentMapper.insert(optEquipment);
        } else {
            optEquipmentMapper.updateById(optEquipment);
        }
        if (StringUtils.isNotEmpty(param.getPersonResponsible())) {
            optPersonResponsibleService.remove(Wrappers.<OptPersonResponsible>lambdaQuery().eq(OptPersonResponsible::getEquipmentId, optEquipment.getId()));
            String[] usersId = param.getPersonResponsible().split(",");
            List<OptPersonResponsible> personResponsibleList = new ArrayList<>();
            for (String userId : usersId) {
                OptPersonResponsible personResponsible = new OptPersonResponsible();
                personResponsible.setEquipmentId(optEquipment.getId());
                personResponsible.setUserId(userId);
                personResponsibleList.add(personResponsible);
            }
            optPersonResponsibleService.saveBatch(personResponsibleList);
        }else {
            optPersonResponsibleService.remove(Wrappers.<OptPersonResponsible>lambdaQuery().eq(OptPersonResponsible::getEquipmentId, optEquipment.getId()));
        }

    }

}
