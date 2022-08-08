package com.zcdy.dsc.common.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zcdy.dsc.common.util.sms.DySmsHelper;
import com.zcdy.dsc.modules.message.handle.impl.EmailSendMsgHandle;

/**
 * 设置静态参数初始化
 * @author Roberto
 * @date 2020/05/27
 */
@Configuration
public class SmsEmailConfig {

    @Value("${system.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${system.sms.accessKeySecret}")
    private String accessKeySecret;

    @Value(value = "${spring.mail.username}")
    private String emailFrom;


    @Bean
    public void initStatic() {
        DySmsHelper.setAccessKeyId(accessKeyId);
        DySmsHelper.setAccessKeySecret(accessKeySecret);
        EmailSendMsgHandle.setEmailFrom(emailFrom);
    }
}
