package com.zcdy.dsc.waterwatch.entity;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: Roberto
 * @CreateTime:2020年1月17日 上午10:11:45
 * @Description: <p></p>
 */
@Setter
@Getter
public class RealDataResult {

	@JSONField(name="Page")
	private Integer page;
	
	@JSONField(name="Flag")
	private Integer flag;
	
	@JSONField(name="Msg")
	private String msg;
	
	@JSONField(name="List")
	private List<RealDataItem> data;
}
