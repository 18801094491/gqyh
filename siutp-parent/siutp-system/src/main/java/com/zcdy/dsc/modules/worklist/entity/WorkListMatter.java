package com.zcdy.dsc.modules.worklist.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zcdy.dsc.common.util.StringUtil;
import com.zcdy.dsc.modules.worklist.param.WorkListMatterParam;
import com.zcdy.dsc.modules.worklist.utils.MapUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Description: 工单任务
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@TableName("work_list_matter")
@ApiModel(value="work_list_matter", description="工单任务")
public class WorkListMatter {

	public WorkListMatter()
	{

	}

	public  WorkListMatter(WorkListMatterParam param)
	{
		this.subTimeStart = param.getSubTimeStart();
		this.subTimeEnd = param.getSubTimeEnd();
		this.status = param.getStatus();
	}
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**工单id*/
    @ApiModelProperty(value = "工单id")
	private java.lang.String listId;
	/**标题*/
    @ApiModelProperty(value = "标题")
	private java.lang.String title;
	/**描述*/
    @ApiModelProperty(value = "描述")
	private java.lang.String description;
	/**顺序*/
	@ApiModelProperty(value = "顺序")
	private Integer seq;
	/**类型1巡检2维养3问题*/
    @ApiModelProperty(value = "类型1巡检2维养3问题")
	private java.lang.String type;
	/**问题类型*/
	@ApiModelProperty(value = "问题类型")
	private java.lang.String matterType;
	/**问题等级*/
	@ApiModelProperty(value = "问题等级")
	private java.lang.String matterLevel;
	/**任务经度*/
    @ApiModelProperty(value = "任务经度")
	private java.lang.String matterLongitude;
	/**任务纬度*/
    @ApiModelProperty(value = "任务纬度")
	private java.lang.String matterLatitude;
	/**提交人id*/
    @ApiModelProperty(value = "提交人id")
	private java.lang.String subId;
	/**提交时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "提交时间")
	private java.util.Date subTime;
	/**处理人id*/
    @ApiModelProperty(value = "处理人id")
	private java.lang.String solveId;
	/**处理时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "处理时间")
	private java.util.Date solveTime;
	/**处理经度*/
    @ApiModelProperty(value = "处理经度")
	private java.lang.String solveLongitude;
	/**处理纬度*/
    @ApiModelProperty(value = "处理纬度")
	private java.lang.String solveLatitude;
	/**处理描述*/
    @ApiModelProperty(value = "处理描述")
	private java.lang.String solveDesc;
	/**状态0已取消1已创建2已分配3已完成4未完成*/
    @ApiModelProperty(value = "状态0已取消1已创建2已分配3已完成4未完成")
	private java.lang.String status;
	/**关联设备id*/
    @ApiModelProperty(value = "关联设备id")
	private java.lang.String equipmentId;
	/**下次维养时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "下次维养时间")
	private java.util.Date nextKeepDate;
	/**是否发现问题*/
    @ApiModelProperty(value = "是否发现问题")
	private java.lang.String hasMatter;
	/**问题id*/
    @ApiModelProperty(value = "问题id")
	private java.lang.String matterId;
	/**删除标识0-正常,1-已删除*/
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
	private java.lang.Integer delFlag;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
	private java.lang.String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;

	/**提交时间开始*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "提交时间开始")
	@TableField(exist = false)
	private java.util.Date subTimeStart;

	/**提交时间结束*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "提交时间结束")
	@TableField(exist = false)
	private java.util.Date subTimeEnd;

	/**
	 * 类别描述
	 */
	@ApiModelProperty(value = "类别描述")
	@TableField(exist = false)
	private String typeDes;
	@ApiModelProperty(value = "问题类型描述")
	@TableField(exist = false)
	private java.lang.String matterTypeDes;
	@ApiModelProperty(value = "问题级别描述")
	@TableField(exist = false)
	private java.lang.String matterLevelDes;
	/**
	 * 状态描述
	 */
	@ApiModelProperty(value = "状态描述")
	@TableField(exist = false)
	private String statusDes;
	@ApiModelProperty(value = "提交人姓名")
	@TableField(exist = false)
	private String subName;
	@ApiModelProperty(value = "处理人姓名")
	@TableField(exist = false)
	private String solveName;

	/**关联设备名称*/
	@ApiModelProperty(value = "关联设备名称")
	private java.lang.String equipmentName;

	/**查询条件中类型在数据字典的code*/
	@ApiModelProperty(value = "查询条件中类型在数据字典的code")
	@TableField(exist = false)
	private String queryTypeCode;
	/**查询条件中状态在数据字典的code*/
	@ApiModelProperty(value = "查询条件中状态在数据字典的code")
	@TableField(exist = false)
	private String queryStatusCode;
	/**查询条件中问题类型在数据字典的code*/
	@ApiModelProperty(value = "查询条件中问题类型在数据字典的code")
	@TableField(exist = false)
	private String queryMatterTypeCode;
	/**查询条件中等级在数据字典的code*/
	@ApiModelProperty(value = "查询条件中等级在数据字典的code")
	@TableField(exist = false)
	private String queryMatterLevelCode;
	/**附件列表*/
	@ApiModelProperty(value = "附件列表")
	@TableField(exist = false)
	private List<WorkListMatterFile> fileList;

