package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.collection.gis.vo.GisLocationVo;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentInfoVO;
import com.zcdy.dsc.modules.operation.equipment.entity.OptEquipment;
import com.zcdy.dsc.modules.operation.equipment.vo.*;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeEquipData;
import com.zcdy.dsc.modules.operation.work.vo.WorkPointDropdown;
import com.zcdy.dsc.modules.settle.constant.SettleConstant;
import com.zcdy.dsc.modules.system.vo.DictDropdown;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 描述: 设备资产
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-17
 * 版本号: V1.0
 */
public interface OptEquipmentMapper extends BaseMapper<OptEquipment> {

    /**
     * 设备资产分页查询列表
     *
     * @return
     */
    public IPage<OptEquipmentModel> getList(IPage<OptEquipmentModel> page,
                                            @Param("equipmentSupplier") String equipmentSupplier, @Param("equipmentSn") String equipmentSn, @Param("equipmentType") String equipmentType, @Param("equipmentRevstop") String equipmentRevstop, @Param("equipmentCategory") String equipmentCategory);


    /**
     * 获取资产详情
     *
     * @param id
     * @return
     */
    public OptEquipmentModel getDetail(String id);

    /**
     * 查询设备关联gis模型下拉列表
     *
     * @param page
     * @param name
     * @return
     */
    @Select({
            " <script>",
            " SELECT",
            " t.id,",
            " t.equipment_sn name,",
            " cat.NAME type ",
            " FROM",
            " opt_equipment t",
            " LEFT JOIN sys_category cat ON cat.CODE = t.equipment_type",
            " WHERE 1=1",
            " <if test='name!=null and name!=\"\"'>",
            " and t.equipment_sn like concat('%',#{name},'%')",
            " </if>",
            " </script>",
    })
    public IPage<GisVo> getGisVoList(IPage<GisVo> page, @Param("name") String name);

    @Select({
            "SELECT "
                    + "cat.`name`  equipmentSection,t.equipment_location  equipmentLocation"
                    + " FROM"
                    + " opt_equipment t"
                    + " LEFT JOIN sys_category cat on cat.code=t.equipment_section"
                    + " where t.id=#{id}"
    })
    public GisLocationVo getLocation(String id);


    /**
     * 描述: 通过设备类型,型号,规格查询关联的知识库数据
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月27日 下午5:19:37
     * 版本号: V1.0
     */
    @Select({
            " <script>",
            " SELECT",
            " s.id id,",
            " s.knowlege_name knowlegeName,",
            " sysc1.NAME type",
            " FROM",
            " opt_knowlege s",
            " LEFT JOIN sys_category sysc1 ON s.type = sysc1.CODE",
            " where 1=1",
            " <if test='equipmentTypeCode!=null and equipmentTypeCode!=\"\"'>",
            "  and s.equipment_type= #{equipmentTypeCode}",
            " </if>",
            " <if test='equipmentMode!=null and equipmentMode!=\"\"'>",
            " and s.equipment_model=#{equipmentMode} ",
            " </if>",
            " <if test='equipmentSpecs!=null and equipmentSpecs!=\"\"'>",
            " and s.equipment_specs=#{equipmentSpecs} ",
            " </if>",
            " order by s.create_time desc",
            " </script>",
    })
    public List<KnowlegeEquipData> queryKnowlegeData(@Param("equipmentTypeCode") String equipmentTypeCode, @Param("equipmentMode") String equipmentMode,
                                                     @Param("equipmentSpecs") String equipmentSpecs);


    /**
     * 描述:校验设备编号是否存在
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月29日 下午1:42:49
     * 版本号: V1.0
     */
    @Select({
            " <script>",
            " SELECT count(t.id) from  opt_equipment t where t.equipment_sn=#{sn} ",
            " <if test='id!=null and id!=\"\"'>",
            "  and t.id!=#{id}",
            " </if>",
            " </script>",
    })
    public Boolean checkEquipSnExist(@Param("id") String id, @Param("sn") String sn);

