package com.zcdy.dsc.modules.operation.equipment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.operation.equipment.entity.OptBom;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptBomMapper;
import com.zcdy.dsc.modules.operation.equipment.param.OptBomPageParam;
import com.zcdy.dsc.modules.operation.equipment.service.OptBomService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptBomVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: bom清单
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Service
public class OptBomServiceImpl extends ServiceImpl<OptBomMapper, OptBom> implements OptBomService {

    @Resource
    private OptBomMapper optBomMapper;

    @Override
    public IPage<OptBomVo> queryPageData(Page<OptBomVo> page, OptBomPageParam param) {
        return optBomMapper.selectPageData(page,param);
    }
}
