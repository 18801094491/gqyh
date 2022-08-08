package com.zcdy.dsc.modules.objecttype.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.centre.vo.CentreTreeObjectVo;
import com.zcdy.dsc.modules.objecttype.entity.SysObject;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 对象类型
 * @Author: 在信汇通
 * @Date:   2021-03-05
 * @Version: V1.0
 */
public interface SysObjectMapper extends BaseMapper<SysObject> {

    CentreTreeObjectVo getSysObjectByTreeId(String treeId);

    @Update("update sys_object " +
            "set name = #{name}, " +
                "update_by = #{updateBy}, " +
                "update_time = #{updateTime} " +
            "where id = #{id}")
    int updateNameById(SysObject sysObject);
}
