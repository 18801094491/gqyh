package com.zcdy.dsc.modules.settle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.modules.settle.entity.SettleChecklist;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.settle.entity.SettleChecklistPage;
import com.zcdy.dsc.modules.settle.vo.SettleCheckQueryParam;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsVo;

import java.util.List;

/**
 * @Description: 结算清单
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-05-11
 * @Version: V1.0
 */
public interface SettleChecklistService extends IService<SettleChecklist> {
    /**
     * 查询清单分页
     * @return 分页结果
     */
    IPage<SettleChecklistPage> pageChecklist(Page<SettleChecklistPage> page, SettleCheckQueryParam settleCheckQueryParam);

    /**
     * 获取结算单明细
     * @param id 结算单id
     * @return 结算单明细
     */
    List<SettlementStatisticsVo> getCheckListDetail(String id);
}
