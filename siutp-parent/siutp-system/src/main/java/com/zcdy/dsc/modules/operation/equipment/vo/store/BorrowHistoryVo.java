package com.zcdy.dsc.modules.operation.equipment.vo.store;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 货品借用历史
 * @author：  songguang.jiao
 * 创建时间： 2020年2月8日 下午3:32:46 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="BorrowHistoryVo",description="货品借用历史")
public class BorrowHistoryVo {
	
	//借用人姓名
	@ApiModelProperty(value="借用人姓名")
	private String userName;
	
	//借用时间
	@ApiModelProperty(value="借用时间")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date borrowTime;
	
	//归还时间
	@ApiModelProperty(value="归还时间")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date backTime;
	
	//备注信息
	@ApiModelProperty(value="备注信息")
	private String description;
}
