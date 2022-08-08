package com.zcdy.dsc.common.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 描述: 模拟浏览器发送http请求
 * @author：  songguang.jiao
 * 创建时间： 2020年3月16日 下午9:15:45 
 * 版本号: V1.0
 */
public class HttpUtil {

	//Get请求
	public static String doGet(String url) throws Exception{
		HttpGet httpGet=new HttpGet(url);
		return execute(httpGet);
	}

	//Post请求
	public static String doPost(String url,Map<String, String> param) throws Exception {
		HttpPost httpPost=new HttpPost(url);
		ArrayList<BasicNameValuePair> arrayList=new ArrayList<>();
		Set<String> keySet = param.keySet();
		for (String key : keySet) {
			arrayList.add(new BasicNameValuePair(key, param.get(key)));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		return execute(httpPost);
	}
	
	private static String execute(HttpRequestBase request) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(request);
		if(200==response.getStatusLine().getStatusCode()){
			return EntityUtils.toString(response.getEntity(),Charset.forName("utf-8"));
		}else{
		}
		return "";
	}
	
}
