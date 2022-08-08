package com.zcdy.dsc.waterwatch.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * @Description : 发送请求工具类
 * @Version: V1.0
 * @Author : 李永平
 * @Date: 2020-01-16 18:15
 */
public class CallApi {

    //发送post请求
    public static String sendPost(String url, String param) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.connect();
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        return result;
    }

    //根据设备的AddressCode 取id
    @Deprecated
    public static String getEquipId(String key) {
        HashMap<String, String> hashMap = new HashMap<>();
        //运河东大街58号院3号楼（原B2地块）  05ZSB01
        hashMap.put("1803160007", "1209350947234426881");

        //达济街9号院1号楼（原B1地块） 05ZSB03
        hashMap.put("1811070003", "1209350869199400962");

        //市政府A2西侧    01ZSB02
        hashMap.put("1803210004", "1209350802493190145");

        //B4地块   01ZSB03
        hashMap.put("1903150005", "1209350646637047809");

        //市政府A2东侧（未开）  05ZSB02   1209350575723950081
        hashMap.put("1903150002", "1209350575723950081");

        //A4西北角绿化    09ZSB01
        hashMap.put("1903150001", "1209350490919317506");

        //A4西北入楼井组   09ZSB02
        hashMap.put("1803210003", "1209350417858736129");

        //A1西配楼井组    09ZSB03
        hashMap.put("1803210008", "1209350348103266306");

        //A4东北侧绿化井组  09ZSB04
        hashMap.put("1709270007", "1209350282303025154");

        //A4东南地块入楼井组  09ZSB05
        hashMap.put("1709270004", "1209350085925711873");

        //A4东南侧绿化井组  09ZSB06
        hashMap.put("1903150004", "1209350001850888194");

        //A3西北侧绿化井组  09ZSB08
        hashMap.put("1709270003", "1209349933164965889");

        //A3西南侧绿化井组 09ZSB09
        hashMap.put("1709270006", "1209349866760744962");

        //A3西南入楼井组  09ZSB10
        hashMap.put("1709270001", "1209349718777311234");

        //A1东配楼井组  09ZSB11
        hashMap.put("1709270008", "1209349618541834241");

        //未知预留井口  09ZSB12
        hashMap.put("1803210006", "1209349546760515586");

        //A3东北角入楼井组  09ZSB13
        hashMap.put("1803210005", "1209349466502508545");

        //A1主楼  09ZSB07
        hashMap.put("1803210001", "1209349389906128898");

        //B3承安路1号院   01ZSB01
        hashMap.put("1808300022", "1209349294389243905");

        //后北营三区  99WSB04
        hashMap.put("1807070009", "fc3e1940289111eaa659d05099cd3eff");

        //警务中心  07ZSB06
        hashMap.put("1808300006", "1207594056556843009");

        //C3  04ZSB02
        hashMap.put("1808300033", "1209340994880843778");

        //丰子沟      99WSB05
        hashMap.put("1803040001", "8888888888888888888");

        //后北营一区1号楼5号楼    99WSB03
        hashMap.put("1807230037", "7777777777777777777");

        //绿色动力     99WSB02
        hashMap.put("1804040014", "6666666666666666666");

        //华电北燃    99WSB01
        hashMap.put("1803040003", "5555555555555555555");
        return hashMap.get(key);
    }

    //根据设备的AddressCode 取设备名称
    @Deprecated
    public static String getEquipName(String key) {
        HashMap<String, String> hashMap = new HashMap<>();
        //运河东大街58号院3号楼（原B2地块）     1209350947234426881
        hashMap.put("1803160007", "05ZSB01");

        //达济街9号院1号楼（原B1地块）    1209350869199400962
        hashMap.put("1811070003", "05ZSB03");

        //市政府A2西侧    1209350802493190145
        hashMap.put("1803210004", "01ZSB02");

        //B4地块       1209350646637047809
        hashMap.put("1903150005", "01ZSB03");

        //市政府A2东侧（未开）     1209350575723950081
        hashMap.put("1903150002", "05ZSB02");

        //A4西北角绿化        1209350490919317506
        hashMap.put("1903150001", "09ZSB01");

        //A4西北入楼井组      1209350417858736129
        hashMap.put("1803210003", "09ZSB02");

        //A1西配楼井组       1209350348103266306
        hashMap.put("1803210008", "09ZSB03");

        //A4东北侧绿化井组     1209350282303025154
        hashMap.put("1709270007", "09ZSB04");

        //A4东南地块入楼井组     1209350085925711873
        hashMap.put("1709270004", "09ZSB05");

        //A4东南侧绿化井组     1209350001850888194
        hashMap.put("1903150004", "09ZSB06");

        //A3西北侧绿化井组     1209349933164965889
        hashMap.put("1709270003", "09ZSB08");

        //A3西南侧绿化井组   1209349866760744962
        hashMap.put("1709270006", "09ZSB09");

        //A3西南入楼井组    1209349718777311234
        hashMap.put("1709270001", "09ZSB10");

        //A1东配楼井组    1209349618541834241
        hashMap.put("1709270008", "09ZSB11");

        //未知预留井口     1209349546760515586
        hashMap.put("1803210006", "09ZSB12");

        //A3东北角入楼井组     1209349466502508545
        hashMap.put("1803210005", "09ZSB13");

        //A1主楼     1209349389906128898
        hashMap.put("1803210001", "09ZSB07");

        //B3承安路1号院      1209349294389243905
        hashMap.put("1808300022", "01ZSB01");

        //后北营三区     fc3e1940289111eaa659d05099cd3eff
        hashMap.put("1807070009", "99WSB04");

        //警务中心     1207594056556843009
        hashMap.put("1808300006", "07ZSB06");

        //C3     1209340994880843778
        hashMap.put("1808300033", "04ZSB02");

        //丰子沟         8888888888888888888
        hashMap.put("1803040001", "99WSB05");

        //后北营一区1号楼5号楼       7777777777777777777
        hashMap.put("1807230037", "99WSB03");

        //绿色动力        6666666666666666666
        hashMap.put("1804040014", "99WSB02");

        //华电北燃       5555555555555555555
        hashMap.put("1803040003", "99WSB01");
        return hashMap.get(key);
    }

}
