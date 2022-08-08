package com.zcdy.dsc.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.system.entity.SysRole;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *@author : songguang.jiao
 
 * @since 2018-12-19
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 启停用
     * @param id id
     * @param status 状态
     */
    @Update("update sys_role set status=#{status} where id=#{id}")
    void startOrStop(String id, Boolean status);
}
