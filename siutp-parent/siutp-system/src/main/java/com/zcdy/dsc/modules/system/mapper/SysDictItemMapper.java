package com.zcdy.dsc.modules.system.mapper;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.system.entity.SysDictItem;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *@author : songguang.jiao
 
 * @since 2018-12-28
 */
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {
    /**
     * 查询子项
     * @param mainId 父id
     * @return
     */
    @Select("SELECT * FROM SYS_DICT_ITEM WHERE DICT_ID = #{mainId}")
    public List<SysDictItem> selectItemsByMainId(String mainId);
}
