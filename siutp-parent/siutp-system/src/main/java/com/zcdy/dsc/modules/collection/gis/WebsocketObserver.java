package com.zcdy.dsc.modules.collection.gis;

import java.util.Observable;
import java.util.Observer;

import com.zcdy.dsc.common.framework.websocket.dto.WebsocketStatusDTO;
import com.zcdy.dsc.modules.collection.gis.websocket.GISUserWebsocketManager;

/**
 * @author： Roberto
 * 创建时间：2020年3月5日 上午11:33:22
 * 描述: <p>Websocket观察者</p>
 */
public class WebsocketObserver implements Observer {

	/*
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof WebsocketStatusDTO){
			WebsocketStatusDTO dto = (WebsocketStatusDTO)arg;
			if(dto.isOpen()){
				GISUserWebsocketManager.setValue(dto.getUserId(), "on");
			}else{
				GISUserWebsocketManager.remove(dto.getUserId());
			}
		}
	}
}
