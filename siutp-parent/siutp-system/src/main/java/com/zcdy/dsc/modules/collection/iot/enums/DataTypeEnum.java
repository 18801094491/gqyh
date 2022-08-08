package com.zcdy.dsc.modules.collection.iot.enums;

import com.zcdy.dsc.modules.collection.iot.service.DataTypeIndentify;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 数据类型校验
 *
 * @author songguang.jiao
 * @date 2020/7/27/0027  11:28:10
 */
public enum DataTypeEnum {
    /**
     * 整形
     */
    INTEGER("0", val -> {
        try {
            if (Integer.parseInt(val) < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }),
    /**
     * 短整形
     */
    SHORT("1", val -> {
        try {
            if (Short.parseShort(val) < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }),

    /**
     * 字符串型
     */
    STRING("2", val -> {
        return true;
    }),
    /**
     * 单精度型
     */
    FLOAT("3", val -> {
        try {
            if (Float.parseFloat(val) < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }),
    /**
     * 双精度型
     */
    DOUBLE("4", val -> {
        try {
            if (Double.parseDouble(val) < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }),

    /**
     * 布尔类型
     */
    BOOLEAN("5", val -> {
        return BooleanUtils.toBoolean(val);
    });

    /**
     * 数据类型
     */
    private String type;
    /**
     * 类型判断接口
     */
    private DataTypeIndentify dataTypeIndentify;

    DataTypeEnum(String type, DataTypeIndentify dataTypeIndentify) {
        this.type = type;
        this.dataTypeIndentify = dataTypeIndentify;
    }

    public static DataTypeEnum getByType(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        for (DataTypeEnum dataTypeEnum : values()) {
            if (type.equals(dataTypeEnum.getType())) {
                return dataTypeEnum;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public DataTypeIndentify getDataTypeIndentify() {
        return dataTypeIndentify;
    }
}