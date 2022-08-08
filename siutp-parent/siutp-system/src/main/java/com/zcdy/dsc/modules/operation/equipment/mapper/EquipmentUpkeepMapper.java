package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.entity.EquipmentUpkeep;
import com.zcdy.dsc.modules.operation.equipment.entity.EquipmentUpkeepAttach;
import com.zcdy.dsc.modules.operation.equipment.entity.EquipmentUpkeepUsers;
import com.zcdy.dsc.modules.operation.equipment.vo.AttachVo;
import com.zcdy.dsc.modules.operation.equipment.vo.EquipmentUpkeepVo;
import com.zcdy.dsc.modules.operation.equipment.vo.UpkeepUsersVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 描述: 维保记录
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-17
 * 版本号: V1.0
 */
public interface EquipmentUpkeepMapper extends BaseMapper<EquipmentUpkeep> {
	
	/**
    * 描述: 查维保记录列表
    * @author：  songguang.jiao
    * 创建时间： 2020年2月25日 上午10:22:00 
    * 版本号: V1.0
    */
	@Results(id="selectListInfos",
			value={
					@Result(property="id",column="id"),
					@Result(property="list",javaType=List.class,column="id",many=@Many(select="selectByUpkeepIdAtt"))
			})
	@Select({
		"<script>",
		" SELECT",
		" ta.id id,",
		" ( SELECT GROUP_CONCAT( upkeep_user ) FROM equipment_upkeep_users WHERE upkeep_id = ta.id ) upkeepUsersId,",
		" ( SELECT GROUP_CONCAT( realname ) FROM sys_user WHERE id IN ( SELECT upkeep_user FROM equipment_upkeep_users WHERE upkeep_id = ta.id ) ) upkeepUsers,",
		" ta.equipment_id equipmentId,",
		" tc.equipment_name equipmentName,",
		" ta.upkeep_creator upkeepCreator,",
		" tb.realname upkeepCreatorName,",
		" ta.upkeep_time upkeepTime,",
		" ta.upkeep_content upkeepContent,",
		" ta.upkeep_reason upkeepReason,",
		" ta.upkeep_result upkeepResult,",
		" ta.type type,",
		" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'upkeep_type' ) AND item_value = ta.type ) typeText ",
		" FROM",
		" equipment_upkeep ta",
		" LEFT JOIN sys_user tb ON tb.id = ta.upkeep_creator",
		" LEFT JOIN opt_equipment tc ON tc.id = ta.equipment_id ",
		" WHERE",
		" ta.del_flag =0",
		" <if test='equipmentName!=null and equipmentName!=\"\"'>",
		" and tc.equipment_name like concat('%',#{equipmentName},'%')",
		" </if>",
		" <if test='type!=null and type!=\"\"'>",
		"  and ta.type=#{type} ",
		" </if>",
		" <if test='upkeepTimeStart!=null and upkeepTimeStart!=\"\"'>",
		"  AND DATE_FORMAT(ta.upkeep_time,'%Y-%m-%d') &gt;= #{upkeepTimeStart}",
		" </if>",
		" <if test='upkeepTimeEnd!=null and upkeepTimeEnd!=\"\"'>",
		" AND DATE_FORMAT(ta.upkeep_time,'%Y-%m-%d') &lt;= #{upkeepTimeEnd}",
		" </if>",
		" order by ta.upkeep_time desc",
		"</script>"
	})
    IPage<EquipmentUpkeepVo> selectListInfo(Page<EquipmentUpkeepVo> page,
                                            @Param("equipmentName") String equipmentName,
                                            @Param("upkeepTimeStart") String upkeepTimeStart,
                                            @Param("upkeepTimeEnd") String upkeepTimeEnd,
                                            @Param("type") String type);
	
	
	
	
    
	/**
	 * 描述: 维保记录详情
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月25日 上午10:18:16 
	 * 版本号: V1.0
	 */
	@Select({
		"  SELECT ta.upkeep_image upkeepImage,ta.upkeep_thumb upkeepThumb FROM equipment_upkeep_attach ta WHERE ta.upkeep_id =#{upkeepId}"
	})
    List<AttachVo> selectByUpkeepIdAtt(@Param("upkeepId")String upkeepId);
    //维保记录详情
    List<UpkeepUsersVo> selectByUpkeepIdUser(@Param("upkeepId")String upkeepId);
    //导出
    List<EquipmentUpkeepVo> getExportXls();
    //通过资产id查询-具体的维保信息
    List<EquipmentUpkeepVo> selectUpkeepInfo(@Param("equipmentId")String equipmentId);
    
    /**
     * 描述: 批量插入用户
     * @author：  songguang.jiao
     * 创建时间： 2020年2月24日 下午3:45:01 
     * 版本号: V1.0
     */
	@Insert({
		" <script>",
		" INSERT INTO `equipment_upkeep_users` ( `id`, `upkeep_user`, `upkeep_id` )",
		" VALUES",
		" <foreach collection='list' item='item' index='index' separator=','>",
		" (",
		" #{item.id},",
		" #{item.upkeepUser},",
		" #{item.upkeepId}",
		" )",
		" </foreach>",
		" </script>",
	})
    void insertUsersData(@Param("list")List<EquipmentUpkeepUsers> list);
	
	/**
	 * 描述: 批量插入图片地址
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月24日 下午4:14:09 
	 * 版本号: V1.0
	 */
	@Insert({
		" <script>",
		" INSERT INTO `equipment_upkeep_attach` ( `id`, `upkeep_thumb`, `upkeep_image`, `upkeep_id` )",
		" VALUES",
		" <foreach collection='list' item='item' index='index' separator=','>",
		" (",
		" #{item.id},",
		" #{item.upkeepThumb},",
		" #{item.upkeepImage},",
		" #{item.upkeepId}",
		" )",
		" </foreach>",
		" </script>",
	})
	void insertAttachData(@Param("list")List<EquipmentUpkeepAttach> list);
	

	
	/**
	 * 描述: 删除维保附件
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月24日 下午6:25:01 
	 * 版本号: V1.0
	 */
	@Delete(" delete from equipment_upkeep_attach where upkeep_id=#{id}")
	void deleteAttach(@Param("id")String id);
	
	/**
	 * 描述: 删除维保参与用户
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月24日 下午6:25:01 
	 * 版本号: V1.0
	 */
	@Delete(" delete from equipment_upkeep_users where upkeep_id=#{id}")
	void deleteUsers(@Param("id") String id);
	
	
}
