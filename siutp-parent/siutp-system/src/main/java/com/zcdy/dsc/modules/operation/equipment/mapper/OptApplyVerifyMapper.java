package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.equipment.entity.OptApplyVerify;
import com.zcdy.dsc.modules.operation.equipment.vo.OptApplyVerifyVo;

import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;

/**
 * 描述: 申请审核历史
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-05
 * 版本号: V1.0
 */
public interface OptApplyVerifyMapper extends BaseMapper<OptApplyVerify> {
    //审核历史弹窗
    LinkedList<OptApplyVerifyVo> selectByApplyId(@Param("id") String id);
}
