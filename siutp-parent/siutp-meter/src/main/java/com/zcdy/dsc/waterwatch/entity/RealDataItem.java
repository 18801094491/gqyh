package com.zcdy.dsc.waterwatch.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Description : 根据大运河名字,获取所有设备实时数据
 * @Version: V1.0
 * @Author : 李永平
 * @Date: 2020-01-15 14:12
 */
@Getter
@Setter
public class RealDataItem  implements Serializable {

    private static final long serialVersionUID = 1L;
	//公司名称
    @JSONField(name="OrgName")
    private  String  orgName;
    //正向
    @JSONField(name="ForValue")
    private  String  forValue;
    //反向
    @JSONField(name="RevValue")
    private  String  revValue;
    //净用
    @JSONField(name="ToValue")
    private  String  toValue;
    //瞬时
    @JSONField(name="RealValue")
    private  String  realValue;
    //压力
    @JSONField(name="PressValue")
    private  String  pressValue;
    //电池电压
    @JSONField(name="CelVal")
    private  String  celVal;
    //状态
    @JSONField(name="CurStatus")
    private  String  curStatus;
    //告警
    @JSONField(name="WarnStatus")
    private  String  warnStatus;
    //抄表时间
    @JSONField(name="CreateTime")
    private  String  createTime;
    //地址编码
    @JSONField(name="AddressCode")
    private  String  addressCode;
    //设备ID
    @JSONField(name="MeterId")
    private  String  meterId;
    //SIM卡号
    @JSONField(name="PhoneNo")
    private  String  phoneNo;
    //用水类型
    @JSONField(name="MUserType")
    private  String  mUserType;
    //类型
    @JSONField(name="MType")
    private  String  mType;
    //名称
    @JSONField(name="MName")
    private  String  mName;
    //安装地址
    @JSONField(name="MInstallAddress")
    private  String  mInstallAddress;
    //户号
    @JSONField(name="MDoorNo")
    private  String  mDoorNo;
    //管径大小
    @JSONField(name="MPipeDn")
    private  String  mPipeDn;
    //管线材质
    @JSONField(name="MMaterial")
    private  String  mMaterial;
    //联系电话
    @JSONField(name="M_Phone")
    private  String  m_Phone;
    //水源井
    @JSONField(name="MSrc")
    private  String  mSrc;
    //排序索引
    @JSONField(name="MSortCode")
    private  String  mSortCode;
}
