package com.zcdy.dsc.modules.worklist.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatterFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 工单任务附件
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@ApiModel(value="work_list_matter_file", description="工单任务附件参数列表")
public class WorkListMatterFileParam extends AbstractPageParam {
    @ApiModelProperty(value="查询条件，前端调用时要写searchParam.属性=xxx，如：searchParam.name=abc，json格式：{'searchParam.name', 'abc'}")
    private WorkListMatterFile searchParam = new WorkListMatterFile();
}
