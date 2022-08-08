package com.zcdy.dsc.modules.worklist.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Description: 地图处理工具类
 * @Author: 在信汇通
 * @Date:   2021-03-02
 * @Version: V1.0
 */
@Component
public class MapUtil
{
    private static Logger log = LoggerFactory.getLogger(MapUtil.class);

    private static RestTemplate restTemplate;
    @Resource
    public void setRestTemplate(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    /**
     * 腾讯地图API接口域名
     */
    private static String tencentMapApiDomain;
    @Value("${map.api.tencent.domain}")
    public void setTencentMapApiDomain(String tencentMapApiDomain)
    {
        this.tencentMapApiDomain = tencentMapApiDomain;
    }
    /**
     * 腾讯地图API接口key，认证用
     */
    private static String tencentMapApiKey;
    @Value("${map.api.tencent.key}")
    public void setTencentMapApiKey(String tencentMapApiKey)
    {
        this.tencentMapApiKey = tencentMapApiKey;
    }
    /**
     * 腾讯地图API接口sk，签名用
     */
    private static String tencentMapApiSk;
    @Value("${map.api.tencent.sk}")
    public void setTencentMapApiSk(String tencentMapApiSk)
    {
        this.tencentMapApiSk = tencentMapApiSk;
    }
    /**
     * 腾讯坐标转换接口地址
     */
    private static String tencentMapApiUrlTranslate;
    @Value("${map.api.tencent.url.translate}")
    public void setTencentMapApiUrlTranslate(String tencentMapApiUrlTranslate)
    {
        this.tencentMapApiUrlTranslate = tencentMapApiUrlTranslate;
    }

    /**
     * 百度地图API接口域名
     */
    private static String baiduMapApiDomain;
    @Value("${map.api.baidu.domain}")
    public void setBaiduMapApiDomain(String baiduMapApiDomain)
    {
        this.baiduMapApiDomain = baiduMapApiDomain;
    }
    /**
     * 百度地图API接口ak，认证用
     */
    private static String baiduMapApiAk;
    @Value("${map.api.baidu.ak}")
    public void setBaiduMapApiAk(String baiduMapApiAk)
    {
        this.baiduMapApiAk = baiduMapApiAk;
    }
    /**
     * 百度地图API接口sk，签名用
     */
    private static String baiduMapApiSk;
    @Value("${map.api.baidu.sk}")
    public void setBaiduMapApiSk(String baiduMapApiSk)
    {
        this.baiduMapApiSk = baiduMapApiSk;
    }
    /**
     * 百度地图坐标转换接口地址
     */
    private static String baiduMapApiUrlGeoconv;
    @Value("${map.api.baidu.url.geoconv}")
    public void setBaiduMapApiUrlGeoconv(String baiduMapApiUrlGeoconv)
    {
        this.baiduMapApiUrlGeoconv = baiduMapApiUrlGeoconv;
    }

    /**
     * 通过腾讯地图API接口，将百度地图坐标转为腾讯地图坐标
     * 此方法要求服务所处环境必须能访问互联网
     * @param lng 百度地图的经度
     * @param lat 百度地图的纬度
     * @return {"lng": 116.315052, "lat": 39.932926}
     */
    public static Map<String, Double> baiduMap2TencentMap(String lng, String lat)
    {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("key", tencentMapApiKey);
            map.put("locations", lat + "," + lng);
            map.put("type", 3);
            String sinUrl = splicUrlArgs(tencentMapApiUrlTranslate, map);
            String sig = getTencentSin(sinUrl);//获得签名
            String apiUrl = tencentMapApiDomain + sinUrl + "&sig=" + sig;
            ResponseEntity<Map> object = restTemplate.getForEntity(apiUrl, Map.class);
            List<Map<String, Double>> locations = (List<Map<String, Double>>) object.getBody().get("locations");
            return locations.get(0);
        } catch (Exception e) {
            log.error("调用腾讯地图API出错：", e);
            return null;
        }
    }

    /**
     * 根据参数进行API接口签名
     * @param url
     * @return
     */
    public static String getTencentSin(String url)
    {
        String s = url + tencentMapApiSk;
        String encryptStr = DigestUtils.md5DigestAsHex(s.getBytes());//springboot自带MD5工具
        return encryptStr;
    }

