package com.zcdy.dsc.collector.ioserver;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ioserver.dll.IOServerAPICilent;
import com.zcdy.dsc.collector.constant.IOServerStatusConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zrb 获取IOServer连接Bean
 */
@Slf4j
@Component
@Order(1)
public class IOServerClientBean implements InitializingBean {

	@Value("${ioserver_ip:localhost}")
	private String ip;

	@Value("${ioserver_port:12380}")
	private int port;

	private final static IOServerAPICilent ioServerAPICilent = new IOServerAPICilent();

	private Callable<Integer> ioserverConnCallable;

	private ExecutorService exec = Executors.newFixedThreadPool(1);

	public synchronized void connect() throws IOException {
		if(null!=getIoServer()){
			return;
		}
		try {
			ioServerAPICilent.IOServerDisConnect(ioServerAPICilent.getHandle());
		} catch (Exception e) {
			log.warn("重连关闭IOServer连接异常……");
		}
		ioserverConnCallable = new IoserverConnCallable();
		Future<Integer> future = exec.submit(ioserverConnCallable);
		try {
			int result = future.get(1L, TimeUnit.MINUTES);
			if (result == 1) {
				log.info("IOServer连接成功");
			} else {
				log.info("IOServer连接失败");
			}
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			log.warn(e.getMessage());
		}finally {
			//取消call，交由jvm挂断，再次获取需要重新call
			future.cancel(true);
		}
	}

	public IOServerAPICilent getIoServer() {
		if (ioServerAPICilent.IOServerIsConnected(ioServerAPICilent.getHandle())
				&& ioServerAPICilent.getIOServerWorkStatus(ioServerAPICilent.getHandle()) == 0) {
			return ioServerAPICilent;
		} else {
			return null;
		}
	}
	
	/**
	 * @author:Roberto
	 * @create:2020年3月12日 上午11:25:04
	 * @Description:<p>获取IOServer链接状态</p>
	 */
	public int getLinkStatus(){
		return ioServerAPICilent.IOServerIsConnected(ioServerAPICilent.getHandle())?IOServerStatusConstant.LINK_FINE:IOServerStatusConstant.LINK_BAD;
	}
	
	/**
	 * @author:Roberto
	 * @create:2020年3月12日 上午11:25:23
	 * @Description:<p>工作状态</p>
	 */
	public int getWorkStatus(){
		return ioServerAPICilent.getIOServerWorkStatus(ioServerAPICilent.getHandle())==0?IOServerStatusConstant.WORK_GOOD:IOServerStatusConstant.WORK_BAD;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		connect();
		if (log.isInfoEnabled()) {
			log.info("IOServerClientBean初始化成功");
		}
	}

	@PreDestroy
	public void release() {
		if (null != ioServerAPICilent) {
			synchronized (ioServerAPICilent) {
				ioServerAPICilent.IOServerDisConnect(ioServerAPICilent.getHandle());
			}
		}
	}

	private class IoserverConnCallable implements Callable<Integer> {
		@Override
		public Integer call() throws Exception {
			boolean isConnected = false;
			isConnected = ioServerAPICilent.IOServerConnecton(ip, port);
			if (!isConnected) {
				log.error("获取IOServer连接失败，请检查配置或IOServer状态");
				ioServerAPICilent.IOServerDisConnect(ioServerAPICilent.getHandle());
				return 0;
			} else if (!ioServerAPICilent.IOServerIsConnected(ioServerAPICilent.getHandle())
					|| ioServerAPICilent.getIOServerWorkStatus(ioServerAPICilent.getHandle()) != 0) {
				ioServerAPICilent.IOServerDisConnect(ioServerAPICilent.getHandle());
				log.error("初始化IOServer连接状态异常，当前IOServer未连接……");
				return 2;
			}
			return 1;
		}
	}
}
