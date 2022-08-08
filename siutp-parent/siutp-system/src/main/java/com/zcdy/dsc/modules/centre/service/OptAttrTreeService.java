package com.zcdy.dsc.modules.centre.service;

import com.zcdy.dsc.modules.centre.entity.OptAttrTree;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 属性与树形结构关系
 * @Author: 在信汇通
 * @Date:   2021-03-10
 * @Version: V1.0
 */
public interface OptAttrTreeService extends IService<OptAttrTree> {
    /**
     * 获取绑定在某节点上的属性对象
     * @param treeId
     * @return
     */
    OptAttrTree getAttrByTreeId(String treeId);
}
