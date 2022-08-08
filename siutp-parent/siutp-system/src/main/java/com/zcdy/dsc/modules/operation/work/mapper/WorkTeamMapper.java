package com.zcdy.dsc.modules.operation.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.inspection.param.WorkTeamDateParam;
import com.zcdy.dsc.modules.operation.work.entity.WorkTeam;

import java.util.List;

/**
 * 描述: 班组信息表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-10
 * 版本号: V1.0
 */
public interface WorkTeamMapper extends BaseMapper<WorkTeam> {

    List<WorkTeam> getWorkTeamAndMemberListByDate(WorkTeamDateParam workTeamDateParam);
}
