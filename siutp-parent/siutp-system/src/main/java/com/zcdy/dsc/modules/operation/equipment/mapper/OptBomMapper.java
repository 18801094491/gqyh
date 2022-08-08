package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.entity.OptBom;
import com.zcdy.dsc.modules.operation.equipment.param.OptBomPageParam;
import com.zcdy.dsc.modules.operation.equipment.vo.OptBomVo;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: bom清单
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-06-02
 * @Version: V1.0
 */
public interface OptBomMapper extends BaseMapper<OptBom> {

    /**
     * 分页查询bom清单
     * @param page
     * @param param
     * @return
     */
    IPage<OptBomVo> selectPageData(Page<OptBomVo> page,@Param("param") OptBomPageParam param);
}
