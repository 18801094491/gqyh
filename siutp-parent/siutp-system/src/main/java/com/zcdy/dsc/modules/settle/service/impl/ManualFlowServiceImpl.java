package com.zcdy.dsc.modules.settle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.exception.BusinessException;
import com.zcdy.dsc.constant.ScaleConstant;
import com.zcdy.dsc.modules.settle.constant.SettleConstant;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRuleItem;
import com.zcdy.dsc.modules.settle.entity.SettleBatch;
import com.zcdy.dsc.modules.settle.mapper.ManualFlowMapper;
import com.zcdy.dsc.modules.settle.service.ContractWaterRuleItemService;
import com.zcdy.dsc.modules.settle.service.ManualFlowService;
import com.zcdy.dsc.modules.settle.service.SettleBatchService;
import com.zcdy.dsc.modules.settle.vo.ManualContractInfoVo;
import com.zcdy.dsc.modules.settle.vo.ManualFlowFormVo;
import com.zcdy.dsc.modules.settle.vo.SettleBatchOptEquipInfoVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * @author tangchao
 * @date 2020/5/12
 */
@Service
public class ManualFlowServiceImpl extends ServiceImpl<ManualFlowMapper, ManualFlowFormVo> implements ManualFlowService {
    @Resource
    private SettleBatchService settleBatchService;

    @Resource
    private ContractWaterRuleItemService contractWaterRuleItemService;
    /**
     * 新增
     *
     * @param manualFlowFormVo 表单
     */
    @Override
    public void add(ManualFlowFormVo manualFlowFormVo) {
        QueryWrapper<SettleBatch> query = new QueryWrapper<>();
        query.lambda().eq(SettleBatch::getEquipmentId, manualFlowFormVo.getEquipmentId())
                .eq(SettleBatch::getCurrentFlowDate, manualFlowFormVo.getCurrentFlowTime());
        IPage<SettleBatch> page = this.settleBatchService.page(new Page(1, 1), query);
        if(page.getRecords().size() > 0){
            SettleBatch s = page.getRecords().get(0);
            manualFlowFormVo.setId(String.valueOf(s.getId()));
            edit(manualFlowFormVo);
            return;
        }
        //yyyy-MM-dd
        SettleBatch settleBatch = new SettleBatch();
        settleBatch.setEquipmentId(manualFlowFormVo.getEquipmentId());
        computeSettleData(manualFlowFormVo, settleBatch);
        this.settleBatchService.save(settleBatch);
    }

    @Override
    public void edit(ManualFlowFormVo manualFlowFormVo) {
        String id = manualFlowFormVo.getId();
        if (StringUtils.isBlank(id)) {
            throw new BusinessException("id 不能为空, 请联系管理员.");
        }
        SettleBatch curSettleBatch = this.settleBatchService.getById(id);
        UpdateWrapper<SettleBatch> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(SettleBatch::getId, id);
        SettleBatch settleBatch = new SettleBatch(curSettleBatch.getStatus());
        manualFlowFormVo.setEquipmentId(curSettleBatch.getEquipmentId());
        computeSettleData(manualFlowFormVo, settleBatch);
        if(SettleConstant.STATUS_INIT_SETTLEMENT.equals(curSettleBatch.getStatus())){
            settleBatch.setUsedFlow("0");
            settleBatch.setTotalCost(BigDecimal.ZERO);
        }
        this.settleBatchService.update(settleBatch, updateWrapper);
    }

    private void computeSettleData(ManualFlowFormVo manualFlowFormVo, SettleBatch settleBatch) {
        Date currentFlowDateObj = manualFlowFormVo.getCurrentFlowDateTimeObj();
        String ruleItemId = manualFlowFormVo.getRuleItemId();
        SettleBatchOptEquipInfoVo optEquipInfo = this.baseMapper.getByRuleItemId(ruleItemId, manualFlowFormVo.getEquipmentId());
        BeanUtils.copyProperties(optEquipInfo, settleBatch);
        settleBatch.setCurrentFlow(manualFlowFormVo.getCurrentFlow());
        settleBatch.setCurrentPositiveFlow(manualFlowFormVo.getCurrentFlow());
        settleBatch.setCurrentNavigatFlow(BigDecimal.ZERO.setScale(ScaleConstant.LL_SCALE_STORE, RoundingMode.HALF_UP).toEngineeringString());
        settleBatch.setCurrentFlowDate(currentFlowDateObj);
        settleBatch.setCurrentFlowTime(currentFlowDateObj);
        ContractWaterRuleItem ruleItem = this.contractWaterRuleItemService.getById(ruleItemId);
        BigDecimal unitPrice = new BigDecimal(ruleItem.getPrice()).setScale(ScaleConstant.YL_SCALE, RoundingMode.HALF_UP);
        settleBatch.setUnitPrice(unitPrice);
    }


    @Override
    public List<ManualContractInfoVo> listContractByOptDate(String equipmentId, String date){
        return this.baseMapper.listContractByOptDate(equipmentId, date);
    }


}
