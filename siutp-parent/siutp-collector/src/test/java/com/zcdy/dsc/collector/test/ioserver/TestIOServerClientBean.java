package com.zcdy.dsc.collector.test.ioserver;

import java.io.IOException;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zcdy.dsc.collector.ioserver.IOServerClientBean;

/**
 * @author: Roberto
 * @CreateTime:2019年12月31日 下午3:31:53
 * @Description: <p></p>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestIOServerClientBean {

	@Resource
	private IOServerClientBean ioserverClientBean;
	
	@Test
	public void test() throws IOException{
		int handle = ioserverClientBean.getIoServer().getHandle();
		System.out.println(ioserverClientBean.getIoServer().IOServerIsConnected(handle));
		ioserverClientBean.release();
	}
}
