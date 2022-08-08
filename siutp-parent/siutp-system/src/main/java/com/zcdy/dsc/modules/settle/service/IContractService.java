package com.zcdy.dsc.modules.settle.service;

import com.zcdy.dsc.modules.settle.entity.Contract;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 描述: 合同管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-13
 * 版本号: V1.0
 */
public interface IContractService extends IService<Contract> {
    
    /**
     * 处于告警的合同数量
     * @author songguang.jiao
     * @date 2020/05/29 14:55:42
     * @param remindDay
     * @return
     */
    int countAlarmContracts(String remindDay);

}
