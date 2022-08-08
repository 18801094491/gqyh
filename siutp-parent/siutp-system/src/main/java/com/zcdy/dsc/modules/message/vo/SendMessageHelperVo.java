package com.zcdy.dsc.modules.message.vo;


import lombok.Getter;
import lombok.Setter;

/**
 * 2 * @Author: 王海东
 * 3 * @Date: 2020/4/13 13:51
 * 4
 */
@Getter
@Setter
public class SendMessageHelperVo {
    private String id;
    /**模板名称*/
    private String templateName;
    /**短信模板code*/
    private String templateCode;
    /**短信签名*/
    private String signName;
    /**短信内容模板*/
    private String templateContent;
    /**短信发送人*/
    private java.util.ArrayList<String> phones;
    /**模块id*/
    private String  moduleId;
}
