package com.zcdy.dsc.modules.message.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zcdy.dsc.modules.message.entity.SysMessage;
import com.zcdy.dsc.modules.message.entity.SysMessageTemplate;
import com.zcdy.dsc.modules.message.handle.enums.SendMsgStatusEnum;
import com.zcdy.dsc.modules.message.service.ISysMessageService;
import com.zcdy.dsc.modules.message.service.ISysMessageTemplateService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消息生成工具
 */

@Component
public class PushMsgUtil {

    @Autowired
    private ISysMessageService sysMessageService;

    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;

    /**
     * @param msgType 消息类型 1短信 2邮件 3微信 4系统
     * @param templateCode    消息模板码
     * @param map     消息参数
     * @param sentTo  接收消息方
     */
    public boolean sendMessage(String msgType, String templateCode, Map<String, String> map, String sentTo) {
        List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
        SysMessage sysMessage = new SysMessage();
        if (sysSmsTemplates.size() > 0) {
            SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
            sysMessage.setEsType(msgType);
            sysMessage.setEsReceiver(sentTo);
            //模板标题
            String title = sysSmsTemplate.getTemplateName();
            //模板内容
            String content = sysSmsTemplate.getTemplateContent();
            if(map!=null) {
            	 for (Map.Entry<String, String> entry : map.entrySet()) {
                     String str = "${" + entry.getKey() + "}";
                     title = title.replace(str, entry.getValue());
                     content = content.replace(str, entry.getValue());
                 }
            }
            sysMessage.setEsTitle(title);
            sysMessage.setEsContent(content);
            sysMessage.setEsParam(JSONObject.toJSONString(map));
            sysMessage.setEsSendTime(new Date());
            sysMessage.setEsSendStatus(SendMsgStatusEnum.WAIT.getCode());
            sysMessage.setEsSendNum(0);
            if(sysMessageService.save(sysMessage)) {
				return true;
			}
        }
        return false;
    }
    
    /**
     * 批量插入消息
     * @param msgType 消息类型 1短信 2邮件 3微信 4系统
     * @param templateCode    消息模板码
     * @param map     消息参数
     * @param sentTo  接收消息方  用户id字符串(通过逗号拼接用户id)
     */
    public boolean sendBatchMessage(String msgType, String templateCode, Map<String, String> map, String sentTo) {
    	if(StringUtils.isEmpty(templateCode)){
    		//没有配置模板
    		return false;
    	}
        List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
        if (sysSmsTemplates.size() > 0) {
            SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
            //模板标题
            String title = sysSmsTemplate.getTemplateName();
            //模板内容
            String content = sysSmsTemplate.getTemplateContent();
            if(map!=null) {
            	 for (Map.Entry<String, String> entry : map.entrySet()) {
                     String str = "${" + entry.getKey() + "}";
                     title = title.replace(str, entry.getValue());
                     content = content.replace(str, entry.getValue());
                 }
            }
            if(!StringUtils.isEmpty(sentTo)){
            	String[] users = sentTo.split(",");
            	List<SysMessage> messages=new ArrayList<SysMessage>();
            	for (String user : users) {
            		SysMessage sysMessage = new SysMessage();
            		sysMessage.setEsTitle(title);
            		sysMessage.setEsContent(content);
            		sysMessage.setEsType(msgType);
            		sysMessage.setEsReceiver(user);
            		sysMessage.setEsParam(JSONObject.toJSONString(map));
            		sysMessage.setEsSendTime(new Date());
            		sysMessage.setEsSendStatus(SendMsgStatusEnum.WAIT.getCode());
            		sysMessage.setEsSendNum(0);
            		messages.add(sysMessage);
				}
            	if(sysMessageService.saveBatch(messages)) {
            		return true;
            	}
            }
            
        }
        return false;
    }
}
