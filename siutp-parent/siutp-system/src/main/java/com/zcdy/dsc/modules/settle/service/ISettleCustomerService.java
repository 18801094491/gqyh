package com.zcdy.dsc.modules.settle.service;

import com.zcdy.dsc.modules.settle.entity.SettleCustomer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.settle.vo.TreeVo;

import java.util.List;

/**
 * 描述: 客户信息管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-02
 * 版本号: V1.0
 */
public interface ISettleCustomerService extends IService<SettleCustomer> {
    /**
     * 查询所有客户树形 禅道#292
     * 2020-4-26 09:58:00
     * @author tangchao
     * @return 所有客户
     */
    List<TreeVo> queryCustomerTreeList(String keyword);
}
