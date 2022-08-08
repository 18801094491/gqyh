package com.zcdy.dsc.modules.settle.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.configcentre.constant.SystemParamConstant;
import com.zcdy.dsc.modules.configcentre.entity.SystemConfig;
import com.zcdy.dsc.modules.configcentre.service.SystemParamService;
import com.zcdy.dsc.modules.settle.entity.Contract;
import com.zcdy.dsc.modules.settle.event.ContractExpireEvent;
import com.zcdy.dsc.modules.settle.service.IContractService;
import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

/**
 * 每天定时检查合同到期情况,并发送短信
 * 合同到期检查并做相关操作
 * 
 * @author Roberto
 * @date 2020/05/26
 */
public class ContractExpireJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    // 默认提前发送短信天数
    private final int DEFAULT_PREV_DAYS = 30;

    // 参数，提前多少天发送短信
    private String parameter;

    @Resource
    private SystemParamService systemParamService;
    
    @Resource
    private IContractService contractService;

    // 事件发布者
    @Resource
    private ApplicationEventPublisher publisher;

    /* (non-Javadoc)
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 系统配置，是否需要发送短信
        SystemConfig config =
            this.systemParamService.getConfigByKey(SystemParamConstant.CONTRACT_EXPIRE_CHECK_SWITCH.getTitle());
        // 如果有这个配置，并且这个配置生效，而且这个配置启用
        if (null != config && String.valueOf(StatusConstant.RUN).equals(config.getConfigStatus())
            && config.getConfigValue().equals(SystemParamConstant.CONTRACT_EXPIRE_CHECK_SWITCH.YES.getValue())) {
            //处理控制端传来的天前日期的参数
            int days = this.DEFAULT_PREV_DAYS;
            config = this.systemParamService.getConfigByKey(SystemParamConstant.CONTRACT_REMIND_REMAINING_DAYS);
            if (null != config && !StringUtils.isEmpty(config.getConfigValue()) && String.valueOf(StatusConstant.RUN).equals(config.getConfigStatus())){
                try {
                    days = Integer.parseInt(config.getConfigValue());
                    days++;
                } catch (NumberFormatException e) {
                    if(logger.isWarnEnabled()){
                        logger.warn("系统未(或错误)配置参数[contract.remind.remaining.days]，自动放弃，并使用默认配置值[30]，如果需要请使用数字配置此项。");
                    }
                }
            }
            if(!StringUtils.isEmpty(this.parameter)){
                try {
                    days =  Integer.parseInt(this.parameter);
                } catch (NumberFormatException e) {
                    if(logger.isWarnEnabled()){
                        logger.warn("非法的参数传入:"+this.parameter+"，系统自动使用默认参数:"+this.DEFAULT_PREV_DAYS);
                    }
                }
            }
            
            LocalDate today = LocalDate.now();
            LocalDate someDay = today.plusDays(days);
            Date limitDay = Date.from(someDay.atStartOfDay().toInstant(ZoneOffset.of("+8")));
            
            // 检测合同，处理到期合同
            QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
            //只是在提前提醒日期提醒，如果需要变更---------改这里
            queryWrapper.lambda().eq(Contract::getEndDate, limitDay);
            List<Contract> contracts = this.contractService.list(queryWrapper);
            if(contracts.size()>0){
                // 合同到期发布到期事件
                this.publish(contracts);
            }
        }else{
            if(logger.isWarnEnabled()){
                logger.warn("系统没有配置参数[contract.expire.check.switch]，自动跳过合同到期提醒程序。\t\n如果需要启用此功能，请配置该参数为1");
            }
        }
    }

    /**
     * 发布事件
     * 
     * @param contracts
     */
    private void publish(List<Contract> contracts) {
        ContractExpireEvent event = new ContractExpireEvent(this, contracts);
        this.publisher.publishEvent(event);
    }

    /**
     * @param parameter
     *            the parameter to set
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
