package com.zcdy.dsc.modules.settle.service.impl;

import com.zcdy.dsc.modules.settle.entity.Contract;
import com.zcdy.dsc.modules.settle.mapper.ContractMapper;
import com.zcdy.dsc.modules.settle.service.IContractService;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 描述: 合同管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-13
 * 版本号: V1.0
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {

    @Resource
    private ContractMapper contractMapper;
    
    @Override
    public int countAlarmContracts(String remindDay) {
         return contractMapper.countAlarmContracts(remindDay);
    }

}
