package com.zcdy.dsc.modules.centre.service.impl;

import com.zcdy.dsc.modules.centre.entity.OptObjTree;
import com.zcdy.dsc.modules.centre.mapper.OptObjTreeMapper;
import com.zcdy.dsc.modules.centre.service.OptObjTreeService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 对象与树的绑定关系
 * @Author: 在信汇通
 * @Date:   2021-03-11
 * @Version: V1.0
 */
@Service
public class OptObjTreeServiceImpl extends ServiceImpl<OptObjTreeMapper, OptObjTree> implements OptObjTreeService {

    @Override
    @Transactional
    public int treeObjBind(OptObjTree optObjTree) {
        deleteTreeObjBind(optObjTree);//先删除旧的绑定关系
        return baseMapper.treeObjBind(optObjTree);
    }

    @Override
    public int deleteTreeObjBind(OptObjTree optObjTree) {
        return baseMapper.deleteTreeObjBind(optObjTree);
    }
}
