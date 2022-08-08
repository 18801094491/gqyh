package com.zcdy.dsc.modules.centre.service;

import com.zcdy.dsc.modules.centre.entity.OptObjTree;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 对象与树的绑定关系
 * @Author: 在信汇通
 * @Date:   2021-03-11
 * @Version: V1.0
 */
public interface OptObjTreeService extends IService<OptObjTree> {
    /**
     * 绑定树与对象的关系
     * @return
     */
    int treeObjBind(OptObjTree optObjTree);

    /**
     * 根据dataId删除绑定关系
     * @param optObjTree
     * @return
     */
    int deleteTreeObjBind(OptObjTree optObjTree);
}
