package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.equipment.entity.KnowlegeAttach;

import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;

/**
 * 描述: 附件信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
public interface KnowlegeAttachMapper extends BaseMapper<KnowlegeAttach> {
    //根据条目查 手册
    LinkedList<KnowlegeAttach> getlistInfo(@Param("itemId") String itemId);
    //添加附近信息
    int addKnowlegeAttachInfo(LinkedList<KnowlegeAttach> knowlegeAtList);

    int editKnowlegeAttachInfo(LinkedList<KnowlegeAttach> knowlegeAtList);
    //删除附件表
    int deleteByItemId(@Param("itemId")String id);
}
