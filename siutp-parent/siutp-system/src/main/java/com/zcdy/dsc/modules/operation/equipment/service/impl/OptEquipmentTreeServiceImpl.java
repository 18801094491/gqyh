package com.zcdy.dsc.modules.operation.equipment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.entity.OptLabel;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptEquipmentTreeMapper;
import com.zcdy.dsc.modules.operation.equipment.service.OptEquipmentTreeService;
import com.zcdy.dsc.modules.operation.equipment.service.OptLabelService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentTreeQueryParam;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentTreeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tangchao
 * @date 2020/5/28
 */
@Service
public class OptEquipmentTreeServiceImpl implements OptEquipmentTreeService {

    @Resource
    private OptEquipmentTreeMapper optEquipmentTreeMapper;

    @Resource
    private OptLabelService optLabelService;

    @Override
    public IPage<OptEquipmentTreeVo> queryEquipmentInfoBylabelId(Page page, OptEquipmentTreeQueryParam param) {
        if(StringUtils.isBlank(param.getLabelId())){
            param.setLabelId("0");
        }
        OptLabel optLabel = this.optLabelService.getById(param.getLabelId());
        if (optLabel == null) {
            throw new IllegalArgumentException("标签不存在!");
        }
        param.setLabelParentIds(concatParentIds(optLabel));
        if("0".equals(optLabel.getId())){
            param.setLabelId(null);
            param.setLabelParentIds(null);
        }
        return this.optEquipmentTreeMapper.pageIndex(page, param);
    }

    private String concatParentIds(OptLabel label) {
        return label.getParentIds() + label.getId() + ",";
    }
}
