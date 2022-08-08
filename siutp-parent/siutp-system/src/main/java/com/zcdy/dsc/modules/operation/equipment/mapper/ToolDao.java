package com.zcdy.dsc.modules.operation.equipment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.operation.equipment.entity.OptToolBorrow;
import com.zcdy.dsc.modules.operation.equipment.entity.StoreTree;
import com.zcdy.dsc.modules.operation.equipment.vo.UserCodeName;
import com.zcdy.dsc.modules.operation.equipment.vo.store.BorrowHistoryVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.GoodsApplyListVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.GoodsBorrowVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.StoreDropdownVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.StoreUserName;
import com.zcdy.dsc.modules.operation.equipment.vo.store.StoreVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.ToolExportVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.ToolVo;

/**
 * 描述: 工具相关自定义接口
 * @author：  songguang.jiao
 * 创建时间：  2020年1月9日 上午10:58:08
 * 版本号: V1.0
 */
public interface ToolDao {

	/**
	 * 描述: 分页查询仓库管理列表
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月9日 上午11:11:21
	 * 版本号: V1.0
	 */
	@Select({
		" <script>",
		" SELECT",
		" t.id,",
		" t.store_sn storeSn,",
		" t.store_name storeName,",
		" t.store_position storePosition,",
		" t.store_status storeStatusCode,",
		" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = t.store_status ) storeStatus,",
		" 	(SELECT GROUP_CONCAT(realname) from sys_user where id in(SELECT manage from opt_store_manager where store_id=t.id)) manager, ",
		" t.create_time createTime",
		" FROM",
		" opt_store t",
		" where t.del_flag='0'",
		" <if test='storeSn!=null and storeSn!=\"\"'>",
		" and t.store_sn like concat('%',#{storeSn},'%')",
		" </if>",
		" <if test='storeName!=null and storeName!=\"\"'>",
		" and t.store_name like concat('%',#{storeName},'%')",
		" </if>",
		" ORDER BY t.create_time desc",
		" </script>",
	})
	public IPage<StoreVo> queryStorePageList(IPage<StoreVo> page,@Param("storeName")String storeName,@Param("storeSn")String storeSn);
	
	/**
	 * 查询最后一个库房编号
	 * 描述: 
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月2日 下午5:32:43
	 * 版本号: V1.0
	 */
	@Select({
		"SELECT MAX(SUBSTR(store_sn,2)) from opt_store"
	})
	public String getLastStoreSn();
	
	/**
	 * 描述: 分页查询工具库存列表
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月9日 下午1:09:44
	 * 版本号: V1.0
	 */
	@Select({
		" <script>",
		" SELECT",
		" t.id,",
		" (SELECT name from sys_category where code=t.tool_name) toolName,",
		" cat.name toolModel,",
		" ( SELECT supplier_name FROM opt_supplier WHERE id = t.supplier_id ) supplierName,",
		" t.tool_factory toolFactory,",
		" ( SELECT store_name FROM opt_store WHERE id = t.store_id ) storeName,",
		" ( SELECT count( t1.id ) FROM opt_goods_item t1 WHERE t1.data_id = t.id AND t1.item_type = '0' ) AS total,",
		" ( SELECT count( t1.id ) FROM opt_goods_item t1 WHERE t1.data_id = t.id AND t1.item_type = '0' AND t1.item_status = '3' ) AS damageNum,",
		" ( SELECT count( t1.id ) FROM opt_goods_item t1 WHERE t1.data_id = t.id AND t1.item_type = '0' AND t1.item_status = '2' ) AS borrowedNum,",
		" ( SELECT count( t1.id ) FROM opt_goods_item t1 WHERE t1.data_id = t.id AND t1.item_type = '0' AND t1.item_status = '1' ) AS usedNum",
		" FROM",
		" opt_tool t",
		" LEFT JOIN sys_category cat on cat.code=t.tool_model",
		" where t.del_flag='0'",
		" and t.store_id in ",
		"  <foreach collection='storeList' item='item' index='index' open='(' separator=',' close=')'>",
        " #{item}",  
         " </foreach>",      
		" <if test='toolName!=null and toolName!=\"\"'>",
		" and t.tool_name =#{toolName}",
		" </if>",
		" <if test='storeId!=null and storeId!=\"\"'>",
		" and t.store_id =#{storeId}",
		" </if>",
		" <if test='toolModel!=null and toolModel!=\"\"'>",
		" and cat.name like concat('%',#{toolModel},'%')",
		" </if>",
		" ORDER BY t.create_time desc",
		" </script>",
	})
	public IPage<ToolVo> queryToolList(IPage<ToolVo> page,@Param("toolName")String toolName,@Param("storeId")String storeId,
			@Param("toolModel")String toolModel,@Param("storeList")List<String> storeList);
	
	
	
