package com.zcdy.dsc.modules.operation.equipment.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2 * @author： 王海东
 * 3 * 创建时间： 2020/3/2 13:42
 * 4
 */
public class EquipmentUtil {

    public List<String> getStrContainData(String str, String start, String end, boolean isSpecial){
        List<String> result = new ArrayList<String>();

        if(isSpecial){
            str=str.replace(start,"startChar").replace(end,"endChar");
            start="startChar";
            end="endChar";
        }

        String regex = start + "(.*?)" + end;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            String key = matcher.group(1);
            if(!key.contains(start) && !key.contains(end)){
                result.add(key);
            }
        }

        return result;
    }
}
