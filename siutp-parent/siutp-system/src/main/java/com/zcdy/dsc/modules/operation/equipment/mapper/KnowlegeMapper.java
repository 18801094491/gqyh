package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.entity.Knowlege;
import com.zcdy.dsc.modules.operation.equipment.param.KnowlegePageParam;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeDataVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述: 知识库管理
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
public interface KnowlegeMapper extends BaseMapper<Knowlege> {

    /**
     * 分页查询列表
     *
     * @return
     */
    IPage<KnowlegeDataVo> getListInfo(Page<KnowlegeDataVo> page, @Param("param") KnowlegePageParam param);

    /**
     * 列表
     *
     * @return
     */
    List<KnowlegeDataVo> getListInfo(@Param("param") KnowlegePageParam param);

}
