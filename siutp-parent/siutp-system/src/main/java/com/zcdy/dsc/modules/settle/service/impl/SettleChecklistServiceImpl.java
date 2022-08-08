package com.zcdy.dsc.modules.settle.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.settle.entity.SettleChecklist;
import com.zcdy.dsc.modules.settle.entity.SettleChecklistPage;
import com.zcdy.dsc.modules.settle.mapper.SettleChecklistMapper;
import com.zcdy.dsc.modules.settle.service.SettleChecklistService;
import com.zcdy.dsc.modules.settle.vo.SettleCheckQueryParam;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsVo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @description 结算清单
 * @author :  tangchao
 * @date   2020-05-11
 * @version V1.0
 */
@Service
public class SettleChecklistServiceImpl extends ServiceImpl<SettleChecklistMapper, SettleChecklist> implements SettleChecklistService {
    @Override
    public IPage<SettleChecklistPage> pageChecklist(Page<SettleChecklistPage> page, SettleCheckQueryParam settleCheckQueryParam) {
        return this.baseMapper.pageChecklist(page, settleCheckQueryParam);
    }

    @Override
    public List<SettlementStatisticsVo> getCheckListDetail(String id) {

        return this.baseMapper.getCheckListDetail(id);
    }
}
