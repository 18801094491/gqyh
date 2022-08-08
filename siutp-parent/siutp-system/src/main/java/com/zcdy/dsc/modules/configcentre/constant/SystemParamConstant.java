package com.zcdy.dsc.modules.configcentre.constant;

/**
 * @author: Roberto 创建时间:2020年4月23日 下午6:13:01 描述:
 * <p>
 * 系统参数配置信息表
 * </p>
 */
public class SystemParamConstant {

    private SystemParamConstant() {}

    /**
     * 系统参数配置缓存前缀
     */
    public static final String PREFIX = "system:config::";

    /**
     * 是否发送自动关闭事件短信
     */
    public static final String EVENT_ALARM_AUTOCLOSE_SMS = "event.alarm.autoclose.sms";

    /**
     * 客户水表读数变量类型
     */
    public static final String CUSTOMER_METER_FLOW_VARTYPE = "customer.meter.flow.vartype";

    /**
     * 合同倒计时间-(天)
     */
    public static final String CONTRACT_REMIND_REMAINING_DAYS = "contract.remind.remaining.days";

    /**
     * 设备保养提醒倒计时时间-（天）
     */
    public static final String UPKEEPER_REMIND_DAYS ="upkeeper.remind.days";

    /**
     * 是否检测合同到期 [{value:"1",title:"是"},{value:"0",title:"否"}]
     */
    public enum CONTRACT_EXPIRE_CHECK_SWITCH {

        /**
         * 是
         */
        YES("1"),
        /**
         * 否
         */
        NO("0");

        private String value;

        CONTRACT_EXPIRE_CHECK_SWITCH(String value) {
            this.value = value;
        }

        public static String getTitle(){
            return "contract.expire.check.switch";
        }
        
        public String getValue(){
            return this.value;
        }
    };
    
    /**
     * 合同到期是否发送短信 [{value:"1",title:"是"},{value:"0",title:"否"}]
     */
    public enum CONTRACT_EXPIRE_SMS_SWITCH {

        /**
         * 是
         */
        YES("1"),

        /**
         * 否
         */
        NO("0");

        private String value;

        CONTRACT_EXPIRE_SMS_SWITCH(String value) {
            this.value = value;
        }

        public static String getTitle(){
            return "contract.expire.sms.switch";
        }
        
        public String getValue(){
            return this.value;
        }
    };
    
    /**
     * 7标压力恢复线的值
     */
    public static final String PRESS_7_RESUME_POINT_VALUE="press.7.resume.point";
}
