package com.zcdy.dsc.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.system.entity.SysPosition;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author : songguang.jiao
 * 描述: 职务表
 */
public interface SysPositionMapper extends BaseMapper<SysPosition> {

    /**
     * 导出列表
     * @param code code
     * @param name 名称
     * @return
     */
    @Select({
    		" <script>",
    		"select  ",
            "sp.code code,",
            "sp.name name,",
            "vsdiPr.item_text rank,",
            "sd.depart_name companyId",
            " from sys_position sp",
            " left join v_sys_dict_item vsdiPr on vsdiPr.item_value=sp.rank and vsdiPr.dict_code='position_rank'",
            " left join sys_depart sd on sd.org_code=sp.sys_org_code ",
            " where 1=1 ",
            " <if test='code!=null and code!=\"\"'>",
            " and sp.code=#{code}",
            " </if>",
            " <if test='name!=null and name!=\"\"'>",
            " and sp.name like concat('%',#{name},'%')",
            " </if>",
            " </script>",
    })
    List<SysPosition> exportList(@Param("code") String  code, @Param("name") String  name);

    /**
     * 分页查询
     * @param page 分页参数
     * @param code code
     * @param name 名称
     * @param status 状态
     * @return
     */
    @Select({
            " <script>",
            " select  sp.id id,",
                    "sp.sys_org_code sysOrgCode,",
                    "sp.code code,",
                    "sp.name name,",
                    "vsdiPr.item_text rank_dictText,",
                    "sp.rank rank,",
                    "sp.create_time createTime,",
                    "sd.depart_name companyId,",
                    "sp.status status,",
                    "case when sp.status='1' then 1 else 0 end statusCode ",
                    "from sys_position sp ",
                    "left join v_sys_dict_item vsdiPr on vsdiPr.item_value=sp.rank and vsdiPr.dict_code='position_rank' ",
                    "left join sys_depart sd on sd.org_code=sp.sys_org_code ",
                    " where 1=1 ",
                    " <if test='code!=null and code!=\"\"'>",
            " and sp.code=#{code}",
            " </if>",
            " <if test='name!=null and name!=\"\"'>",
            " and sp.name like concat('%',#{name},'%')",
            " </if>",
            " <if test='status!=null and status!=\"\"'>",
            " and sp.status =#{status} ",
            " </if>",
            " </script>",
    })
    IPage<SysPosition> queryPageList(Page<SysPosition> page, @Param("code") String  code, @Param("name") String  name,@Param("status") String  status);

    /**
     * 更改状态
     * @param id id
     * @param status 状态
     */
    @Update("update sys_position set status=#{status} where id=#{id}")
    void startOrStop(String id, String status);
}
