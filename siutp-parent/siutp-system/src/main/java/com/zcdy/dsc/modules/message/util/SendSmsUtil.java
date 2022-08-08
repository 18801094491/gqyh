package com.zcdy.dsc.modules.message.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zcdy.dsc.constant.WorkingStatus;
import com.zcdy.dsc.modules.message.entity.SmsEvent;
import com.zcdy.dsc.modules.message.service.SendMessageHelperService;
import com.zcdy.dsc.modules.message.service.SmsEventService;
import com.zcdy.dsc.modules.message.vo.SendMessageHelperVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * 2 * @Author: 王海东
 * 3 * @Date: 2020/4/13 16:05
 * 4
 */
@Component
@Slf4j
public class SendSmsUtil {

    //产品名称:云通信短信API产品,开发者无需替换
	private  static final String PRODUCT = "Dysmsapi";
    //产品域名,开发者无需替换
	private final String domain = "dysmsapi.aliyuncs.com";

    // 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    @Value("${system.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${system.sms.accessKeySecret}")
    private  String accessKeySecret;
    
    @Autowired
    private SendMessageHelperService sendMessageHelperService;
    
    @Resource
	private SmsEventService smsEventService;
    
    
    /**
     * 描述: 通过短信事件code发送消息
     * @param eventCode 事件code
     * @author:  songguang.jiao
     * 创建时间:  2020年5月9日 上午11:13:55
     * 版本: V1.0
     */
    public void sendSms(String eventCode,JSONObject dataJson) {

    	SmsEvent smsEvent = smsEventService.getOne(Wrappers.lambdaQuery(new SmsEvent()).eq(SmsEvent::getEventCode, eventCode).eq(SmsEvent::getEventStatus, WorkingStatus.WORKING));

    	if(smsEvent==null){
			log.error("短信事件配置错误");
		}else{
			try {
				this.sendSmsDefu(smsEvent.getTemplateId(), dataJson);
			} catch (ClientException e) {
				log.error(e.getErrMsg());
			}
		}
	}

    /**
     * 发送短信接口
     * @param templateParamJson 要发送的数据拼成的json串
     * @param templateId  短信配置的id
     * @return
     * @throws ClientException
     */
    private  void sendSmsDefu(String templateId, JSONObject templateParamJson) throws ClientException {

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //从配置模板获取发送信息，发送人，发送的模板code，发送的key，发送的标签
        SendMessageHelperVo sendMessageHelperVo=sendMessageHelperService.querySendMessageHelperVoById(templateId);
        ArrayList<String> phones=sendMessageHelperVo.getPhones();
        if(phones!=null&&phones.size()>0){
            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(sendMessageHelperVo.getSignName());
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(sendMessageHelperVo.getTemplateCode());
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(templateParamJson.toJSONString());
            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            request.setSmsUpExtendCode("90997");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("yourOutId");
            log.info("发送消息拼装json："+templateParamJson.toJSONString());
            for(String phone:phones){
                request.setPhoneNumbers(phone);
                try {
                    SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
                    if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                        log.info("++短信发送成功+++");
                        log.info("{Code:" + sendSmsResponse.getCode()+",Message:" + sendSmsResponse.getMessage()+",RequestId:"+ sendSmsResponse.getRequestId()+",BizId:"+sendSmsResponse.getBizId()+"}");
                    }
                }
                catch(ClientException e) {
                    log.info("++短信发送失败+++");
                }

            }
        }
    }
}
