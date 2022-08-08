package com.zcdy.dsc.modules.operation.equipment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.entity.OptBom;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.equipment.param.OptBomPageParam;
import com.zcdy.dsc.modules.operation.equipment.vo.OptBomVo;

/**
 * @Description: bom清单
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-06-02
 * @Version: V1.0
 */
public interface OptBomService extends IService<OptBom> {

    /**
     * 分页查询bom清单
     * @param page
     * @param param
     * @return
     */
    IPage<OptBomVo> queryPageData(Page<OptBomVo> page, OptBomPageParam param);
}
