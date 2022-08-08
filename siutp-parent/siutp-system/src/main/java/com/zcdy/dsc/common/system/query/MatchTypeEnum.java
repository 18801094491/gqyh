package com.zcdy.dsc.common.system.query;

import com.zcdy.dsc.common.util.ConvertUtils;

/**
 * 查询链接规则
 * @author : songguang.jiao
 */
public enum MatchTypeEnum {

	/**
	 * 与
	 */
    AND("AND"),
    /**
     * 或
     */
    OR("OR");

    private String value;

    MatchTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MatchTypeEnum getByValue(String value) {
        if (ConvertUtils.isEmpty(value)) {
            return null;
        }
        for (MatchTypeEnum val : values()) {
            if (val.getValue().toLowerCase().equals(value.toLowerCase())) {
                return val;
            }
        }
        return null;
    }
}