    /**
     * 校验海康监控设备唯一编码是否存在
     * @param id 设备资产唯一标识
     * @param monitorCode 海康监控设备唯一编码
     * @return
     */
    @Select({
            "<script>" +
            "select count(t.id) from opt_equipment t where t.hk_monitor_code = #{monitorCode}" +
            "  <if test=\"id != null and id != '' \">" +
            "      and t.id != #{id}" +
            "  </if>" +
            "</script>"
    })
    public Boolean checkHkMonitorCode(@Param("id") String id, @Param("monitorCode") String monitorCode);


    /**
     * 描述:导出设备信息
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月1日 下午3:47:51
     * 版本号: V1.0
     */
    @Select({
            " <script>",
            " SELECT",
            " eq.id,",
            " eq.equipment_sn equipmentSn,",
            " eq.equipment_name equipmentName,",
            " cat1.NAME equipmentType,",
            " cat2.NAME equipmentCategory,",
            " ( SELECT NAME FROM sys_category WHERE CODE = eq.equipment_mode ) equipmentModeName,",
            " ( SELECT NAME FROM sys_category WHERE CODE = eq.equipment_specs ) equipmentSpecsName,",
            " cat3.NAME equipmentSection,",
            " eq.equipment_location equipmentLocation,",
            " sup.supplier_name equipmentSupplier,",
            " cat4.NAME equipmentStatus,",
            " eq.equipment_operating equipmentOperating,",
            " eq.equipment_purchase equipmentPurchase,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = eq.equipment_revstop ) AS equipmentRevstopText ",
            " FROM",
            " opt_equipment eq",
            " LEFT JOIN sys_category cat1 ON cat1.CODE = eq.equipment_type",
            " LEFT JOIN sys_category cat2 ON cat2.CODE = eq.equipment_category",
            " LEFT JOIN sys_category cat3 ON cat3.CODE = eq.equipment_section",
            " LEFT JOIN sys_category cat4 ON cat4.CODE = eq.equipment_status",
            " LEFT JOIN opt_supplier sup ON sup.id = eq.equipment_supplier",
            " LEFT JOIN gis_equipment gis ON gis.equipment_id = eq.id",
            " where 1=1",
            " <if test='equipmentSupplier!=null and equipmentSupplier!=\"\"'>",
            "  and eq.equipment_supplier=#{equipmentSupplier}",
            " </if>",
            " <if test='equipmentSn!=null and equipmentSn!=\"\"'>",
            "  and eq.equipment_sn like concat('%',#{equipmentSn},'%')",
            " </if>",
            " <if test='equipmentType!=null and equipmentType!=\"\"'>",
            "  and eq.equipment_type=#{equipmentType}",
            " </if>",
            " <if test='equipmentRevstop!=null and equipmentRevstop!=\"\"'>",
            "  and eq.equipment_revstop=#{equipmentRevstop}",
            " </if>",
            " order by eq.equipment_type desc, eq.equipment_sn",
            " </script>",
    })
    public List<OptEquipmentModel> exportData(@Param("equipmentSupplier") String equipmentSupplier, @Param("equipmentSn") String equipmentSn,
                                              @Param("equipmentType") String equipmentType, @Param("equipmentRevstop") String equipmentRevstop);

    /**
     * 描述: 查询分类列表数据
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月2日 下午9:10:12
     * 版本号: V1.0
     */
    @Select({
            " select name title,code  from sys_category	where pid = (SELECT id from sys_category where code=#{sysCode})"
    })
    public List<SysCateDropdown> getCategoryData(String sysCode);

    /**
     * 描述: 查询供应商数据
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月2日 下午9:10:12
     * 版本号: V1.0
     */
    @Select("SELECT supplier_name supplierName,id FROM opt_supplier t where t.del_flag='0'")
    public List<SupplierListVo> getSupplierData();

    /**
     * 描述: 查询数据字典数据
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月2日 下午9:07:13
     * 版本号: V1.0
     * dictCode 数据分类code
     */
    @Select({
            " SELECT s.item_text title,s.item_value code FROM	sys_dict_item s",
            " WHERE	dict_id = (	select id from sys_dict where dict_code = #{dictCode})"
    })
    public List<DictDropdown> getDictyData(String dictCode);


