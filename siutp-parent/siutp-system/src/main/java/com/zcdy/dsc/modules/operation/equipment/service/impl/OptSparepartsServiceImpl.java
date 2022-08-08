package com.zcdy.dsc.modules.operation.equipment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.entity.OptSpareparts;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptSparepartsMapper;
import com.zcdy.dsc.modules.operation.equipment.param.OptSparepartsPageParam;
import com.zcdy.dsc.modules.operation.equipment.service.OptSparepartsService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptSparepartsVo;
import com.zcdy.dsc.modules.operation.equipment.vo.SparepartsDropdown;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 备品备件基本信息
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Service
public class OptSparepartsServiceImpl extends ServiceImpl<OptSparepartsMapper, OptSpareparts> implements OptSparepartsService {

    @Resource
    private OptSparepartsMapper optSparepartsMapper;

    @Override
    public IPage<OptSparepartsVo> queryPageData(Page<OptSparepartsVo> page, OptSparepartsPageParam param) {
        return optSparepartsMapper.selectPageData(page,param);
    }

    @Override
    public List<SparepartsDropdown> querySparepartsList(String name) {
        return optSparepartsMapper.selectSparepartsList(name);
    }
}
