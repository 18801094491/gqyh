package com.zcdy.dsc.modules.objecttype.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.centre.vo.CentreTreeObjectVo;
import com.zcdy.dsc.modules.objecttype.entity.SysObject;

/**
 * @Description: 对象类型
 * @Author: 在信汇通
 * @Date:   2021-03-05
 * @Version: V1.0
 */
public interface SysObjectService extends IService<SysObject> {

    /**
     *根据节点id，获取能与之绑定的对象信息
     * @param treeId
     * @return
     */
    CentreTreeObjectVo getSysObjectByTreeId(String treeId);

    /**
     * 修改名称
     * @param sysObject
     * @return
     */
    Boolean updateNameById(SysObject sysObject);
}
