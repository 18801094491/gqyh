package com.zcdy.dsc.modules.collection.iot.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.modules.collection.iot.entity.IotContro;
import com.zcdy.dsc.modules.collection.iot.enums.DataTypeEnum;
import com.zcdy.dsc.modules.collection.iot.mapper.IotControMapper;
import com.zcdy.dsc.modules.collection.iot.service.IotControService;
import com.zcdy.dsc.modules.collection.iot.vo.IotControVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述: 反向控制
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-06
 * 版本号: V1.0
 */
@Service
public class IotControServiceImpl extends ServiceImpl<IotControMapper, IotContro> implements IotControService {
    @Resource
    private IotControMapper iotControMapper;

    @Override
    public IPage<IotControVo> queryPageList(Page<IotContro> page, IotContro iotContro) {
        return iotControMapper.queryPageList(page, iotContro);
    }

    @Override
    public Result<Object> checkDataFormat(String dataType, String variableValue) {
        Result<Object> result=new Result<>();
        if (DataTypeEnum.getByType(dataType).getDataTypeIndentify().indentifyData(variableValue)) {
            result.success();
        } else {
            result.error500("变量值与类型不匹配,而且值必须为正值");
        }
        return result;
    }
}
