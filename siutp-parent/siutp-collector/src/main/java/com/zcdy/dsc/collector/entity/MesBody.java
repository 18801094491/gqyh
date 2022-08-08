package com.zcdy.dsc.collector.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * body 消息体
 */
@Data
public class MesBody implements Serializable {

	private static final long serialVersionUID = 4356164678148781492L;

	/**
     * 消息子类型
     */
    private Integer subType;

    /**
     * 存放各种消息
     */
    private List<String> context;
}
