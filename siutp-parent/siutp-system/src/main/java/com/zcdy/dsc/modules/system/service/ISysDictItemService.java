package com.zcdy.dsc.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.system.entity.SysDictItem;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *@author : songguang.jiao
 
 * @since 2018-12-28
 */
public interface ISysDictItemService extends IService<SysDictItem> {
    /**
     * 查询子项
     * @param mainId 父id
     * @return
     */
    public List<SysDictItem> selectItemsByMainId(String mainId);
}
