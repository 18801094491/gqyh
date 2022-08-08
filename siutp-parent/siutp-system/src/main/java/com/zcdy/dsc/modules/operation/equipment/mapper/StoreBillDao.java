package com.zcdy.dsc.modules.operation.equipment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.constant.BillType;
import com.zcdy.dsc.modules.operation.equipment.constant.GoodsItemStatus;
import com.zcdy.dsc.modules.operation.equipment.constant.GoodsItemType;
import com.zcdy.dsc.modules.operation.equipment.entity.OptGoodsItem;
import com.zcdy.dsc.modules.operation.equipment.vo.store.SparepartBillParamVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.SparepartDropdown;
import com.zcdy.dsc.modules.operation.equipment.vo.store.SparepartVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.StoreBillVo;

/**
 * 描述: 出入库自定义相关接口
 * @author：  songguang.jiao
 * 创建时间： 2020年2月3日 下午9:37:51 
 * 版本号: V1.0
 */
public interface StoreBillDao {
	
	

	/**
	 * 描述:  库存管理分页查询
	 * @return
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月11日 下午9:26:55 
	 * 版本号: V1.0
	 */
	@Select({
		" <script>",
		" SELECT",
		" s.id id,",
		" s.store_id storeId,",
		" (SELECT name from sys_category where code= s.sparepart_name) sparepartName,",
		" cat1.name sparepartSpecs, ",
		" cat2.name sparepartModel,",
		" s.warn_amount warnAmount,",
		" ta.id supplierId,",
		" ta.supplier_name supplierName,",
		" tb.store_name storeName,",
		" ( SELECT count( td.id ) FROM opt_goods_item td WHERE td.data_id = s.id AND td.item_type = 1 AND td.item_status = 1 ) AS amount ",
		" FROM",
		" opt_sparepart s",
		" LEFT JOIN opt_supplier ta ON ta.id = s.supplier_id",
		" LEFT JOIN opt_store tb ON tb.id = s.store_id ",
		" LEFT JOIN sys_category cat1 on cat1.code=s.sparepart_specs",
		" LEFT JOIN sys_category cat2 on cat2.code=s.sparepart_model",
		" WHERE",
		" s.del_flag = 0 ",
		" and s.store_id in ",
		" <foreach collection='storeIdList' item='item' index='index' open='(' separator=',' close=')'>",
        " #{item}",  
         " </foreach>",
		" <if test='sparepartName!=null and sparepartName!=\"\"'>",
		" and s.sparepart_name = #{sparepartName}",
		" </if>",
		" <if test='sparepartModel!=null and sparepartModel!=\"\"'>",
		" and cat2.name like concat('%',#{sparepartModel},'%')",
		" </if>",
		" <if test='sparepartSpecs!=null and sparepartSpecs!=\"\"'>",
		" and cat1.name like concat('%',#{sparepartSpecs},'%')",
		" </if>",
		" <if test='storeId!=null and storeId!=\"\"'>",
		" and s.store_id = #{storeId}",
		" </if>",
		" ORDER BY warn_amount asc,store_id asc",
		" </script>",
	})
	public IPage<SparepartVo> queryPageData(Page<SparepartVo> page, @Param("sparepartName") String sparepartName,
            @Param("sparepartModel") String sparepartModel,
            @Param("sparepartSpecs") String sparepartSpecs,@Param("storeId") String storeId,
            @Param("storeIdList") List<String> storeIdList);
	

	/**
	 * 描述: 出入库单分页查询
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月3日 下午9:25:11 
	 * 版本号: V1.0
	 */
	@Select({
		" <script>",
		" SELECT",
		" t.id id,",
		" t.batch_sn batchSn,",
		" t4.realname createBy,",
		" t.amount amount,",
		" t.create_time createTime,",
		" t.sparepart_id sparepartId,",
		" t3.sparepart_name sparepartName,",
		" t3.sparepart_specs sparepartSpecs,",
		" t.store_id storeId,",
		" t.end_date endDate,",
		" t.product_date productDate,",
		" t2.store_name storeName ",
		"FROM ",
		" opt_store_bill t",
		" LEFT JOIN opt_store t2 ON t2.id = t.store_id",
		" LEFT JOIN opt_sparepart t3 ON t3.id = t.sparepart_id",
		" LEFT JOIN sys_user t4 on t4.username=t.create_by",
		" where t.del_flag='0' and t.bill_type=#{billType}",
		" and t.store_id in ",
		" <foreach collection='storeList' item='item' index='index' open='(' separator=',' close=')'>",
        " #{item}",  
         " </foreach>",   
		" <if test='sparepartName!=null and sparepartName!=\"\"'>",
		" and t3.sparepart_name like concat('%',#{sparepartName},'%')",
		" </if>",
		" <if test='batchSn!=null and batchSn!=\"\"'>",
		" and t.batch_sn like concat('%',#{batchSn},'%')",
		" </if>",
		" ORDER BY t.create_time desc",
		" </script>",
	})
	public IPage<StoreBillVo> queryBillPageList(Page<StoreBillVo> page,@Param("sparepartName") String sparepartName,
			@Param("batchSn")String batchSn,@Param("storeList") List<String> storeList,@Param("billType")String billType);
	