	/**
	 * 描述: 查询仓库地址下拉选
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月9日 下午6:03:27
	 * 版本号: V1.0
	 */
	@Select({
		"SELECT t.id storeId,t.store_name storePosition from opt_store t ORDER BY t.create_time desc"
	})
	public List<StoreDropdownVo> queryStoreList();
	
	/**
	 * 描述: 更改仓库启停用状态
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月19日 下午3:36:58
	 * 版本号: V1.0
	 */
	@Update({
		" UPDATE opt_store set store_status=(case store_status when 0 then 1 when 1 then 0 else store_status end) where id=#{id}"
	})
	public void editStoreStatus(String id);
	
	/**
	 * 描述: 查询用户下拉列表
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月19日 下午4:11:31
	 * 版本号: V1.0
	 */
	@Select({
		"<script> ",
		" SELECT",
		" t.id,",
		" t.realname name",
		" FROM",
		" sys_user t",
		" where t.del_flag='0'",
		" <if test='name!=null and name!=\"\"'>",
		"  and t.realname like concat('%',#{name},'%')",
		" </if>",
		" ORDER BY t.username",
		"</script> ",
	})
	public List<StoreUserName> getUserList(@Param("name") String name);
	
	
	/**
	 * 描述: 查询最大入库单编号
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月4日 上午10:15:12 
	 * 版本号: V1.0
	 */
	@Select({
		"SELECT max(SUBSTR(bill_sn,10)) from opt_tool_bill WHERE  date(create_time)=CURDATE() ",
	})
	public String getMaxInBill();
	
	/**
	 * 描述: 运维工具导出
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 上午10:04:32 
	 * 版本号: V1.0
	 */
	@Select({
		" <script>",
		" SELECT",
		" t.id,",
		" (SELECT name from sys_category where code=t.tool_name) toolName,",
		" cat.name toolModel,",
		" ( SELECT supplier_name FROM opt_supplier WHERE id = t.supplier_id ) supplierName,",
		" t.tool_factory toolFactory,",
		" ( SELECT store_name FROM opt_store WHERE id = t.store_id ) storeName,",
		" ( SELECT count( t1.id ) FROM opt_goods_item t1 WHERE t1.data_id = t.id AND t1.item_type = '0' ) AS total,",
		" ( SELECT count( t1.id ) FROM opt_goods_item t1 WHERE t1.data_id = t.id AND t1.item_type = '0' AND t1.item_status = '3' ) AS damageNum,",
		" ( SELECT count( t1.id ) FROM opt_goods_item t1 WHERE t1.data_id = t.id AND t1.item_type = '0' AND t1.item_status = '2' ) AS borrowedNum,",
		" ( SELECT count( t1.id ) FROM opt_goods_item t1 WHERE t1.data_id = t.id AND t1.item_type = '0' AND t1.item_status = '1' ) AS usedNum",
		" FROM",
		" opt_tool t",
		" LEFT JOIN sys_category cat on cat.code=t.tool_model",
		" where t.del_flag='0'",
		" <if test='toolName!=null and toolName!=\"\"'>",
		" and t.tool_name =#{toolName}",
		" </if>",
		" <if test='storeId!=null and storeId!=\"\"'>",
		" and t.store_id =#{storeId}",
		" </if>",
		" <if test='toolModel!=null and toolModel!=\"\"'>",
		" and cat.name like concat('%',#{toolModel},'%')",
		" </if>",
		" ORDER BY t.create_time desc",
		" </script>",
	})
	public List<ToolExportVo> exportCutomerList(@Param("toolName")String toolName, @Param("storeId")String storeId,
			@Param("toolModel")String toolModel);

