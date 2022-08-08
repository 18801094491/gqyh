package com.zcdy.dsc.modules.operation.upkeep.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 保养日期入参
 *
 * @author songguang.jiao
 * @date 2020/6/518:30
 */
@Getter
@Setter
@ApiModel(value = "KeepPlanParam")
public class KeepPlanParam {
    /**
     * 计划日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "计划日期")
    @NotNull(message = "日期不能为空")
    private Date planDate;

    @ApiModelProperty(value = "资产设备id")
    @NotBlank(message = "资产id不能为空")
    private String optId;
}
