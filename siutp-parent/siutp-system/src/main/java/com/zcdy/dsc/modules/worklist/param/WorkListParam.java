package com.zcdy.dsc.modules.worklist.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zcdy.dsc.common.api.param.AbstractPageParam;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 工单信息
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@ApiModel(value="work_list", description="工单信息参数列表")
public class WorkListParam extends AbstractPageParam {
    /**名称*/
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**开始日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期")
    private java.util.Date startDate;
    /**结束日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束日期")
    private java.util.Date overDate;
    /**状态1已创建2部分完成3已完成4未完成*/
    @ApiModelProperty(value = "状态1已创建2部分完成3已完成4未完成")
    private java.lang.String status;
}
