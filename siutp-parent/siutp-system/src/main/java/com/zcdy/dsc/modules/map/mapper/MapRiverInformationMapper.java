package com.zcdy.dsc.modules.map.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.map.entity.MapRiverInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.map.param.MapRiverParam;
import com.zcdy.dsc.modules.rdp.vo.RiverVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Description: 地图-河流信息表
 * @Author: 在信汇通
 * @Date:   2020-12-10
 * @Version: V1.0
 */
public interface MapRiverInformationMapper extends BaseMapper<MapRiverInformation> {

    @Select({"<script>\n" +
            "SELECT\n" +
            "river.id,\n" +
            "river.river_name,\n" +
            "river.river_level,\n" +
            "river.river_start_longitude,\n" +
            "river.river_start_latitude,\n" +
            "river.river_end_longitude,\n" +
            "river.river_end_latitude,\n" +
            "river.river_address_info,\n" +
            "river.create_by,\n" +
            "river.create_time,\n" +
            "river.update_by,\n" +
            "river.update_time,\n" +
            "river.active,\n" +
            "river.del_flag,\n" +
            "river.river_style,\n" +
            "river.river_weight,\n" +
            "river.river_color,\n" +
            "river.river_opacity\n" +
            "FROM\n" +
            "map_river_information AS river\n" +
            "WHERE\n" +
            "river.active = 'true' AND\n" +
            "river.del_flag = 0\n" +
            "<if test=\"mapRiverParam.riverName != null and mapRiverParam.riverName != ''\">\n" +
            "    and river.river_name like concat('%',#{mapRiverParam.riverName},'%')\n" +
            "</if>\n" +
            "ORDER BY\n" +
            "river.river_level ASC\n" +
            "</script>"})
    IPage<MapRiverInformation> queryPageData(Page<MapRiverInformation> page,@Param("mapRiverParam") MapRiverParam mapRiverParam);
}
