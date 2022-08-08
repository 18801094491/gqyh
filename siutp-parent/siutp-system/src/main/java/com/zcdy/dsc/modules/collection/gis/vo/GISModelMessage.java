package com.zcdy.dsc.modules.collection.gis.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2019年12月24日 下午4:53:23
 * 描述: <p>与前端交互的消息体定义</p>
 */
@Setter
@Getter
public class GISModelMessage {

	// 标注消息类型
	private final String cmd = "gis";

	// 消息体
	private MessageBody body;

	@Setter
	@Getter
	@ApiModel(value="MessageBody", description="设备(gis模型)状态数据信息消息体")
	public static final class MessageBody {
		
		/**
		 * 模型更新状态： 1 新增 2修改 3删除
		 */
	    @ApiModelProperty(value="操作类型信息",notes="模型更新状态： 1 新增 2修改 3删除,目前只有2",example="2")
		private Integer status;
		
		//GIS模型的id
	    @ApiModelProperty(value="Gis模型唯一标识",notes="根据标识更新页面模型状态和数据")
		private String id;

		//状态
	    @ApiModelProperty(value="模型状态信息",notes="模型状态信息")
		private OperationSatus opration;
	    
	    /**
	     * APP端列表需要单独展示的采集数据
	     */
	    @ApiModelProperty(value="列表单独展示数据")
	    private String showData;
		
		//采集数据
	    @ApiModelProperty(value="模型数据信息",notes="模型数据信息",dataType="list")
		private List<IotDataVO> data;
	}
}
