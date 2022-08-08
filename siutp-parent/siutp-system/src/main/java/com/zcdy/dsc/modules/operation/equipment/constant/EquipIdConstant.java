package com.zcdy.dsc.modules.operation.equipment.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 描述: 设备id
 * @author: songguang.jiao
 * 创建时间:  2020年4月14日 下午7:25:46
 * 版本: V1.0
 */
public interface EquipIdConstant {

	/**
	 * 河东水厂id  编号: HDZSSFLOW
	 */
	public static final String HDSC="1245658629931659265";
	
	/**
	 * 7标段流量计 07GLL01
	 */
	public static final String GLL_7_01="1209339007074349057";
	
	/**
	 * 2标段流量计 02GLL01
	 */
	public static final String GLL_2_01="1209335863774867457";
	
	/**
	 * 镜河补水 累计正向流量
	 */
	public static final String FZG_WSB05="1306853571244363777";
	
	/**
	 * 华电北燃 流量计
	 */
	public static final String HBDR_WSB01="1298875718557044737";
	
	/**
	 * 入楼的水表集合
	 */
	public static final List<String> METER_RULOU=Arrays.asList(
			"1209349294389243905","1209350802493190145","1209350646637047809","1209340994880843778"
			,"1209350947234426881","1209350575723950081","1209350869199400962","1207594056556843009"
			,"1209350417858736129","1209350348103266306","1209350085925711873","1209349389906128898"
			,"1209349718777311234","1209349618541834241","1209349546760515586","1209349466502508545"
			,"fbacc493289111eaa659d05099cd3eff","fc3e1940289111eaa659d05099cd3eff");
	
	/**
	 * 绿化的水表集合
	 */
	public static final List<String> METER_LVHUA=Arrays.asList(
			"1209350490919317506","1209350282303025154","1209350001850888194","1209349933164965889","1209349866760744962");
}
