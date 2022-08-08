package com.zcdy.dsc.collector.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: Roberto
 * @CreateTime:2020年4月7日 上午10:29:28
 * @Description: <p>IOServer变量信息</p>
 */
@Setter
@Getter
public class IOServerTag {

	private String tagName;
	
	private long dataType;
	
	private String description;
}
