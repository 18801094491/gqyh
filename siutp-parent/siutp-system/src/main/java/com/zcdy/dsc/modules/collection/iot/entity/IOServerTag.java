package com.zcdy.dsc.modules.collection.iot.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2020年4月7日 上午10:29:28
 * 描述: <p>IOServer变量信息</p>
 */
@Setter
@Getter
public class IOServerTag {

	private String tagName;
	
	private long dataType;
	
	private String description;
}
