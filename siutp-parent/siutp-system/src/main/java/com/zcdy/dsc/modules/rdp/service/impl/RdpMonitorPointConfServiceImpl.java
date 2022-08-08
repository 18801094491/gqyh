package com.zcdy.dsc.modules.rdp.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.rdp.entity.RdpMonitorPointConf;
import com.zcdy.dsc.modules.rdp.mapper.RdpMonitorPointConfMapper;
import com.zcdy.dsc.modules.rdp.param.RdpMonitorPointConfPageParam;
import com.zcdy.dsc.modules.rdp.service.RdpMonitorPointConfService;
import com.zcdy.dsc.modules.rdp.vo.RdpMonitorPointConfVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 大屏重点监控配置ServiceImpl
 * @Author: 在信汇通
 * @Date:   2020-12-10
 * @Version: V1.0
 */
@Service
public class RdpMonitorPointConfServiceImpl extends ServiceImpl<RdpMonitorPointConfMapper, RdpMonitorPointConf> implements RdpMonitorPointConfService {
    @Autowired
    private RdpMonitorPointConfMapper monitorPointConfMapper;

    @Override
    public IPage<RdpMonitorPointConfVo> queryPageList(Page<RdpMonitorPointConfVo> page, RdpMonitorPointConfPageParam param) {
        return monitorPointConfMapper.queryPageList(page, param);
    }

    @Override
    public boolean updateMonitorPointConfById(RdpMonitorPointConf monitorPointConf) {
        return monitorPointConfMapper.updateMonitorPointConfById(monitorPointConf);
    }
}