	@ApiModelProperty(value = "批量操作用")
	@TableField(exist = false)
	private List<String> idList;
	@ApiModelProperty(value = "问题工单类型在数据字典的code，用于更新工单所属问题时的条件")
	@TableField(exist = false)
	private String typeCodeMatter;
	@ApiModelProperty(value = "问题工单状态在数据字典的code，用于更新工单所属问题时的条件")
	@TableField(exist = false)
	private String statusCodeMatter;
	@ApiModelProperty(value = "发现的新问题")
	@TableField(exist = false)
	private WorkListMatter newMatter;

	/**腾讯地图任务经度*/
	@ApiModelProperty(value = "腾讯地图任务经度")
	@TableField(exist = false)
	private java.lang.String matterLongitudeTencent;
	/**腾讯地图任务纬度*/
	@ApiModelProperty(value = "腾讯地图任务纬度")
	@TableField(exist = false)
	private java.lang.String matterLatitudeTencent;
	/**腾讯地图处理经度*/
	@ApiModelProperty(value = "腾讯地图处理经度")
	@TableField(exist = false)
	private java.lang.String solveLongitudeTencent;
	/**腾讯地图处理纬度*/
	@ApiModelProperty(value = "腾讯地图处理纬度")
	@TableField(exist = false)
	private java.lang.String solveLatitudeTencent;

	/**
	 * 获取腾讯地图的任务经度
	 * @return
	 */
	public String getMatterLongitudeTencent()
	{
		//如果不为空直接返回
		if(StringUtils.isNotBlank(matterLongitudeTencent))
			return matterLongitudeTencent;

		//否则，调用腾讯地图API将百度地图坐标进行转换
		if(StringUtils.isNotBlank(matterLongitude) && StringUtils.isNotBlank(matterLatitude))
		{
			//经纬度都不为空才能调用API
			Map<String, Double> location = getTencentLocation(matterLongitude, matterLatitude);
			if(location == null)
			{
				return null;
			}
			this.matterLongitudeTencent = String.valueOf(location.get("lng"));
			this.matterLatitudeTencent = String.valueOf(location.get("lat"));
			return this.matterLongitudeTencent;
		}
		else
		{
			return null;
		}
	}

	/**
	 * 获取腾讯地图的任务纬度
	 * @return
	 */
	public String getMatterLatitudeTencent()
	{
		//如果不为空直接返回
		if(StringUtils.isNotBlank(matterLatitudeTencent))
			return matterLatitudeTencent;

		//否则，调用腾讯地图API将百度地图坐标进行转换
		if(StringUtils.isNotBlank(matterLongitude) && StringUtils.isNotBlank(matterLatitude))
		{
			//经纬度都不为空才能调用API
			Map<String, Double> location = getTencentLocation(matterLongitude, matterLatitude);
			if(location == null)
			{
				return null;
			}
			this.matterLongitudeTencent = String.valueOf(location.get("lng"));
			this.matterLatitudeTencent = String.valueOf(location.get("lat"));
			return this.matterLatitudeTencent;
		}
		else
		{
			return null;
		}
	}

	/**
	 * 获取腾讯地图的解决经度
	 * @return
	 */
	public String getSolveLongitudeTencent()
	{
		//如果不为空直接返回
		if(StringUtils.isNotBlank(solveLongitudeTencent))
			return solveLongitudeTencent;

		//否则，调用腾讯地图API将百度地图坐标进行转换
		if(StringUtils.isNotBlank(solveLongitude) && StringUtils.isNotBlank(solveLatitude))
		{
			//经纬度都不为空才能调用API
			Map<String, Double> location = getTencentLocation(solveLongitude, solveLatitude);
			if(location == null)
			{
				return null;
			}
			this.solveLongitudeTencent = String.valueOf(location.get("lng"));
			this.solveLatitudeTencent = String.valueOf(location.get("lat"));
			return this.solveLongitudeTencent;
		}
		else
		{
			return null;
		}
	}

	/**
	 * 获取腾讯地图的解决纬度
	 * @return
	 */
	public String getSolveLatitudeTencent()
	{
		//如果不为空直接返回
		if(StringUtils.isNotBlank(solveLatitudeTencent))
			return solveLatitudeTencent;

		//否则，调用腾讯地图API将百度地图坐标进行转换
		if(StringUtils.isNotBlank(solveLongitude) && StringUtils.isNotBlank(solveLatitude))
		{
			//经纬度都不为空才能调用API
			Map<String, Double> location = getTencentLocation(solveLongitude, solveLatitude);
			if(location == null)
			{
				return null;
			}
			this.solveLongitudeTencent = String.valueOf(location.get("lng"));
			this.solveLatitudeTencent = String.valueOf(location.get("lat"));
			return this.solveLatitudeTencent;
		}
		else
		{
			return null;
		}
	}

	/**
	 * 将百度地图坐标转为腾讯地图坐标
	 * @param lon
	 * @param lat
	 * @return
	 */
	private Map<String, Double> getTencentLocation(String lon, String lat)
	{
		Map<String, Double> map = MapUtil.baiduMap2TencentMap(lon, lat);
		return map;
	}
}
