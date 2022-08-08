package com.zcdy.dsc.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.personal.vo.DepartUserVO;
import com.zcdy.dsc.modules.personal.vo.DepartmentVO;
import com.zcdy.dsc.modules.system.entity.SysUserDepart;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author : songguang.jiao
 */
public interface SysUserDepartMapper extends BaseMapper<SysUserDepart> {

    /**
     * 通过用户查询 部门列表
     * @param userId 用户id
     * @return
     */
    List<SysUserDepart> getUserDepartByUid(@Param("userId") String userId);

    /**
     * 部门分组数据
     * @param pid 父id
     * @return
     */
    @Select({
        "<script>",
	    " SELECT ",
        "    t.id, ",
        "    t.parent_id pid, ",
        "    t.depart_name departName, ",
        "    t.org_code orgCode, ",
        "    (SELECT COUNT(1) FROM sys_user_depart sud LEFT JOIN sys_user su ON sud.user_id = su.id " +
        "    WHERE su.del_flag = 0 AND sud.dep_id = t.id) usercount ",
        " FROM ",
        "    sys_depart t ",
        " where 1=1 ",
        " <if test='null==pid or \"\"==pid'> ",
        "   and (t.parent_id is null or t.parent_id='') ",
        " </if>",
        " <if test='null!=pid and \"\"!=pid'> ",
        "   and t.parent_id = #{pid} ",
        " </if>",
        " GROUP BY t.depart_name ",
        " ORDER BY t.depart_order, t.parent_id ",
        "</script>"
	})
    List<DepartmentVO> selectDepartDataByGroup(@Param("pid") String pid);

    /**
     * 查询部门组织结构
     * @param orgCode 部门code
     * @param realname 用户名称
     * @return
     */
    @Select({
        "<script>",
        "SELECT t.realname,t.phone,t.telephone,t.email, t.work_no workNo,b.depart_name orgName,c.`name` post from sys_user t ",
        "   LEFT JOIN sys_user_depart d on d.user_id = t.id",
        "   LEFT JOIN sys_depart b on d.dep_id = b.id",
        "   LEFT JOIN sys_position c on t.post = c.`code`",
        "where 1=1 ",
        "<if test='realname!=null and realname!=\"\"'>",
        " and (t.realname like concat('%',#{realname},'%')  or t.work_no = #{realname} )",
        "</if>",
        "<if test='orgCode!=null and orgCode!=\"\"'>",
        " and b.org_code = #{orgCode} ",
        "</if>",
        "</script>"
    })
    List<DepartUserVO> selectDepartUsers(@Param(value = "orgCode") String orgCode, @Param(value = "realname") String realname);
}
