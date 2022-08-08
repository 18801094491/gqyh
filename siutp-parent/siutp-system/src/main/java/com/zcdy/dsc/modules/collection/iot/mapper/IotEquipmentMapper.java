package com.zcdy.dsc.modules.collection.iot.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipment;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipmentVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 描述: 模型设备维护
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-26
 * 版本号: V1.0
 */
public interface IotEquipmentMapper extends BaseMapper<IotEquipment> {
    List<IotEquipment> queryIotEquipmentByName(@Param("iotSn")String iotSn, @Param("iotName")String iotName,@Param("id")String id);

    IPage<IotEquipmentVo> queryPageList(Page<IotEquipmentVo> page, @Param("iotEquipment") IotEquipment iotEquipment);

    void updateCycleByCate(@Param("iotCategory") String iotCategory, @Param("iotCycle") String iotCycle);

    @Select({
    	"select id from iot_equipment where iot_category=#{value}"
    })
	List<String> selectIdsByType(String typeCode);

    /**
     * @Description: 通过采集设备id获取标段
     * @Author:  songguang.jiao
     * @Date:  2020年4月9日 下午7:32:17
     * @Version: V1.0
     */
    @Select({
    	" SELECT",
    	" t2.name",
    	" FROM",
    	" iot_opt_equipment t",
    	" LEFT JOIN opt_equipment t1 ON t1.id = t.opt_id",
    	" LEFT JOIN sys_category t2 ON t2.code = t1.equipment_section",
    	" WHERE",
    	" t.iot_id = #{id}",
    })
	String queryEquipmentById(String id);
}
