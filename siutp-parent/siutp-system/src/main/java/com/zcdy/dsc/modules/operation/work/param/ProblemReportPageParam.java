package com.zcdy.dsc.modules.operation.work.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询问题列表
 *
 * @author songguang.jiao
 * @date 2020/6/417:24
 */
@Getter
@Setter
@ApiModel(value = "ProblemReportPageParam")
public class ProblemReportPageParam extends AbstractPageParam {

    /**
     * 问题名称
     */
    @ApiModelProperty(value = "问题名称")
    private java.lang.String problemName;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createUser;

    /**
     * 问题状态
     */
    @ApiModelProperty(value = "问题状态")
    private java.lang.String problemStatus;

    /**
     * 问题类型
     */
    @ApiModelProperty(value = "问题类型code")
    private java.lang.String problemType;

    /**
     * 开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    /**
     * 结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