	/**
	 * 描述: 查询运维工具借用详情
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午4:31:11 
	 * 版本号: V1.0
	 */
	@Select({
		"<script>",
		" SELECT",
		" t.id goodsId,",
		" t.item_sn itemSn,",
		" t.item_status itemStatus,",
		" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'goods_item_status' ) AND item_value = t.item_status ) itemStatusName,",
		" ( SELECT count( id ) FROM opt_tool_borrow WHERE goods_id = t.id ) borrowTimes ",
		" FROM",
		" opt_goods_item t",
		" where t.data_id=#{toolid} ",
		" <if test='itemSn!=null and itemSn!=\"\"'>",
		" and t.item_sn like concat('%',#{itemSn},'%') ",
		" </if>",
		" <if test='batchSn!=null and batchSn!=\"\"'>",
		" and t.batch_sn=#{batchSn}",
		" </if>",
		"</script>",
	})
	public IPage<GoodsBorrowVo> queryGoodsBorrowDetail(IPage<GoodsBorrowVo> page,@Param("toolid")String toolid,@Param("itemSn")String itemSn,@Param("batchSn")String batchSn);
	
	/**
	 * 描述: 批量更新货物状态
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午5:52:44 
	 * 版本号: V1.0
	 */
	@Update({
		" <script>",
		" update opt_goods_item set item_status=#{itemStatus} where id in ",
		" <if test='array!=null and array!=\"\"'>",
		" <foreach collection='array' item='item' index='index' open='(' separator=',' close=')'>",
		" #{item}",
		" </foreach>",
		" </if>",
		" </script>",
	})
	public void updateGoodsStatus(@Param("array")String[] array,@Param("itemStatus")String itemStatus);
	
	/**
	 * 描述: 校验是否存在非在库货品
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午6:16:00 
	 * 版本号: V1.0
	 */
	@Select({
		" <script>",
		" select ",
		" count( id )",
		" FROM",
		" opt_goods_item t",
		" where t.item_status!='1'",
		" and t.id in",
		" <foreach collection='array' item='item' index='index' open='(' separator=',' close=')'>",
		" #{item}",
		" </foreach>",
		" </script>",
	})
	public Integer checkOtherGoodsStatus(@Param("array")String[] array);

	/**
	 * 描述: 批量插入借阅历史
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午9:35:08 
	 * 版本号: V1.0
	 */
	@Insert({
		" <script>",
		" INSERT INTO `opt_tool_borrow` ( `id`, `goods_id`, `apply_sn`, `borrow_time`, `back_status`, `user_id`, `create_by`, `create_time`, `del_flag` )",
		" VALUES",
		" <foreach collection='list' item='item' index='index' separator=','>",
		" (",
		" #{item.id},",
		" #{item.goodsId},",
		" #{item.applySn},",
		" #{item.borrowTime},",
		" #{item.backStatus},",
		" #{item.userId},",
		" #{item.createBy},",
		" #{item.createTime},",
		" #{item.delFlag}",
		" )",
		" </foreach>",
		" </script>",
	})
	public void insertToolBorrow(@Param("list")List<OptToolBorrow> list);
	
	/**
	 * 描述: 更改货物状态
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午10:03:56 
	 * 版本号: V1.0
	 */
	@Update({
		" update opt_goods_item set item_status=#{itemStatus} where id=#{id}"
	})
	public void editGoodsStatus(@Param("id")String id, @Param("itemStatus")String itemStatus);
	
