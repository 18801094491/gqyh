package com.zcdy.dsc.modules.rdp.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.rdp.entity.RdpMonitorPointConf;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.rdp.param.RdpMonitorPointConfPageParam;
import com.zcdy.dsc.modules.rdp.vo.RdpMonitorPointConfVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 大屏重点监控配置Mapper
 * @Author: 在信汇通
 * @Date:   2020-12-10
 * @Version: V1.0
 */
public interface RdpMonitorPointConfMapper extends BaseMapper<RdpMonitorPointConf> {
    /**
     * 分页查询列表
     * @param page 分页对象
     * @param param 参数对象
     * @return 分页数据对象
     */
    @Select("SELECT id, positio_no, index_code, monitor_name, remarks, hk_monitor_key " +
            "FROM rdp_monitor_point_conf ORDER BY positio_no ASC")
    IPage<RdpMonitorPointConfVo> queryPageList(Page<RdpMonitorPointConfVo> page, @Param("param") RdpMonitorPointConfPageParam param);

    /**
     * 根据id更新监控配置
     * @param monitorPointConf
     * @return
     */
    @Update("UPDATE rdp_monitor_point_conf " +
            "SET index_code = #{monitorPointConf.indexCode}, " +
            "hk_monitor_key = #{monitorPointConf.hkMonitorKey} " +
            "WHERE id = #{monitorPointConf.id}")
    boolean updateMonitorPointConfById(@Param("monitorPointConf") RdpMonitorPointConf monitorPointConf);
}
