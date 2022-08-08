package com.zcdy.dsc.modules.collection.iot.event;

import java.util.Date;

import org.springframework.context.ApplicationEvent;

/**
 * 压力到达某个阈值的时候生成事件
 * 
 * @author Roberto
 * @date 2020/07/02
 */
public class PressLessNumEvent extends ApplicationEvent {

    private static final long serialVersionUID = 6281080572925895846L;

    private String warnValue;

    private Date date;
    
    /**
     * @param source
     */
    public PressLessNumEvent(Object source) {
        super(source);
    }

    public PressLessNumEvent(Object source, String value, Date date) {
        super(source);
        this.warnValue = value;
        this.date = date;
    }

    /**
     * @return the warnValue
     */
    public String getWarnValue() {
        return warnValue;
    }

    /**
     * @param warnValue
     *            the warnValue to set
     */
    public void setWarnValue(String warnValue) {
        this.warnValue = warnValue;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

}
