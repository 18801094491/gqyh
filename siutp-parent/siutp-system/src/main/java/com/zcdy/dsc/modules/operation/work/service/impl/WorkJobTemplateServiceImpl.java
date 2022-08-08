package com.zcdy.dsc.modules.operation.work.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobTemplate;
import com.zcdy.dsc.modules.operation.work.mapper.WorkJobTemplateMapper;
import com.zcdy.dsc.modules.operation.work.param.WorkJobTemplatePageParam;
import com.zcdy.dsc.modules.operation.work.service.WorkJobTemplateService;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobTemplateVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkTplDropdown;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 工单模板
 * @author: 智能无人硬核心项目组
 * @date: 2020-06-24
 * @version: V1.0
 */
@Service
public class WorkJobTemplateServiceImpl extends ServiceImpl<WorkJobTemplateMapper, WorkJobTemplate> implements WorkJobTemplateService {

    @Resource
    private WorkJobTemplateMapper workJobTemplateMapper;


    @Override
    public IPage<WorkJobTemplateVo> queryPageData(Page<WorkJobTemplateVo> page, WorkJobTemplatePageParam param) {
        return workJobTemplateMapper.selectPageData(page,param);
    }

    @Override
    public List<WorkTplDropdown> dropdown() {
        return workJobTemplateMapper.dropdown();
    }


}
