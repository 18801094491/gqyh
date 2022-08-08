package com.zcdy.dsc.modules.operation.alarm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.alarm.entity.IotEquipmentRuleWarn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

/**
 * 描述: 告警信息配置
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-02
 * 版本号: V1.0
 */
public interface IotEquipmentRuleWarnMapper extends BaseMapper<IotEquipmentRuleWarn> {
    @Delete(" delete from iot_equipment_rule_warn")
    void deleteIotEquipmentRuleWarn();

    @Select("select count(1)," +
            " (select  group_concat(warn_level_code order by warn_level_code separator ',' )  from iot_equipment_rule_warn" +
            " where type='0' ) as warnLevelCode," +
            " (select  group_concat(warn_level_code order by warn_level_code separator ',' )  from iot_equipment_rule_warn" +
            " where type='1' ) as type" +
            " from iot_equipment_rule_warn")
    IPage<IotEquipmentRuleWarn> queryPageList(Page<IotEquipmentRuleWarn> page);
}
