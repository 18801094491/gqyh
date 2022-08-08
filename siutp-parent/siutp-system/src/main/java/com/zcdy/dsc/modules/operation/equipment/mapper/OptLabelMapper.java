package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.equipment.entity.OptLabel;
import com.zcdy.dsc.modules.operation.equipment.vo.OptLabelTreeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Description: 区域配置
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-04-20
 * @Version: V1.0
 */
public interface OptLabelMapper extends BaseMapper<OptLabel> {
    List<OptLabelTreeVo> getTreeByParentId(@Param("id") String id);

    List<OptLabelTreeVo> getTreeById(@Param("id") String id);

    /**
     * 根据模糊关键字查询树形结构
     *
     * @param keyword
     * @return
     */
    List<OptLabelTreeVo> getTreeByKeyword(@Param("keyword") String keyword);
}
