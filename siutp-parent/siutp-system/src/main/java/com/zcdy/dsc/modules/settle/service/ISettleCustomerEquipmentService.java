package com.zcdy.dsc.modules.settle.service;

import com.zcdy.dsc.modules.settle.entity.SettleCustomerEquipment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 描述: 用户水表关联表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-08
 * 版本号: V1.0
 */
public interface ISettleCustomerEquipmentService extends IService<SettleCustomerEquipment> {
    String BIND_STATUS_BINDED = "00";
    String BIND_STATUS_UNBINDED = "01";
}
