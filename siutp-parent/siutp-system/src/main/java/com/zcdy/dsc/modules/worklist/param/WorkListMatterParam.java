package com.zcdy.dsc.modules.worklist.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zcdy.dsc.common.api.param.AbstractPageParam;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 工单任务
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@ApiModel(value="work_list_matter", description="工单任务参数列表")
public class WorkListMatterParam extends AbstractPageParam {
    /**提交时间开始*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "提交时间开始")
    private java.util.Date subTimeStart;

    /**提交时间结束*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "提交时间结束")
    private java.util.Date subTimeEnd;

    /**问题状态*/
    @ApiModelProperty(value = "问题状态")
    private String status;
}
