package com.zcdy.dsc.common.util.security.entity;

import cn.hutool.json.JSONObject;
import lombok.Data;

/**
 * @author : songguang.jiao
 */
@Data
public class SecurityResp {
    private Boolean success;
    private JSONObject data;
}
