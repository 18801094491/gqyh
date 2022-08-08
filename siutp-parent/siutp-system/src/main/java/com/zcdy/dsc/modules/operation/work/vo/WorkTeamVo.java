package com.zcdy.dsc.modules.operation.work.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 描述: 班组分页查询实体
 *
 * @author： songguang.jiao
 * 创建时间： 2020年2月10日 下午1:25:02
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "WorkTeamVo", description = "班组分页查询实体")
public class WorkTeamVo {

    /**
     * 班组id
     */
    @ApiModelProperty(value = "班组id")
    private java.lang.String id;
    /**
     * 班组名称
     */
    @Excel(name = "班组名称", width = 15)
    @ApiModelProperty(value = "班组名称")
    private java.lang.String teamName;
    /**
     * 班组描述
     */
    @Excel(name = "班组描述", width = 15)
    @ApiModelProperty(value = "班组描述")
    private java.lang.String teamDescribe;
    /**
     * 有效期始
     */
    @Excel(name = "有效期始", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "有效期始")
    private java.util.Date startTime;
    /**
     * 有效期止
     */
    @Excel(name = "有效期止", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "有效期止")
    private java.util.Date overTime;
    /**
     * 班组状态code
     */
    @ApiModelProperty(value = "班组状态code")
    private java.lang.String teamStatusCode;
    /**
     * 启停用状态名称
     */
    @Excel(name = "班组状态", width = 15)
    @ApiModelProperty(value = "班组状态名称")
    private java.lang.String teamStatusName;
    /**
     * 班次名称
     */
    @Excel(name = "班次名称", width = 15)
    @ApiModelProperty(value = "班次名称")
    private java.lang.String shiftsName;
    /**
     * 班次ID
     */
    @ApiModelProperty(value = "班次ID")
    private java.lang.String shiftsId;
    /**
     * 成员名称
     */
    @Excel(name = "班组人员", width = 15)
    @ApiModelProperty(value = "班组人员")
    private java.lang.String userNames;
    /**
     * 成员用户id
     */
    @ApiModelProperty(value = "班组人员id")
    private java.lang.String userIds;
    /**
     * 组长名称
     */
    @Excel(name = "组长名称", width = 15)
    @ApiModelProperty(value = "组长名称")
    private java.lang.String userManageNames;
    /**
     * 组长id
     */
    @ApiModelProperty(value = "组长id")
    private java.lang.String userManageIds;

}
