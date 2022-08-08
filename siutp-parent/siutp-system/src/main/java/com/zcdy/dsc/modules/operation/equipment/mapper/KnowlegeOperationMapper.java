package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.equipment.entity.KnowlegeOperation;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeOperationVo;

import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;

/**
 * 描述: 知识库条目操作章程
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
public interface KnowlegeOperationMapper extends BaseMapper<KnowlegeOperation> {

    //根据条目查 操作规程
    LinkedList<KnowlegeOperationVo> getlistInfo(String id);
    //保存操作规程
    int addKnowlegeOperationInfo(LinkedList<KnowlegeOperation> knowlegeOpList);
    //删除操作章程
    int deleteByItemId(@Param("ItemId") String id);


    int editKnowlegeOperationInfo(LinkedList<KnowlegeOperation> knowlegeOpList);


}
