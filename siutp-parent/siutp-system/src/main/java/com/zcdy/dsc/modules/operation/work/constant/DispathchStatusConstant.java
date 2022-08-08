package com.zcdy.dsc.modules.operation.work.constant;

/**
 * 派工状态
 * @author songguang.jiao
 * @date 2020/6/1711:01
 */
public class DispathchStatusConstant {

    /**
     * 初始化 未派工
     */
    public static final Short INIT = 0;
    /**
     * 已派工
     */
    public static final Short DISPATCH = 1;
    /**
     * 已关闭 未派工直接关闭
      */
    public static final Short CLOSED = 3;
}
