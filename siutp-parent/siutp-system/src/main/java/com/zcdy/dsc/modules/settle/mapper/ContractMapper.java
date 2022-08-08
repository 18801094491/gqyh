package com.zcdy.dsc.modules.settle.mapper;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.settle.entity.Contract;

/**
 * 描述: 合同管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-13
 * 版本号: V1.0
 */
public interface ContractMapper extends BaseMapper<Contract> {

    /**
     * 处于告警的合同数量
     * @author songguang.jiao
     * @date 2020/05/29 14:57:32
     * @param remindDay
     * @return
     */
    @Select({
        "SELECT COUNT(id) FROM settle_contract t where DATEDIFF(t.end_date,t.start_date)<#{remindDay} and t.end_date>=CURDATE()"
    })
    int countAlarmContracts(String remindDay);

}
