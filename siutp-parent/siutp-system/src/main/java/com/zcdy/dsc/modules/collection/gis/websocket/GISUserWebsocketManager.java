package com.zcdy.dsc.modules.collection.gis.websocket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * @author： Roberto
 * 创建时间：2019年12月24日 下午3:30:36
 * 描述: <p>用户开启Websocket管理器</p>
 */
public class GISUserWebsocketManager {
	
	private GISUserWebsocketManager() {
	}

	private static final ConcurrentHashMap<String, String> MANAGER = new ConcurrentHashMap<>();

	public static final String getValue(String userId) {
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		ReadLock readLock = lock.readLock();
		readLock.lock();
		try {
			return MANAGER.get(userId);
		} finally {
			readLock.unlock();
		}
	}

	public static final String setValue(String userId, String value) {
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		WriteLock writeLock = lock.writeLock();
		writeLock.lock();
		try {
			return MANAGER.put(userId, value);
		} finally {
			writeLock.unlock();
		}
	}
	
	public static final void remove(String userId){
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		WriteLock writeLock = lock.writeLock();
		writeLock.lock();
		try {
			MANAGER.remove(userId);
		} finally {
			writeLock.unlock();
		}
	}
	
	public static final boolean exits(String userId){
		return MANAGER.containsKey(userId);
	}
}