	/**
	 * 描述: 更新借用历史备注(工具借用历史中最多有一条为在借状态，更新那一条即可)
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午10:13:40 
	 * 版本号: V1.0
	 */
	@Update({
		" UPDATE opt_tool_borrow set description=#{description},back_status='1',update_time=current_timestamp  where goods_id=#{goodsId} and back_status='0' ",
	})
	public void updateToolBorrowDesc(@Param("goodsId")String goodsId,@Param("description")String description);
	
	
	/**
	 * 描述: 运维工具借用历史
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月8日 下午3:40:34 
	 * 版本号: V1.0
	 */
	@Select({
		" <script>",
		" SELECT",
		" t.borrow_time borrowTime,",
		" t1.realname userName,",
		" t.update_time backTime,",
		" t.description ",
		" FROM",
		" opt_tool_borrow t",
		" LEFT JOIN sys_user t1 ON t1.id = t.user_id",
		" WHERE",
		" t.goods_id =#{goodsId}",
		" <if test='userName!=null and userName!=\"\"'>",
		"  AND t1.realname LIKE concat('%',#{userName},'%') ",
		" </if>",
		" <if test='startTime!=null and startTime!=\"\"'>",
		"  	and date_format(t.create_time,'%Y-%m-%d')&gt;=#{startTime} ",
		" </if>",
		" <if test='endTime!=null and endTime!=\"\"'>",
		"  and DATE_FORMAT(t.create_time,'%Y-%m-%d')&lt;=#{endTime} ",
		" </if>",
		" </script>",
	})
	public IPage<BorrowHistoryVo> queryBorrowHistory(IPage<BorrowHistoryVo> page,@Param("userName")String userName,@Param("goodsId")String goodsId,
			@Param("startTime")String startTime,@Param("endTime")String endTime);

	/**
	 * 描述: 更新申请单状态为已处理
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月8日 下午7:58:30 
	 * 版本号: V1.0
	 */
	@Update({
		" update opt_goods_apply set deal_status='1' where apply_sn=#{applySn}"
	})
	public void updateApplyDealStatus(@Param("applySn")String applySn);
	
	/**
	 * 描述: 通过借用人查询未处理的申请单
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月8日 下午8:32:53 
	 * 版本号: V1.0
	 */
	@Select({
		" SELECT apply_sn applySn,apply_sn applySnValue	FROM opt_goods_apply WHERE",
		" create_by = (SELECT username from sys_user where id=#{userId}) ",
		 "AND deal_status = '0' ",
	})
	public List<GoodsApplyListVo> queryGoodsApply(@Param("userId")String userId);
	
	/**
	 * 描述: 查询用户下拉列表
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月19日 下午4:11:31
	 * 版本号: V1.0
	 */
	@Select({
		"<script> ",
		" SELECT",
		" t.username code,",
		" t.realname name",
		" FROM",
		" sys_user t",
		" where t.del_flag='0'",
		" <if test='name!=null and name!=\"\"'>",
		"  and t.realname like concat('%',#{name},'%')",
		" </if>",
		"</script> ",
	})
	public List<UserCodeName> getUserCodeNameList(@Param("name") String name);


	/**
	 * 描述:检验仓库名称是否存在 
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月29日 下午2:08:43 
	 * 版本号: V1.0
	 */
	@Select({
		" <script>",
		" SELECT count(t.id) from  opt_store t where t.store_name=#{storeName} ",
		" <if test='id!=null and id!=\"\"'>",
		"  and t.id!=#{id}",
		" </if>",
		" </script>",
	})
	public Boolean checkSoreNameExist(@Param("id") String id,@Param("storeName") String storeName);

	/**
	 * 描述:  删除仓库与管理员关系
	 * @param userId
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月7日 下午4:50:02 
	 * 版本号: V1.0
	 */
	@Delete({
		"DELETE from opt_store_manager where manage=#{userId} "
	})
	public void deleteStoreManager(@Param("userId") String userId);

	/**
	 * 描述:  通过用户查看所管理的仓库
	 * @param manager  用户id
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月7日 下午5:00:00 
	 * 版本号: V1.0
	 */
	@Select({"SELECT store_id from opt_store_manager where manage=#{manager}"})
	public List<String> queryStoreIdList(@Param("manager") String manager);

	
	/**
	 * 描述:  查询树状结构仓库数据
	 * @return
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月7日 下午5:10:29 
	 * 版本号: V1.0
	 */
	@Select({
		"SELECT id treeId,store_name treeName,'1' as is_leaf,'0' as Pid from opt_store where del_flag='0' ",
		" union all SELECT '0' treeId,'所有仓库' as  treeName, '0' as is_leaf,'' as Pid",
	})
	List<StoreTree> getTreeStore();

	/**
	 * 描述:  通过仓库查看所有管理员
	 * @param storeId
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月7日 下午7:56:55 
	 * 版本号: V1.0
	 */
	@Select("SELECT GROUP_CONCAT(manage) from opt_store_manager where store_id=#{storeId} ")
	public String queryManagerIdList(@Param("storeId") String storeId);
}
