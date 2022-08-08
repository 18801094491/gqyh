package com.zcdy.dsc.collector.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * Message 协议体
 */
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息id
     * 格式为：发布者mac地址_时间戳
     */
    private String msg_id;

    /**
     * 应答的目标 消息id
     * 格式为：发布者mac地址_时间戳
     */
    private String callback_id;

    /**
     * msg类型
     * 1 注册消息
     * 2 心跳消息
     * 3功能消息
     */
    private Integer msg_type;

    /**
     * 发布者的mac地址
     */
    private String source_mac;

    /**
     * 发布者类服务类型，可参考《14_mqtt通讯协议》的 2.1款说明
     */
    private String source_type;

    /**
     * 消息生成时的时间戳
     */
    private Long create_time;

    /**
     * 节点授权码（不知道还用不用，原来设计的初衷是留给加密解密用的，现在还没给加密解密的方式）
     */
    private String license;

    /**
     * 消息体 json格式
     */
    private MesBody body;
}
