package com.zcdy.dsc.common.util;

import com.zcdy.dsc.common.exception.BusinessException;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Description: 处理类的工具类
 * @Author: 在信汇通
 * @Date:   2021-02-20 11:33:45
 * @Version: V1.0
 */
public class ClassUtil
{
    /**
     * 排除的属性
     */
    public static final String[] excludeProperties =
    {
        "id",//id
        "delFlag",//删除标记
        "updateTime",//修改时间
        "updateBy",//修改用户
        "createTime",//创建时间
        "createBy",//创建用户
        "bindStatus",//绑定状态
        "upkeepCount",//设备属性：维保次数，排除原因：查询语句中未包含该属性
        "upkeepTimeBy",//设备属性：最近保养时间，排除原因：查询语句中未包含该属性
        "attrAddParams",//设备属性：设备附属属性列表，排除原因：查询语句中未包含该属性
        "planDate",//设备属性：计划时间，排除原因：查询语句中未包含该属性
        "dispatchStatus",//设备属性：是否派工，排除原因：查询语句中未包含该属性
        "hkMonitorCode",//设备属性：海康监控设备唯一编码，排除原因：查询语句中未包含该属性
        "hkMonitorKey",//设备属性：海康监控平台在配置文件中的唯一标识，排除原因：查询语句中未包含该属性
        "latitude",//设备属性：海康监控平台在配置文件中的唯一标识，排除原因：查询语句中未包含该属性
        "longitude",//设备属性：海康监控平台在配置文件中的唯一标识，排除原因：查询语句中未包含该属性
        "upkeepTimeWx",//设备属性：最近维修时间，排除原因：查询语句中未包含该属性
        "equipmentOperatingEmptyStr",//设备属性：投入运营时间，“空值”查询判断条件，排除原因：非显示字段，只在生成sql时作为判断条件使用
        "equipmentPurchaseEmptyStr",//设备属性：购置时间，“空值”查询判断条件，排除原因：非显示字段，只在生成sql时作为判断条件使用
        "contacterPhone",//客户属性：联系人电话，排除原因：查询语句中未包含该属性
        "contacterName"//客户属性：客户联系人姓名，排除原因：查询语句中未包含该属性
    };

    /**
     * 根据类名获取类的属性，以及在属性上的ApiModelProperty的注解属性
     * @param className
     * @return
     */
    public static List<Map<String, String>> getFieldsAndApiModelProperty(String className) throws BusinessException
    {
        try
        {
            Class cla = Class.forName(className);
            Field[] declaredFields = cla.getDeclaredFields();
            List<Map<String, String>> mapList = new ArrayList<>();
            for(int i = 0; i < declaredFields.length; i++)
            {
                Map<String, String> map = new HashMap<>();
                Field field = declaredFields[i];
                ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                if(annotation != null && !Arrays.asList(excludeProperties).contains(field.getName()))
                {
                    map.put("title", annotation.value());
                    map.put("field", field.getName());
                    mapList.add(map);
                }
            }
            return mapList;
        } catch (ClassNotFoundException e) {
            throw new BusinessException("后台找不到该类名", e);
        }
    }
}
