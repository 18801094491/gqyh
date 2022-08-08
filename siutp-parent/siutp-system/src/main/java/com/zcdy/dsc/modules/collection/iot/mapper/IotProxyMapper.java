package com.zcdy.dsc.modules.collection.iot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.collection.iot.entity.IotProxy;
import com.zcdy.dsc.modules.collection.iot.entity.IotProxyEquipment;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipVo;
import com.zcdy.dsc.modules.collection.iot.vo.IotProxyVo;

/**
 * 描述: 采集代理管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-13
 * 版本号: V1.0
 */
public interface IotProxyMapper extends BaseMapper<IotProxy> {

	/**
	 * 描述:  更改采集代理状态
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月13日 下午1:44:57 
	 * 版本号: V1.0
	 */
	@Update("UPDATE iot_proxy set proxy_status=(case proxy_status when 0 then 1 when 1 then 0 else proxy_status end)  where id=#{id}")
	void editProxyStatus(@Param("id") String id);

	/**
	 * 描述:  查询代理列表
	 * @return
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月13日 下午2:10:41 
	 * 版本号: V1.0
	 */
	@Select({
		" SELECT",
		" t.id,",
		" t.proxy_name proxyName,",
		" t.ip_address ipAddress,",
		" t.heartbeat_address heartbeatAddress,",
		" t.proxy_type proxyTypeCode,",
		" t.heartbeat_unit heartbeatUnitCode,",
		" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'time_type' ) AND item_value = t.heartbeat_unit ) heartbeatUnit,",
		" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'proxy_type' ) AND item_value = t.proxy_type ) proxyType,",
		" t.heartbeat_cycle heartbeatCycle,",
		" t.proxy_status proxyStatusCode,",
		" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = t.proxy_status ) proxyStatus,",
		" t.heartbeat_status heartbeatStatusCode,",
		" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = t.heartbeat_status ) heartbeatStatus ",
		" FROM",
		" iot_proxy t where t.del_flag='0'",
	})
	List<IotProxyVo> queryData();

	/**
	 * 描述:  分页查询所有采集设备列表
	 * @return
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月13日 下午2:46:46 
	 * 版本号: V1.0
	 */
	@Select({
		" <script>",
		" SELECT",
		" t.id,",
		" t.iot_name iotName,",
		" t.iot_sn iotSn,",
		" ( SELECT count( id ) FROM iot_proxy_equipment WHERE equip_id = t.id ) bindStatus ",
		" FROM",
		" iot_equipment t where 1=1",
		" <if test='iotSn!=null and iotSn!=\"\"'>",
		" and t.iot_sn=#{iotSn}",
		" </if>",
		" <if test='iotName!=null and iotName!=\"\"'>",
		" and t.iot_name=#{iotName}",
		" </if>",
		" <if test='iotCategory!=null and iotCategory!=\"\"'>",
		" and t.iot_category=#{iotCategory}",
		" </if>",
		" </script>",
	})
	IPage<IotEquipVo> queryAllEquip(Page<IotEquipVo> page,@Param("iotSn") String iotSn,@Param("iotName") String iotName,@Param("iotCategory") String iotCategory);

	/**
	 * 描述:  单个代理已绑定的采集设备列表
	 * @return
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月13日 下午3:14:28 
	 * 版本号: V1.0
	 */
	@Select({
		" <script>",
		" SELECT",
		" t.id,",
		" t.iot_name iotName,",
		" t.iot_sn iotSn ",
		" FROM",
		" iot_equipment t",
		" WHERE",
		" id IN ( SELECT equip_id FROM iot_proxy_equipment WHERE proxy_id = #{proxyId} )",
		" <if test='iotSn!=null and iotSn!=\"\"'>",
		" and t.iot_sn=#{iotSn}",
		" </if>",
		" <if test='iotName!=null and iotName!=\"\"'>",
		" and t.iot_name=#{iotName}",
		" </if>",
		" <if test='iotCategory!=null and iotCategory!=\"\"'>",
		" and t.iot_category=#{iotCategory}",
		" </if>",
		" </script>",
	})
	IPage<IotEquipVo> getBindEquip(Page<IotEquipVo> page,@Param("proxyId") String proxyId,@Param("iotSn") String iotSn,@Param("iotName") String iotName,@Param("iotCategory") String iotCategory);


	/**
	 * 描述:批量插入采集设备
	 * @param list
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月13日 下午9:28:49 
	 * 版本号: V1.0
	 */
	@Insert({
		" <script>",
		" INSERT INTO `iot_proxy_equipment` ( `id`, `proxy_id`, `equip_id` )",
		" VALUES",
		" <foreach collection='list' item='item' index='index' separator=','>",
		" (",
		" #{item.id},",
		" #{item.proxyId},",
		" #{item.equipId}",
		" )",
		" </foreach>",
		" </script>",
	})
	void saveBatch(@Param("list") List<IotProxyEquipment> list);

}
