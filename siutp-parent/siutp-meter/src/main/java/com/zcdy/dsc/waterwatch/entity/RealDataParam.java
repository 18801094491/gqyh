package com.zcdy.dsc.waterwatch.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Description : 请求接口 所需要发送的参数
 * @Version: V1.0
 * @Author : 李永平
 * @Date: 2020-01-15 14:12
 */
@Getter
@Setter
public class RealDataParam implements Serializable{
    private static final long serialVersionUID = 1L;
	//名称
    private  String  org;
    //展示第几页
    private  String  page;;
}
