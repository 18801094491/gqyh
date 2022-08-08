package com.zcdy.dsc.modules.settle.event;

import com.alibaba.fastjson.JSONObject;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.configcentre.constant.SystemParamConstant;
import com.zcdy.dsc.modules.configcentre.entity.SystemConfig;
import com.zcdy.dsc.modules.configcentre.service.SystemParamService;
import com.zcdy.dsc.modules.message.constant.EventConstant;
import com.zcdy.dsc.modules.message.util.SendSmsUtil;
import com.zcdy.dsc.modules.settle.entity.Contract;
import com.zcdy.dsc.modules.settle.entity.SettleCustomer;
import com.zcdy.dsc.modules.settle.service.ISettleCustomerService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 合同到期发送短信
 * <b>短信事件编码:CONTRACT_EXPIRE_SMS</b>
 * 
 * @author Roberto
 * @date 2020/05/26
 */
@Component("smsContractExpireEventListener")
public class SmsContractExpireEventListener implements ApplicationListener<ContractExpireEvent> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Resource
    private SystemParamService systemParamService;
    
    @Resource
    private SendSmsUtil sendSmsUtil;
    
    @Resource
    private ISettleCustomerService customerService;

    /* (non-Javadoc)
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(ContractExpireEvent event) {
        // 系统配置，是否需要发送短信
        SystemConfig config =
            this.systemParamService.getConfigByKey(SystemParamConstant.CONTRACT_EXPIRE_SMS_SWITCH.getTitle());
        // 如果没有这个配置，或者这个配置不生效，或者这个配置启用发送短信
        if (null == config || String.valueOf(StatusConstant.STOP).equals(config.getConfigStatus())
            || config.getConfigValue().equals(SystemParamConstant.CONTRACT_EXPIRE_SMS_SWITCH.NO.getValue())) {
            
            if(logger.isWarnEnabled()){
                logger.warn("系统没有配置参数[contract.expire.sms.switch]，自动跳过短信发送程序。\t\n如果需要启用此功能，请配置该参数为1");
            }
            return;
        }
        
        List<Contract> contracts = event.getContracts();
        for(Contract item : contracts){
            reanderAndSendSms(item);
        }
    }
    
    //渲染短信内容
    private void reanderAndSendSms(Contract item){
        //查询合同关联信息
        String cid = item.getCustomerId();
        if(StringUtils.isEmpty(cid)){
            if(logger.isWarnEnabled()){
                logger.warn("合同[编号："+item.getContractSn()+"]-"+item.getId()+"未关联客户信息，系统自动跳过(短信未发送)！");
            }
            return;
        }
        
        if(logger.isDebugEnabled()){
            logger.debug("调用短信接口，发送短信成功……");
        }
        
        //客户信息
        SettleCustomer customer = this.customerService.getById(item.getCustomerId());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy.MM.dd");
        JSONObject dataJson = new JSONObject();
        dataJson.put("customer", customer.getCustomerName());
        dataJson.put("contractName",item.getContractName());
        dataJson.put("startDate",simpleDateFormat.format(item.getStartDate()));
        dataJson.put("overDate",simpleDateFormat.format(item.getEndDate()));
        dataJson.put("contractSN",item.getContractSn());
        sendSmsUtil.sendSms(EventConstant.CONTRACT_EXPIRE_SMS, dataJson);
    }
}
