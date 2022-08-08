package com.zcdy.dsc.modules.operation.work.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 保存工单入参
 *
 * @author songguang.jiao
 * @date 2020/7/16/0016  15:38:54
 */
@Getter
@Setter
@ApiModel("保存工单入参")
public class RecordParam {
    /**
     * 模板id
     */
    @NotBlank(message = "模板id不能为空")
    @ApiModelProperty("模板id")
    private String tplId;

    /**
     * 用户类型 0-派单人  1-巡检人
     */
    @JsonIgnore
    private String userType;

    /**
     * 计划id
     */
    @JsonIgnore
    private String planId;


    /**
     * 工单id
     */
    @JsonIgnore
    private String workJobId;
    /**
     * 参数值
     */
    @ApiModelProperty("参数值")
    private Map<String, String> mapData;
}
