package com.zcdy.dsc.modules.operation.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobTemplate;
import com.zcdy.dsc.modules.operation.work.param.WorkJobTemplatePageParam;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobTemplateVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkTplDropdown;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description: 工单模板
 * @author: 智能无人硬核心项目组
 * @date: 2020-06-24
 * @version: V1.0
 */
public interface WorkJobTemplateMapper extends BaseMapper<WorkJobTemplate> {

    /**
     * 分页查询工单模板
     *
     * @param page
     * @param param
     * @return
     */
    IPage<WorkJobTemplateVo> selectPageData(Page<WorkJobTemplateVo> page, WorkJobTemplatePageParam param);

    /**
     * 模板下拉选
     *
     * @return
     */
    @Select("SELECT t.id,t.tpl_name tplName FROM work_job_template t")
    List<WorkTplDropdown> dropdown();
}
