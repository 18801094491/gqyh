package com.zcdy.dsc.common.util.security.entity;

import lombok.Data;

/**
 * @author : songguang.jiao
 */
@Data
public class SecuritySignResp {
    private String data;
    private String signData;
    private String aesKey;
}
