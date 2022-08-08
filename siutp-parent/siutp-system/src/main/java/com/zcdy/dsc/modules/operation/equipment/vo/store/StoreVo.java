package com.zcdy.dsc.modules.operation.equipment.vo.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 描述: 仓库返回列表对象
 * @author： songguang.jiao
 * 创建时间： 2020年1月9日 上午11:02:27
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "StoreVo", description = "仓库返回列表对象")
public class StoreVo {

	//id
	@ApiModelProperty(value = "id")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String id;

	//仓库名称
	@ApiModelProperty(value = "仓库名称")
	private String storeName;
	
	//仓库编码
	@ApiModelProperty(value = "仓库编码")
	private String storeSn;
	
	//仓库地址
	@ApiModelProperty(value = "仓库地址")
	private String storePosition;
	
	/**启停用状态 1-启用，0-停用*/
    @ApiModelProperty(value = "启停用状态 1-启用，0-停用")
	private java.lang.String storeStatus;
    
    //启停用状态CODE
    @ApiModelProperty(value = "启停用状态 CODE")
    private String storeStatusCode;
    
	/**仓库管理员*/
    @ApiModelProperty(value = "仓库管理员")
	private java.lang.String manager;
    
    /**仓库管理员code*/
    @ApiModelProperty(value = "仓库管理员code")
	private java.lang.String managerCode;
	
	//创建时间
	@JsonFormat(timezone="CMT+8",pattern="yyyy-MM-dd HH:mm")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

}
