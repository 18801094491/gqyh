package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.equipment.entity.KnowlegeCaution;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeCautionVo;

import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;

/**
 * 描述: 知识库条目操作注意事项
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
public interface KnowlegeCautionMapper extends BaseMapper<KnowlegeCaution> {
    //根据条目查 安全事项
    LinkedList<KnowlegeCautionVo> getlistInfo(@Param("knowlegeItemId") String knowlegeItemId);
    //保存注意事项
    int addKnowlegeCautionInfo(LinkedList<KnowlegeCaution> knowlegeCaList);

    int editKnowlegeCautionInfo(LinkedList<KnowlegeCaution> knowlegeCaList);
    //删除知识库注意事项
    int deleteByItemId(@Param("ItemId")String id);
}
