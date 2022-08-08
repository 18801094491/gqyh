package com.zcdy.dsc.modules.rdp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.rdp.entity.RdpMonitorPointConf;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.rdp.param.RdpMonitorPointConfPageParam;
import com.zcdy.dsc.modules.rdp.vo.RdpMonitorPointConfVo;

/**
 * @Description: 大屏重点监控配置Service
 * @Author: 在信汇通
 * @Date:   2020-12-10
 * @Version: V1.0
 */
public interface RdpMonitorPointConfService extends IService<RdpMonitorPointConf> {
    /**
     * 分页查询列表
     * @param page 分页对象
     * @param param 参数对象
     * @return 分页数据对象
     */
    IPage<RdpMonitorPointConfVo> queryPageList(Page<RdpMonitorPointConfVo> page, RdpMonitorPointConfPageParam param);

    /**
     * 根据id更新监控配置
     * @param monitorPointConf
     * @return
     */
    boolean updateMonitorPointConfById(RdpMonitorPointConf monitorPointConf);
}
