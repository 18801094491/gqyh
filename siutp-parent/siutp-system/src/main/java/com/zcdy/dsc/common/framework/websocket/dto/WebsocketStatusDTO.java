package com.zcdy.dsc.common.framework.websocket.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author： Roberto
 * 创建时间：2020年3月5日 上午10:56:04
 * 描述: <p>用户连接Websocket状态传输对象</p>
 */
@Setter
@Getter
@ToString
public class WebsocketStatusDTO {

	private String userId;
	
	private boolean open;
}
