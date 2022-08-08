package com.zcdy.dsc.modules.operation.work.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobTemplate;
import com.zcdy.dsc.modules.operation.work.param.WorkJobTemplatePageParam;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobTemplateVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkTplDropdown;

import java.util.List;

/**
 * @description: 工单模板
 * @author: 智能无人硬核心项目组
 * @date:   2020-06-24
 * @version: V1.0
 */
public interface WorkJobTemplateService extends IService<WorkJobTemplate> {

    /**
     * 分页查询工单模板
     * @param page
     * @param param
     * @return
     */
    IPage<WorkJobTemplateVo> queryPageData(Page<WorkJobTemplateVo> page, WorkJobTemplatePageParam param);

    /**
     * 下拉选
     * @return
     */
    List<WorkTplDropdown> dropdown();

}
