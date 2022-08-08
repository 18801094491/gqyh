package com.zcdy.dsc.modules.operation.alarm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.operation.alarm.entity.BusinessWarn;
import com.zcdy.dsc.modules.operation.alarm.mapper.BusinessWarnMapper;
import com.zcdy.dsc.modules.operation.alarm.service.BusinessWarnService;

/**
 * 描述: 报警信息表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-18
 * 版本号: V1.0
 */
@Service
public class BusinessWarnServiceImpl extends ServiceImpl<BusinessWarnMapper, BusinessWarn> implements BusinessWarnService {

    @Override
    public boolean checkExists(QueryWrapper<BusinessWarn> queryWrap) {
         List<Map<String, Object>> selectMaps = this.getBaseMapper().selectMaps(queryWrap);
         if(selectMaps.size()==0) {
             return false;
         }else {
             Map<String, Object> map = selectMaps.get(0);
             return map.containsKey("ex");
         }
    }
}
