package com.zcdy.dsc.common.util.security.entity;

import lombok.Data;

/**
 * @author : songguang.jiao
 */
@Data
public class SecurityReq {
    private String data;
    private String pubKey;
    private String signData;
    private String aesKey;
}
