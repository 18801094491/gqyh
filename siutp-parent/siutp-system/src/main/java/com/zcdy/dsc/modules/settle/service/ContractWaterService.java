package com.zcdy.dsc.modules.settle.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.settle.entity.ContractWater;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRule;
import com.zcdy.dsc.modules.settle.vo.ContractWaterVo;
import com.zcdy.dsc.modules.settle.vo.CustomerWaterInfoVo;

/**
 * @Description: 合同绑定水表信息
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-22
 * @Version: V1.0
 */
public interface ContractWaterService extends IService<ContractWater> {


    /**
     * 查询当前合同绑定的水表
     * @param page 分页参数
     * @param contractId 合同
     * @return 水表
     */
    IPage<ContractWaterVo> selecContractWatertPage(Page<ContractWaterVo> page, String contractId);

    /**
     * 水表解绑合同
     * @param contractWater 合同信息
     */
    void unbind(ContractWater contractWater);

    /**
     * 合同绑定水表
     * @param contractWater 合同水表信息
     */
    void bind(ContractWater contractWater);

    /**
     * 查询客户绑定中的水表并且还没有在合同中绑定的水表
     * @param customerId
     * @return
     */
    List<CustomerWaterInfoVo> queryCustomerBindedWater(String customerId);

    /**
     * 查询合同建立的所有水价规则
     * @param contractId
     * @return
     */
    List<ContractWaterRule> queryContractRule(String contractId);

}
