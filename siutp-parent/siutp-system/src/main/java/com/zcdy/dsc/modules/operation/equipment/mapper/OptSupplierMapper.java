package com.zcdy.dsc.modules.operation.equipment.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.operation.equipment.entity.OptSupplier;
import com.zcdy.dsc.modules.operation.equipment.vo.SupplierListVo;
import com.zcdy.dsc.modules.operation.equipment.vo.SupplierVo;

/**
 * 描述: 供应商管理
 * @author： bthy
 * 创建时间：   2019-12-16
 * 版本号: V1.0
 */
public interface OptSupplierMapper extends BaseMapper<OptSupplier> {

	/**
	 * 更改供应商状态
	 * @param supplierStatus
	 */
	@Update({
		"update opt_supplier set supplier_status=(case supplier_status when 0 then 1 when 1 then 0 else supplier_status end) where id=#{id}"
	})
	public void startupAndShutdown(@Param("id") String id);
	
	
	/**
	 * 分页查询列表
	 * @return
	 */
	public IPage<SupplierVo> getList(IPage<SupplierVo> page,@Param("supplierSn")String supplierSn,
			@Param("supplierName")String supplierName,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	/**
	 * 查询供应商下拉列表
	 * @return
	 */
	@Select({
		" <script>",
		" SELECT",
		" s.id id,",
		" s.supplier_name supplierName",
		" FROM",
		" opt_supplier s ",
		" where",
		" s.del_flag = '0' ",
		" AND s.supplier_status = '1' ",
		" <if test='name!=null and name!=\"\"'>",
		" and s.supplier_name like concat('%',#{name},'%')",
		" </if>",
		" ORDER BY 	create_time DESC",
		" </script>",
	})
	public List<SupplierListVo> queryNameList(@Param("name")String name);

	/**
	 * 描述:检验供应商编号是否存在 
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月29日 下午1:56:51 
	 * 版本号: V1.0
	 */
	@Select({
		" <script>",
		" SELECT count(t.id) from  opt_supplier t where t.supplier_sn=#{supplierSn} ",
		" <if test='id!=null and id!=\"\"'>",
		"  and t.id!=#{id}",
		" </if>",
		" </script>",
	})
	public Boolean checkSnExist(@Param("id")String id,@Param("supplierSn") String supplierSn);
}