    /**
     * @author：Roberto
     * @create:2020年3月15日 上午10:54:27
     * 描述:<p></p>
     */
    @Select({
            " <script>",
            "SELECT",
            "	t.id equipmentId,",
            "	concat(b.`name`,'[',c.name,t.equipment_location,']','[',t.equipment_sn,']') equipmentName",
            "FROM",
            "	opt_equipment t",
            "LEFT JOIN sys_category b on t.equipment_type = b.`code`",
            "LEFT JOIN sys_category c on t.equipment_section = c.`code`",
            " where 1=1",
            " <if test='name!=null and name!=\"\"'>",
            "  and t.equipment_sn  like concat('%',#{name},'%')",
            " </if>",
            " </script>"
    })
    public List<EquipmentInfoVO> selectSimpleData(@Param("name") String name);

    /**
     * 描述:<p></p>
     *
     * @param monthBalance 是否周期月结 author 唐超 2020-5-12 10:07:19
     * @author：Roberto
     * @create:2020年3月15日 上午10:54:27
     */
    @Select({
            " <script>",
            "SELECT",
            "	t.id equipmentId,",
            "	concat(b.`name`,'[',c.name,t.equipment_location,']','[',t.equipment_sn,']') equipmentName",
            "FROM",
            "	opt_equipment t",
            "LEFT JOIN sys_category b on t.equipment_type = b.`code`",
            "LEFT JOIN sys_category c on t.equipment_section = c.`code`",
            "LEFT JOIN settle_meter_attr sma on sma.equipment_id = t.id",
            " where 1=1",
            " <if test='name!=null and name!=\"\"'>",
            "  and t.equipment_sn  like concat('%',#{name},'%')",
            " </if>",
            " <if test='typeCode!=null and typeCode!=\"\"'>",
            "  and t.equipment_type =#{typeCode}",
            " </if>",
            " <if test='monthBalance!=null and monthBalance!=\"\"'>",
            "  and IFNULL(sma.month_balance, '" + SettleConstant.EQUIP_NOT_CYCLE + "') = #{monthBalance}",
            " </if>",
            " </script>"
    })
    public List<EquipmentInfoVO> selectSimpleDataByCodeName(@Param("typeCode") String typeCode, @Param("name") String name, @Param("monthBalance") String monthBalance);


    /**
     * @author：Roberto
     * @create:2020年4月7日 下午3:44:24
     * 描述:<p>根据资产id获取资产的详细信息</p>
     */
    @Select({
            "SELECT",
            "	t.equipment_location equipmentLocation,",
            "	t.equipment_name equipmentName,",
            "	b.`name` equipmentType,",
            "	c.name equipmentSection,",
            "	d.name equipmentCategory,",
            "	t.equipment_sn equipmentSn",
            "FROM",
            "	opt_equipment t",
            "LEFT JOIN sys_category b on t.equipment_type = b.`code`",
            "LEFT JOIN sys_category c on t.equipment_section = c.`code`",
            "LEFT JOIN sys_category d on t.equipment_category = d.`code`",
            " where t.id = #{value}",
    })
    public OptEquipment selectDetailById(String equipmentId);


    /**
     * 创建人:Roberto
     * 创建时间:2020年4月23日 下午2:41:51
     * 描述:<p>根据条件查询设备分页数据</p>
     */
    public IPage<OptEquipmentModel> selectPageListByParam(Page<OptEquipmentModel> page, @Param("param") EquipmentQueryVO param);

