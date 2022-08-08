package com.zcdy.dsc.constant;

/**
 * 模块编码和名称定义
 * @author Roberto
 * @date 2020/05/22
 */
public enum ModuleConstant {

    /**
     * 根框架
     */
    ROOT("root", "0000", "根节点"),
    
    /**
     * 系统模块
     */
    SYSTEM("system", "root", "系统管理"),
    
    /**
     * 未定义，兼容历史代码
     */
    UNDEFINED("00001", "root", "未定义"),
    
    /**
     * 计量中心模块
     */
    SETTLE("settle", "root", "计量中心"),
    
    /**
     * 采集中心
     */
    COLLECTION("collection", "root", "采集中心"),
    
    /**
     * 数据中心
     */
    DATACENTER("datacenter", "root", "数据中心"),
    
    /**
     * 运营中心
     */
    OPERATION("operation", "root", "运营中心"),
    
    /**
     * 个人中心
     */
    PERSONAL("personal", "root", "个人中心"),
    
    ;
    
    private String code;
    
    private String pcode;
    
    private String name;
    
    private ModuleConstant(String code, String pcode, String name) {
        this.code = code;
        this.name = name;
        this.pcode = pcode;
    }
    
    public String getCode(){
        return this.code;
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getPcode(){
        return this.pcode;
    }
}
