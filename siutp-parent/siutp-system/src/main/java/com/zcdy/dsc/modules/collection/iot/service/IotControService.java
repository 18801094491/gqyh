package com.zcdy.dsc.modules.collection.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.modules.collection.iot.entity.IotContro;
import com.zcdy.dsc.modules.collection.iot.vo.IotControVo;

/**
 * 描述: 反向控制
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-06
 * 版本号: V1.0
 */
public interface IotControService extends IService<IotContro> {

    /**
     * 分页查询
     * @param page 分页参数
     * @param iotContro 查询参数
     * @return
     */
    IPage<IotControVo> queryPageList(Page<IotContro> page, IotContro iotContro);

    /**
     * 检验数据格式是否符合要求
     * @param dataType 数据类型
     * @param variableValue 数据值
     * @return
     */
    Result<Object> checkDataFormat(String dataType, String variableValue);
}
