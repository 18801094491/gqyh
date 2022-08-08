package com.zcdy.dsc.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zcdy.dsc.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 @author : songguang.jiao
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty("id")
    private String id;

    /**
     * 用户账号
     */
    @Excel(name = "用户账号", width = 15)
    @ApiModelProperty("用户账号")
    private String username;

    
    /**
     * 真实姓名
     */
    @Excel(name = "真实姓名", width = 15)
    @ApiModelProperty("真实姓名")
    private String realname;

 
    
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * md5密码盐
     */
    @ApiModelProperty("密码盐")
    private String salt;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;
    
    /**
     * 性别（1：男 2：女）
     */
    @Excel(name = "性别", width = 15,dicCode="sex")
    @Dict(dicCode = "sex")
    @ApiModelProperty("性别")
    private Integer sex;

    /**
     * 生日
     */
    @Excel(name = "生日", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("生日")
    private Date birthday;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码", width = 15)
    @ApiModelProperty("手机号码")
    private String phone;

    /**
     * 邮箱
     */
    @Excel(name = "邮箱", width = 15)
    @ApiModelProperty("邮箱")
    private String email;
    
  

    /**
     * 状态(1：正常  2：冻结 ）
     */
    @Excel(name = "状态", width = 15,dicCode="user_status")
    @Dict(dicCode = "user_status")
    @ApiModelProperty(" 状态(1：正常  2：冻结 ）")
    private Integer status;
    
    
    /**
     * 工号，唯一键
     */
    @Excel(name = "工号", width = 15)
    @ApiModelProperty("工号")
    private String workNo;
    
    /**
     * 职务，关联职务表
     */
    @Excel(name = "职务", width = 15)
    @ApiModelProperty("职务")
    private String post;

    /**
     * 座机号
     */
    @Excel(name = "座机号", width = 15)
    @ApiModelProperty("座机号")
    private String telephone;
    
    /**
     * 部门code
     */
    @ApiModelProperty("部门code")
    private String orgCode;

    

    /**
     * 删除状态（0，正常，1已删除）
     */
    @TableLogic
    @ApiModelProperty("删除状态")
    private String delFlag;


    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
     * 同步工作流引擎1同步0不同步
     */
    @ApiModelProperty("同步工作流引擎1同步0不同步")
    private String activitiSync;

    /**
     * 移动端权限
     */
    @ApiModelProperty("移动端权限")
    @TableField("app_role")
    private String appRole;
}
