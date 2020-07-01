package com.myjdbc.mymongodb.constants;


/**
 * Mongodb常量
 *
 * @Author 陈文
 * @Date 2020/4/23  15:12
 */
public class MongodbConstants {

    /**
     * 是否存在
     */
    public static final String OP_EXISTS = "$exists";
    /**
     * 完全相等
     */
    public static final String OP_EQ = "$eq";
    /**
     * 正则表达式
     */
    public static final String OP_REGEX = "$regex";
    /**
     * 模糊查询
     * （也是使用正则表达式）
     */
    public static final String OP_LIKE = "$regex";
    /**
     * 包含
     */
    public static final String OP_IN = "$in";
    /**
     * 大于
     */
    public static final String OP_GT = "$gt";
    /**
     * 小于
     */
    public static final String OP_LT = "$lt";
    /**
     * 大于等于
     */
    public static final String OP_GE = "$gte";
    /**
     * 小于等于
     */
    public static final String OP_LE = "$lte";
    /**
     * 不等于
     */
    public static final String OP_NEQ = "$ne";

}
