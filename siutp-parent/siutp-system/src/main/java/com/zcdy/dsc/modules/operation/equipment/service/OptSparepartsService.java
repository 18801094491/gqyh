package com.zcdy.dsc.modules.operation.equipment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.equipment.entity.OptSpareparts;
import com.zcdy.dsc.modules.operation.equipment.param.OptSparepartsPageParam;
import com.zcdy.dsc.modules.operation.equipment.vo.OptSparepartsVo;
import com.zcdy.dsc.modules.operation.equipment.vo.SparepartsDropdown;

import java.util.List;

/**
 * @Description: 备品备件基本信息
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-06-02
 * @Version: V1.0
 */
public interface OptSparepartsService extends IService<OptSpareparts> {

    /**
     * 分页查询备品备件
     * @param page
     * @param param
     * @return
     */
    IPage<OptSparepartsVo> queryPageData(Page<OptSparepartsVo> page, OptSparepartsPageParam param);

    /**
     * 备品备件下拉选
     * @param name
     * @return
     */
    List<SparepartsDropdown> querySparepartsList(String name);
}
