package com.zcdy.dsc.modules.map.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipment;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipmentVo;
import com.zcdy.dsc.modules.map.entity.MapRegionManagement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.map.param.MapRegionParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 地图-区域管理表
 * @Author: 在信汇通
 * @Date: 2020-12-02
 * @Version: V1.0
 */
public interface MapRegionManagementMapper extends BaseMapper<MapRegionManagement> {

    @Select({"<script>\n" +
            "select\n" +
            "id,region_name,region_address_info,region_point_longitude,region_point_latitude,create_by,create_time,update_by,update_time,active,del_flag\n" +
            "from map_region_management\n" +
            "where del_flag = 0\n" +
            "    <if test=\"mapRegionParam.regionName != null and mapRegionParam.regionName != ''\">\n" +
            "      and  region_name like CONCAT('%', #{mapRegionParam.regionName}, '%')\n" +
            "    </if>\n" +
            "</script>"})
    IPage<MapRegionManagement> queryPageList(Page<MapRegionManagement> page, @Param("mapRegionParam") MapRegionParam mapRegionParam);
}