    /**
     * 获取监控设备列表
     * @param page
     * @return
     */
    @Select({
            "<script>" +
            "SELECT eq.id, eq.equipment_sn equipmentSn, eq.equipment_name equipmentName, " +
                    "eq.hk_monitor_code, eq.equipment_location equipmentLocation, " +
                    "eq.hk_monitor_key " +
                    "FROM opt_equipment eq " +
            "<where> " +
            "   eq.del_flag='0' AND eq.hk_monitor_code IS NOT NULL AND eq.equipment_type = 'A03A17'" +
            "       <if test=\"param.equipmentSn != null and param.equipmentSn != '' \"> " +
            "           AND eq.equipment_sn = #{param.equipmentSn} " +
            "       </if> " +
            "</where>" +
            "</script>"
    })
    public IPage<OptEquipmentModel> getMonitorEquipList(Page<OptEquipmentModel> page, EquipmentQueryVO param);

    public List<OptEquipmentModel> selectPageListByParam(@Param("param") EquipmentQueryVO param);


    /**
     * 创建人:Roberto
     * 创建时间:2020年4月29日 上午3:19:54
     * 描述:<p></p>
     */
    @Select({
            " <script>",
            "SELECT",
            "	t.id equipmentId,",
            "	concat(b.`name`,'[',c.name,t.equipment_location,']','[',t.equipment_sn,']') equipmentName",
            "FROM",
            "	opt_equipment t",
            "LEFT JOIN sys_category b on t.equipment_type = b.`code`",
            "LEFT JOIN sys_category c on t.equipment_section = c.`code`",
            " where 1=1",
            "	and t.id=#{value}",
            " </script>"
    })
    public EquipmentInfoVO selectSimpleDataById(String id);

    /**
     * 查询设备下拉列表
     *
     * @param equipmentSn 设备编号
     * @return
     */
    @Select({
            " <script>",
            " SELECT t.id,CONCAT(t2.`name`,'[',t.equipment_location,'][',t.equipment_sn,']') equipmentSn FROM opt_equipment t LEFT JOIN sys_category t2 on t2.`code`=t.equipment_section where 1=1",
            " <if test='equipmentSn!=null and equipmentSn!=\"\"'>",
            " and t.equipment_name like concat('%',#{equipmentSn},'%')",
            " </if>",
            " </script>",
    })
    List<EquipmentDropdown> equipDropdown(@Param("equipmentSn") String equipmentSn);

    /**
     * 查询未派工设备下拉列表
     *
     * @param equipmentSn 设备编号
     * @return
     */
    @Select({
            " <script>",
            " SELECT t.id,CONCAT(t2.`name`,'[',t.equipment_location,'][',t.equipment_sn,']') equipmentSn FROM opt_equipment t",
            " LEFT JOIN sys_category t2 on t2.`code`=t.equipment_section ",
            " LEFT JOIN opt_upkeep_plan t3 on t3.opt_id=t.id",
            " LEFT JOIN opt_upkeep_plan_record t4 on t4.plan_id=t3.id",
            "where 1=1",
            " and t4.dispatch_status='0' and t4.current='1'",
            " <if test='equipmentSn!=null and equipmentSn!=\"\"'>",
            " and t.equipment_name like concat('%',#{equipmentSn},'%')",
            " </if>",
            " </script>",
    })
    List<EquipmentDropdown> unDispatchEquipmentDropDown(@Param("equipmentSn") String equipmentSn);

    /**
     * 查询设备下拉列表
     *
     * @param equipmentSn 设备编号
     * @return
     */
    @Select({
            " <script>",
            " SELECT t.id,t.id as equipmentId,",
            "CONCAT(t2.`name`,'[',t.equipment_location,'][',t.equipment_sn,']') name,t4.latitude,t4.longitude ",
            " FROM opt_equipment t LEFT JOIN sys_category t2 on t2.`code`=t.equipment_section ",
            " LEFT JOIN gis_equipment t3 on t3.equipment_id=t.id",
            " LEFT JOIN gis_model t4 on t4.id=t3.model_id",
            " where t.del_flag='0'",
            " <if test='equipmentSn!=null and equipmentSn!=\"\"'>",
            " and t.equipment_name like concat('%',#{equipmentSn},'%')",
            " </if>",
            " </script>",
    })
    List<WorkPointDropdown> equipLocationDropdown(@Param("equipmentSn") String equipmentSn);
}
