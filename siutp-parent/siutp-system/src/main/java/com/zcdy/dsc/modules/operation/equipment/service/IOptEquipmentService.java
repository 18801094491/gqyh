package com.zcdy.dsc.modules.operation.equipment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.collection.gis.vo.GisLocationVo;
import com.zcdy.dsc.modules.operation.equipment.entity.OptEquipment;
import com.zcdy.dsc.modules.operation.equipment.param.OptEquipmentAddParam;
import com.zcdy.dsc.modules.operation.equipment.vo.EquipmentDropdown;
import com.zcdy.dsc.modules.operation.equipment.vo.EquipmentQueryVO;
import com.zcdy.dsc.modules.operation.equipment.vo.GisVo;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.KnowlegeEquipData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 描述: 设备资产
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-17
 * 版本号: V1.0
 */
public interface IOptEquipmentService extends IService<OptEquipment> {

    /**
     * 分页查询列表
     *
     * @return
     */
    public IPage<OptEquipmentModel> getList(IPage<OptEquipmentModel> page,
                                            @Param("equipmentSupplier") String equipmentSupplier, @Param("equipmentSn") String equipmentSn, String equipmentType, String equipmentRevstop, String equipmentCategory);

    /**
     * 获取资产详情
     *
     * @param id
     * @return
     */
    public OptEquipmentModel getDetail(String id);

    /**
     * 查询gis模型 关联 设备下拉列表
     *
     * @param page
     * @param name
     * @return
     */
    public IPage<GisVo> getGisVoList(IPage<GisVo> page, String name);

    /**
     * gis地图显示位置跟标段信息
     *
     * @author songguang.jiao
     * 2020年1月2日 下午2:53:17
     */
    public GisLocationVo getLocation(String id);

    /**
     * 描述:根据设备类型,规格型号带出维保信息
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月27日 下午4:41:33
     * 版本号: V1.0
     */
    public List<KnowlegeEquipData> queryKnowlegeData(String equipmentTypeCode, String equipmentMode, String equipmentSpecs);


    /**
     * 描述:检验设备编号是否存在
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月29日 下午1:38:33
     * 版本号: V1.0
     */
    public Boolean checkEquipSnExist(String id, String sn);

    /**
     * 校验海康监控设备唯一编码是否存在
     * @param id 设备资产唯一标识
     * @param monitorCode 海康监控设备唯一编码
     * @return
     */
    public Boolean checkHkMonitorCode(String id, String monitorCode);

    /**
     * 描述:导出设备信息
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月1日 下午3:45:26
     * 版本号: V1.0
     */
    public List<OptEquipmentModel> exportData(String equipmentSupplier, String equipmentSn, String equipmentType,
                                              String equipmentRevstop);

    /**
     * 描述: 查询分类列表数据
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月2日 下午9:07:13
     * 版本号: V1.0
     * dictCode 分类code
     */
    Map<String, String> getCategoryData(String sysCode);

    /**
     * 描述: 查询供应商数据
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月2日 下午9:07:13
     * 版本号: V1.0
     */
    Map<String, String> getSupplierData();

    /**
     * 描述: 查询数据字典数据
     *
     * @author： songguang.jiao
     * 创建时间： 2020年3月2日 下午9:07:13
     * 版本号: V1.0
     * dictCode 数据分类code
     */
    Map<String, String> getDictyData(String dictCode);

    /**
     * @author：Roberto
     * @create:2020年4月7日 下午3:42:37
     * 描述:<p>获取资产详细内容</p>
     */
    public OptEquipment getDetailById(String equipmentId);

    /**
     * 创建人:Roberto
     * 创建时间:2020年4月23日 下午2:40:40
     * 描述:<p>根据条件查询设备分页数据</p>
     */
    public IPage<OptEquipmentModel> getPageListByParam(Page<OptEquipmentModel> page, EquipmentQueryVO param);

    /**
     * 获取监控设备列表
     * @param page
     * @return
     */
    public IPage<OptEquipmentModel> getMonitorEquipList(Page<OptEquipmentModel> page, EquipmentQueryVO param);

    public List<OptEquipmentModel> selectPageListByParam(@Param("param") EquipmentQueryVO param);

    /**
     * 描述: 查询设备下拉列表
     *
     * @author： songguang.jiao
     * 创建时间： 2020年2月24日 下午4:43:40
     * 版本号: V1.0
     */
    List<EquipmentDropdown> equipDropdown(String equipmentSn);

    /**
     * 未派工设备列表
     *
     * @return
     */
    List<EquipmentDropdown> unDispatchEquipmentDropDown(String equipmentSn);

    /**
     * 保存设备资产
     *
     * @param param
     */
    void saveOptEquipment(OptEquipmentAddParam param);
}
