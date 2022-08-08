package com.zcdy.dsc.modules.settle.vo;

import lombok.Data;

/**
 * @author tangchao
 * @date 2020/5/12
 */
@Data
public class SettleCheckQueryParam {
    /**
     * 客户名称模糊查询
     */
    private String likeCustomerName;
}
