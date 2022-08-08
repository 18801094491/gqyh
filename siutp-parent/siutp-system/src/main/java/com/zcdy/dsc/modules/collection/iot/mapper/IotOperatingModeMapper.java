package com.zcdy.dsc.modules.collection.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.collection.iot.entity.GisIotEquipEntity;
import com.zcdy.dsc.modules.collection.iot.entity.IotOperateModeEntityDefault;
import com.zcdy.dsc.modules.collection.iot.entity.IotOperatingMode;
import com.zcdy.dsc.modules.collection.iot.vo.OperateEquipInfomation.OperateEquipInfoVo;
import com.zcdy.dsc.modules.collection.iot.vo.OperateEquipPageParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: 工况监控关联设备信息
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-05-21
 * @Version: V1.0
 */
public interface IotOperatingModeMapper extends BaseMapper<IotOperatingMode> {

    /**
     * 查询所有设备的信息
     *
     * @param param
     * @return
     * @author songguang.jiao
     * @date 2020/05/21 15:33:53
     */
    @Select({
            " <script>",
            " SELECT",
            " t.id equipId,",
            " CONCAT(t.equipment_location, '{', t5.name, '}{', t.equipment_sn, '}[', t6.name, ']') as equipName,",
            " t.equipment_sn abbreviation,",
            " (SELECT count(id) from iot_operating_mode t2 where t2.equiment_id=t.id) as selectStatus",
            " FROM",
            " opt_equipment t",
            " LEFT JOIN gis_equipment t3 on t3.equipment_id=t.id",
            " LEFT JOIN gis_model t4 on t4.id = t3.model_id",
            " LEFT JOIN sys_category t5 on t5.code = t.equipment_section",
            " LEFT JOIN sys_category t6 on t6.code = t.equipment_type",
            " where t4.model_type_code!='A03A02'",
            " <if test='param.name!=null and param.name != \"\"'>",
            " and (t.equipment_location like concat('%',#{param.name},'%') or t5.name like concat('%',#{param.name},'%') or t.equipment_sn like concat('%',#{param.name},'%'))",
            " </if>",
            " <if test='param.equipType!=null and param.equipType != \"\"'>",
            " and t.equipment_type =#{param.equipType}",
            " </if>",
            " ORDER BY selectStatus desc,t.equipment_section desc,CONVERT(equipName USING GBK)",
            " </script>",
    })
    IPage<OperateEquipInfoVo> selectAllEquip(Page<OperateEquipInfoVo> page, @Param("param") OperateEquipPageParam param);

    /**
     * 查询监控的设备信息
     *
     * @return
     * @author songguang.jiao
     * @date 2020/05/21 16:53:24
     */
    @Select({
            " SELECT",
            " t4.id equipId,",
            " t4.equipment_img equipImg,",
            " CONCAT(t5.`name`,'-',t4.equipment_name,'[',t6.`name`,']') equipName,",
            " t2.iot_id iotId,",
            " t3.model_id gisId",
            " FROM",
            " iot_operating_mode t",
            " LEFT JOIN iot_opt_equipment t2 on t2.opt_id=t.equiment_id",
            " LEFT JOIN gis_equipment t3 on t3.equipment_id=t.equiment_id",
            " LEFT JOIN opt_equipment t4 on t4.id=t.equiment_id",
            " LEFT JOIN sys_category t5 on t5.`code`=t4.equipment_section",
            " LEFT JOIN sys_category t6 on t6.`code`=t4.equipment_type",
    })
    List<GisIotEquipEntity> selectOperateModeEquip();

    /**
     * 默认地区检测点
     * 通济路2+455主管 的压力点不具有参考价值,把这个数据删除掉(0630号)
     * @return
     */
    @Select({
            " SELECT",
            " t.iot_location_id operateLocationId,",
            " t2.`name` operateLocationName,",
            " t.opt_id equipmentId,",
            " t3.equipment_location equimentLocation,",
            " t3.equipment_img equipImg,",
            " t4.model_id gisId",
            " FROM",
            " iot_operating_location_item t",
            " LEFT JOIN iot_operating_location t2 on t2.id=t.iot_location_id",
            " LEFT JOIN opt_equipment t3 on t3.id=t.opt_id",
            " LEFT JOIN gis_equipment t4 ON t4.equipment_id = t3.id",
            " where t3.id!='1209339776133541890'",
    })
    List<IotOperateModeEntityDefault> selectDefaultModeEquip();
}
