package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.entity.OptSpareparts;
import com.zcdy.dsc.modules.operation.equipment.param.OptSparepartsPageParam;
import com.zcdy.dsc.modules.operation.equipment.vo.OptSparepartsVo;
import com.zcdy.dsc.modules.operation.equipment.vo.SparepartsDropdown;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 备品备件基本信息
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-06-02
 * @Version: V1.0
 */
public interface OptSparepartsMapper extends BaseMapper<OptSpareparts> {

    /**
     * 分页查询
     * @param page
     * @param param
     * @return
     */
    IPage<OptSparepartsVo> selectPageData(Page<OptSparepartsVo> page,@Param("param") OptSparepartsPageParam param);

    /**
     * 下拉选列表
     * @param name
     * @return
     */
    List<SparepartsDropdown> selectSparepartsList(@Param("name") String name);
}
