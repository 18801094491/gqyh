package com.zcdy.dsc.modules.collection.iot.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.collection.iot.entity.IotContro;
import com.zcdy.dsc.modules.collection.iot.vo.IotControVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

/**
 * 描述: 反向控制
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-06
 * 版本号: V1.0
 */
public interface IotControMapper extends BaseMapper<IotContro> {

    IPage<IotControVo> queryPageList(Page<IotContro> page, @Param("iotContro") IotContro iotContro);
}
