package com.zcdy.dsc.modules.settle.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.settle.entity.SettleUseralot;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.settle.vo.SettleUseralotVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述: 用户报装信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-11
 * 版本号: V1.0
 */
public interface SettleUseralotMapper extends BaseMapper<SettleUseralot> {

    IPage<SettleUseralotVo> selectUseralotInfo(Page<SettleUseralotVo> page, @Param("customerName") String customerName,
                                               @Param("startTime") String startTime,
                                               @Param("endTime") String endTime);

    List<SettleUseralotVo> getExportXls( @Param("customerName") String customerName,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime);
}
