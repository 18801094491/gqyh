package com.zcdy.dsc.common.aspect.log;

/**
 * 日志枚举
 * 
 * @author Roberto
 * @date 2020/05/22
 */
public interface LogEnum {

    public enum Type {

        /**
         * 登录
         */
        LOGIN(1),

        /**
         * 操作日志
         */
        OPERATION(2),

        /**
         * 调度
         */
        SHEDULE(3),

        /**
         * 其他
         */
        OTHER(99),
        
        /**
         * 未定义
         */
        UNDEFINED(100);

        private int value;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum Operation {

        /**
         * 查询
         */
        QUERY(1),

        /**
         * 新增
         */
        ADD(2),

        /**
         * 更新
         */
        UPDATE(3),

        /**
         * 删除
         */
        DELETE(4),

        /**
         * 导入
         */
        IMPORT(5),

        /**
         * 导出
         */
        EXPORT(6),

        /**
         * 其它
         */
        OTHER(99),
        
        /**
         * 未定义
         */
        UNDEFINED(100);

        private int value;

        Operation(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}
