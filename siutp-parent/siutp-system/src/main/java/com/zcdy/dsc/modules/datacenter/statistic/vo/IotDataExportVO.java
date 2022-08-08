package com.zcdy.dsc.modules.datacenter.statistic.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author： Roberto
 * 创建时间：2020年3月28日 下午5:46:17
 * 描述: <p></p>
 */
@Getter
@Setter
@ToString
public class IotDataExportVO {

	private Long startTime;
	private Long endTime;
	private List<String> datas;
}
