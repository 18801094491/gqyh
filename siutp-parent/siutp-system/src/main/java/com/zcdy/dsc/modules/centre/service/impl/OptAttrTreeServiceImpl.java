package com.zcdy.dsc.modules.centre.service.impl;

import com.zcdy.dsc.modules.centre.entity.OptAttrTree;
import com.zcdy.dsc.modules.centre.mapper.OptAttrTreeMapper;
import com.zcdy.dsc.modules.centre.service.OptAttrTreeService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 属性与树形结构关系
 * @Author: 在信汇通
 * @Date:   2021-03-10
 * @Version: V1.0
 */
@Service
public class OptAttrTreeServiceImpl extends ServiceImpl<OptAttrTreeMapper, OptAttrTree> implements OptAttrTreeService {

    @Override
    public OptAttrTree getAttrByTreeId(String treeId) {
        return baseMapper.getAttrByTreeId(treeId);
    }
}
