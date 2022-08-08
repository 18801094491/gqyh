package com.zcdy.dsc.modules.objecttype.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.centre.vo.CentreTreeObjectVo;
import com.zcdy.dsc.modules.objecttype.entity.SysObject;
import com.zcdy.dsc.modules.objecttype.mapper.SysObjectMapper;
import com.zcdy.dsc.modules.objecttype.service.SysObjectService;
import org.springframework.stereotype.Service;

/**
 * @Description: 对象类型
 * @Author: 在信汇通
 * @Date:   2021-03-05
 * @Version: V1.0
 */
@Service
public class SysObjectServiceImpl extends ServiceImpl<SysObjectMapper, SysObject> implements SysObjectService {

    @Override
    public CentreTreeObjectVo getSysObjectByTreeId(String treeId) {
        return baseMapper.getSysObjectByTreeId(treeId);
    }

    @Override
    public Boolean updateNameById(SysObject sysObject) {
        return baseMapper.updateNameById(sysObject) == 1;
    }
}
