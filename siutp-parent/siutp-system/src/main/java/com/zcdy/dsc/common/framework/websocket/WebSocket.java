package com.zcdy.dsc.common.framework.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zcdy.dsc.common.framework.websocket.dto.WebsocketStatusDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author： Roberto
 * 创建时间：2020年3月5日 上午11:05:01
 * 描述: <p>Websocket处理器</p>
 */
@Component
@Slf4j
// 定义Websocket访问URL
@ServerEndpoint("/websocket/{userId}")
public class WebSocket extends Observable implements InitializingBean{

	private Session session;

	private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
	private static final Map<String, Session> SESSION_POOL = new HashMap<String, Session>();
	
	private final ThreadLocal<String> threadLocal = new ThreadLocal<>();
	
	//所有的观察者
	@Resource
	private List<Observer> observers = new ArrayList<Observer>();

	/**
	 * @author：Roberto
	 * @create:2020年3月5日 上午10:35:47
	 * 描述:<p>通道打开触发</p>
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam(value = "userId") String userId) {
		try {
			this.session = session;
			webSockets.add(this);
			SESSION_POOL.put(userId, session);
			log.info("【websocket消息】有新的连接，总数为:{}", webSockets.size());
			
			//通知观察者，Websocket关闭了
			setChanged();
			WebsocketStatusDTO dto = new WebsocketStatusDTO();
			dto.setUserId(userId);
			dto.setOpen(true);
			notifyObservers(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author：Roberto
	 * @create:2020年3月5日 上午10:34:23
	 * 描述:<p>通道管理出发</p>
	 */
	@OnClose
	public void onClose() {
		try {
			webSockets.remove(this);
			if(log.isInfoEnabled()){
				log.info("【websocket消息】连接断开，总数为:{}", webSockets.size());
			}

			String userId = threadLocal.get();
			
			//通知观察者，Websocket关闭了
			setChanged();
			WebsocketStatusDTO dto = new WebsocketStatusDTO();
			dto.setUserId(userId);
			dto.setOpen(false);
			notifyObservers(dto);
			
		} catch (Exception e) {
		}finally {
			threadLocal.remove();
		}
	}

	/**
	 * @author：Roberto
	 * @create:2020年3月5日 上午10:34:04
	 * 描述:<p>发生错误时调用</p>
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		if(log.isErrorEnabled()){
			log.error("WS调用error处理->{}",error.getMessage());
		}
	}

	/**
	 * @author：Roberto
	 * @create:2020年3月5日 上午10:33:46
	 * 描述:<p>接收消息处理</p>
	 */
	@OnMessage
	public void onMessage(String message) {
		JSONObject obj = new JSONObject();
		// 业务类型
		obj.put("cmd", "heartcheck");
		// 消息内容
		obj.put("msgTxt", "心跳响应");
		synchronized (session) {
			try {
				session.getBasicRemote().sendText(obj.toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @author：Roberto
	 * @create:2020年3月5日 上午10:18:33
	 * 描述:<p>此为广播消息</p>
	 */
	public void sendAllMessage(String message) {
		if(log.isInfoEnabled()){
			log.info("【websocket消息】广播消息:" + message);
		}
		for (WebSocket webSocket : webSockets) {
			try {
				if (webSocket.session.isOpen()) {
					webSocket.session.getAsyncRemote().sendText(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @author：Roberto
	 * @create:2020年3月5日 上午10:18:51
	 * 描述:<p>单点消息</p>
	 */
	public void sendOneMessage(String userId, String message) {
		Session session = SESSION_POOL.get(userId);
		if (session != null && session.isOpen()) {
			try {
				log.info("【websocket消息】 单点消息:" + message);
				synchronized (session) {
					session.getBasicRemote().sendText(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @author：Roberto
	 * @create:2020年3月5日 上午10:33:09
	 * 描述:<p>单点消息(多人)</p>
	 */
	public void sendMoreMessage(String[] userIds, String message) {
		for (String userId : userIds) {
			Session session = SESSION_POOL.get(userId);
			if (session != null && session.isOpen()) {
				try {
					log.info("【websocket消息】 单点消息:" + message);
					session.getAsyncRemote().sendText(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if(null!=observers && observers.size()>0){
			observers.forEach(item->{
				addObserver(item);
			});
		}
	}
}