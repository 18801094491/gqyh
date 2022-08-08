package com.zcdy.dsc.modules.centre.constant;

import java.util.stream.Stream;

public enum CentreTypeEnum
{
    INF("信息中心", "inf"),
    OPT("运营中心", "opt"),
    SEC("安全中心", "sec"),
    MET("计量中心", "met");

    private String text;
    private String value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getTextByValue(String value)
    {
        CentreTypeEnum[] values = CentreTypeEnum.values();
        for(CentreTypeEnum centreTypeEnum : values)
        {
            if(centreTypeEnum.getValue().equals(value))
            {
                return centreTypeEnum.getText();
            }
        }
        return null;
    }

    CentreTypeEnum(String text, String value)
    {
        this.text = text;
        this.value = value;
    }
}
