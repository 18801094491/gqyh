package com.zcdy.dsc.modules.settle.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.settle.constant.SettleConstant;
import com.zcdy.dsc.modules.settle.entity.ContractWater;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRule;
import com.zcdy.dsc.modules.settle.mapper.ContractWaterMapper;
import com.zcdy.dsc.modules.settle.service.ContractWaterService;
import com.zcdy.dsc.modules.settle.service.SettleBatchService;
import com.zcdy.dsc.modules.settle.vo.ContractWaterVo;
import com.zcdy.dsc.modules.settle.vo.CustomerWaterInfoVo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 合同绑定水表信息
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-22
 * @Version: V1.0
 */
@Service
public class ContractWaterServiceImpl extends ServiceImpl<ContractWaterMapper, ContractWater> implements ContractWaterService {

    @Resource
    private SettleBatchService settleBatchService;

    @Override
    public IPage<ContractWaterVo> selecContractWatertPage(Page<ContractWaterVo> page, String contractId) {
        Map<String,String> param = new HashMap<>(2);
        param.put("contractId",contractId);
        param.put("now", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
        return this.baseMapper.selecContractWatertPage(page, param);
    }



    @Override
    /**
     * 解绑规则
     */
    public void unbind(ContractWater contractWater) {
        UpdateWrapper<ContractWater> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(ContractWater::getId, contractWater.getId());
        ContractWater updateObj = new ContractWater();
        updateObj.setInvalidDate(contractWater.getInvalidDate());
        this.baseMapper.update(updateObj, updateWrapper);
    }

    /**
     * 事务
     * 1. 水表绑定
     * 2. 加入事务, 查找绑定后的水价等信息
     * 保证先绑定后抄表的顺序
     * @author tangchao
     * @param contractWater 合同水表信息
     * @date 2020-5-11
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void bind(ContractWater contractWater) {
        this.save(contractWater);
        //---update-begin-----autor:tangchao------date:2020-5-9-------for:将当前表信息记录--------
        this.settleBatchService.executeEquipmentNow(contractWater.getEquipmentId(), SettleConstant.STATUS_INIT_SETTLEMENT);
        //---update-end-----autor:tangchao------date:2020-5-9-------for:将当前表信息记录----------
    }


    @Override
    public List<CustomerWaterInfoVo> queryCustomerBindedWater(String customerId) {
        Map<String,String> param = new HashMap<>(2);
        param.put("customerId",customerId);
        param.put("now", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
        return this.baseMapper.queryCustomerBindedWater(param);
    }

    @Override
    public List<ContractWaterRule> queryContractRule(String contractId) {
        return this.baseMapper.queryContractRule(contractId);
    }

}
