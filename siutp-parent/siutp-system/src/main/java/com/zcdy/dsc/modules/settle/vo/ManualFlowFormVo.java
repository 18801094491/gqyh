package com.zcdy.dsc.modules.settle.vo;

import com.zcdy.dsc.common.exception.BusinessException;
import io.swagger.annotations.Api;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author tangchao
 * @date 2020/5/12
 */
@Api(tags = "手工录入")
@Data
public class ManualFlowFormVo {
    /**
     * 结算周期id, 新增或修改
     */
    private String id;
    /**
     * 设备id
     */
    private String equipmentId;
    /**
     * 抄表日期
     */
    private String currentFlowTime;
    /**
     * 表底数
     */
    private String currentFlow;

    /**
     * 合同水价ID
     */
    private String ruleItemId;
    /**
     * 抄表日期时间
     */
    private String currentFlowDateTime;

    private Date currentFlowDateTimeObj;

    private void setCurrentFlowTime(String currentFlowDate) {
        this.currentFlowTime = currentFlowDate;
        Date currentFlowDateObj = null;
        try {
            currentFlowDateObj = new SimpleDateFormat("yyyy-MM-dd").parse(currentFlowDate);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(currentFlowDateObj);
            cal1.set(Calendar.HOUR_OF_DAY, 23);
            cal1.set(Calendar.MINUTE, 59);
            cal1.set(Calendar.SECOND, 59);
            currentFlowDateObj = cal1.getTime();
            this.currentFlowDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentFlowDateObj);

        } catch (ParseException e) {
            throw new BusinessException(String.format("%s日期格式不正确!", currentFlowDate));
        }
        this.currentFlowDateTimeObj = currentFlowDateObj;
    }


}