    /**
     * 将url和参数拼接成签名需要的字符串
     * @param url
     * @param map
     * @return
     */
    public static String splicUrlArgs(String url, Map<String, Object> map)
    {
        Set<String> set = map.keySet();
        Object[] str = set.toArray();
        // 将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(str);
        StringBuffer sb = new StringBuffer();
        sb.append(url).append("?");
        // 将三个参数字符串拼接成一个字符串
        for(Object param : str)
        {
            sb.append(param).append("=").append(map.get(param)).append("&");
        }
        String string = sb.substring(0, sb.length() - 1);
        return string;
    }

    /**
     * 对Map内所有value作utf8编码，拼接返回结果
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String toQueryString(Map<?, ?> data) throws UnsupportedEncodingException
    {
        StringBuffer queryString = new StringBuffer();
        for (Map.Entry<?, ?> pair : data.entrySet())
        {
            queryString.append(pair.getKey() + "=");
            String ss[] = pair.getValue().toString().split(",");
            if(ss.length>1)
            {
                //参数值中的逗号不进行编码(encode)
                for(String s:ss)
                {
                    queryString.append(URLEncoder.encode(s,"UTF-8") + ",");
                }
                queryString.deleteCharAt(queryString.length()-1);
                queryString.append("&");
            }
            else
            {
                queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8") + "&");
            }
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }

    /**
     *
     * @param url 接口地址，不含域名，以"/"开始，以"/"结束
     * @param paramsMap 参数列表
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getBaiduSigStr(String url, LinkedHashMap<String, String> paramsMap) throws UnsupportedEncodingException {
        // 调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，拼接返回结果address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourak
        String paramsStr = toQueryString(paramsMap);
        // 对paramsStr前面拼接上/geocoder/v2/?，后面直接拼接yoursk得到/geocoder/v2/?address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourakyoursk
        String wholeStr = new String(url + "?" + paramsStr + baiduMapApiSk);
        // 对上面wholeStr再作utf8编码
        String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
        // 调用下面的MD5方法得到最后的sn签名7de5a22212ffaa9e326444c75a58f9a0
        String s = DigestUtils.md5DigestAsHex(tempStr.getBytes());
        return s;
    }

    /**
     * 腾讯地图坐标转为百度地图坐标
     * @param lng
     * @param lat
     * @return
     */
    public static Map<String, Double> TencentMap2baiduMap(String lng, String lat)
    {
        try {
            // 计算sn跟参数对出现顺序有关，get请求请使用LinkedHashMap保存<key,value>，该方法根据key的插入顺序排序；post请使用TreeMap保存<key,value>，该方法会自动将key按照字母a-z顺序排序。所以get请求可自定义参数顺序（sn参数必须在最后）发送请求，但是post请求必须按照字母a-z顺序填充body（sn参数必须在最后）。以get请求为例：http://api.map.baidu.com/geocoder/v2/?address=百度大厦&output=json&ak=yourak，paramsMap中先放入address，再放output，然后放ak，放入顺序必须跟get请求中对应参数的出现顺序保持一致。
            LinkedHashMap<String, String> paramsMap = new LinkedHashMap<>();
            paramsMap.put("coords", lng + "," + lat);
            paramsMap.put("from", "3");
            paramsMap.put("to", "5");
            paramsMap.put("ak", baiduMapApiAk);
            String sinUrl = toQueryString(paramsMap);
            String sn = getBaiduSigStr(baiduMapApiUrlGeoconv, paramsMap);
            String apiUrl = baiduMapApiDomain + baiduMapApiUrlGeoconv + "?" + sinUrl + "&sn=" + sn;
            ResponseEntity<Map> object = restTemplate.getForEntity(apiUrl, Map.class);
            List<Map<String, Double>> locations = (List<Map<String, Double>>) object.getBody().get("result");
            return locations.get(0);
        }
        catch (Exception e)
        {
            log.error("调用腾讯地图API出错：", e);
            return null;
        }
    }

    /**
     * 计算两点之间的距离
     * @return 米
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2)
    {
        // 纬度
        double lat1 = (Math.PI / 180) * latitude1;
        double lat2 = (Math.PI / 180) * latitude2;
        // 经度
        double lon1 = (Math.PI / 180) * longitude1;
        double lon2 = (Math.PI / 180) * longitude2;
        // 地球半径
        double RE = 6370.856;
        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * RE;
        BigDecimal b = new BigDecimal(d * 1000);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