	/**
	 * 描述: 查询最大入库单编号
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月4日 上午10:15:12 
	 * 版本号: V1.0
	 */
	@Select({
		"SELECT max(SUBSTR(bill_sn,10)) from opt_store_bill WHERE bill_type ="+BillType.IN,
		"and date(create_time)=CURDATE() ",
	})
	public String getMaxInBill();
	
	/**
	 * 描述: 查询最大出库单编号
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月4日 上午10:15:12 
	 * 版本号: V1.0
	 */
	@Select({
		"SELECT max(SUBSTR(batch_sn,10)) from opt_store_bill WHERE bill_type ="+BillType.OUT,
		 " and date(create_time)=CURDATE() ",
	})
	public String getMaxOutBill();
	
	/**
	 * 描述: 查询备品备件下拉列表
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月4日 下午1:35:53 
	 * 版本号: V1.0
	 */
	@Select({
		" <script>",
		" SELECT",
		" t.id,",
		" t.sparepart_name",
		" FROM",
		" opt_sparepart t",
		" where",
		" t.del_flag='0'",
		" <if test='sparepartName!=null and sparepartName!=\"\"'>",
		"  and t.sparepart_name like concat('%',#{sparepartName},'%')",
		" </if>",
		" order by t.create_time desc",
		" </script>",
	})
	public List<SparepartDropdown> dropdown(@Param("sparepartName")String sparepartName);
	
	
	/**
	 * 描述: 校验库存
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月4日 下午3:56:11 
	 * 版本号: V1.0
	 */
	@Select({
		" SELECT",
		" count( id )",
		" FROM",
		" opt_goods_item t ",
		" WHERE",
		" t.item_status ="+GoodsItemStatus.NORMAL,
		" AND t.item_type = "+GoodsItemType.SPAREPART,
		" AND t.batch_sn =#{optStoreBill.sparepartId} ",
	})
	public Integer checkBillStore(@Param("optStoreBill")SparepartBillParamVo optStoreBill);
	
	
	/**
	 * 描述:批量插入货品信息 
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月5日 下午1:42:14 
	 * 版本号: V1.0
	 */
	@Insert({
		" <script>",
		" INSERT INTO `opt_goods_item` ( `id`, `data_id`, `item_sn`, `item_type`, `batch_sn`, `item_status`, `create_by`, `create_time`, `del_flag` )",
		" VALUES",
		" <foreach collection='list' item='item' index='index' separator=','>",
		" (",
		" #{item.id},",
		" #{item.dataId},",
		" #{item.itemSn},",
		" #{item.itemType},",
		" #{item.batchSn},",
		" #{item.itemStatus},",
		" #{item.createBy},",
		" #{item.createTime},",
		" #{item.delFlag}",
		" )",
		" </foreach>",
		" </script>",
	})
	public void insertGoodsItems(@Param("list")List<OptGoodsItem> list);
	
	/**
	 * 描述: 根据批次号跟备品备件id 更改货物状态 
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月5日 下午4:11:29 
	 * 版本号: V1.0
	 */
	@Update({
		" <script>",
		" <foreach collection='list' item='item' index='index' separator=';'>",
		" UPDATE opt_goods_item ",
		" SET item_status = #{item.itemStatus},",
		" update_by=#{item.updateBy},",
		" update_time=#{item.updateTime}",
		" WHERE",
		" id =",
		" ( SELECT b.a FROM ( SELECT ( SELECT id FROM opt_goods_item WHERE item_type = "+GoodsItemType.SPAREPART,
		" AND item_status ="+GoodsItemStatus.NORMAL,
		"AND data_id = #{item.dataId}  LIMIT 1 ) a ) b )",
		" </foreach>",
		" </script>",
	})
	public void updateGoodsItemsStatus(@Param("list")List<OptGoodsItem> list);
	
}
