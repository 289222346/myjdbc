package com.myjdbc.core.criteria.util;


import com.myjdbc.core.constants.OpType;
import com.myjdbc.core.model.Criteria;
import com.myjdbc.core.model.Criterion;

/**
 * 限制条件标准生成器
 *
 * @Author 陈文
 * @Date 2020/4/15  11:31
 * @return
 */
public class Restrictions {

    /**
     * 完全相等
     *
     * @param fieldName 限定字段名
     * @param value     限定值
     * @return 字段查询条件
     */
    public static Criteria eq(String fieldName, Object value) {
        return getCriteria(fieldName, OpType.EQ, value);
    }

    /**
     * 大于
     *
     * @param fieldName 限定字段名
     * @param value     限定值
     * @return 字段查询条件
     */
    public static Criteria gt(String fieldName, Object value) {
        return getCriteria(fieldName, OpType.GT, value);
    }

    /**
     * 小于
     *
     * @param fieldName 限定字段名
     * @param value     限定值
     * @return 字段查询条件
     */
    public static Criteria lt(String fieldName, Object value) {
        return getCriteria(fieldName, OpType.LT, value);
    }

    /**
     * 大于等于
     *
     * @param fieldName 限定字段名
     * @param value     限定值
     * @return 字段查询条件
     */
    public static Criteria ge(String fieldName, Object value) {
        return getCriteria(fieldName, OpType.GE, value);
    }

    /**
     * 小于等于
     *
     * @param fieldName 限定字段名
     * @param value     限定值
     * @return 字段查询条件
     */
    public static Criteria le(String fieldName, Object value) {
        return getCriteria(fieldName, OpType.LE, value);
    }

    /**
     * 属性相等
     *
     * @param fieldName   限定字段名
     * @param succedaneum 替代(相等)字段名
     * @return 字段查询条件
     */
    public static Criteria eqProperty(String fieldName, String succedaneum) {
        return getCriteria(fieldName, OpType.EQ_PROPERTY, succedaneum);
    }

    /**
     * 包含
     *
     * @param fieldName 限定字段名
     * @param values    限定值
     * @return 字段查询条件
     */
    public static Criteria in(String fieldName, Object... values) {
        return getCriteria(fieldName, OpType.IN, values);
    }


    /**
     * 等于空值
     *
     * @param fieldName 限定字段名
     * @return 字段查询条件
     */
    public static Criteria isNull(String fieldName) {
        return getCriteria(fieldName, OpType.IS_NULL);
    }

    /**
     * 不等于空值
     *
     * @param fieldName 限定字段名
     * @return 字段查询条件
     */
    public static Criteria isNotNull(String fieldName) {
        return getCriteria(fieldName, OpType.IS_NOT_NULL);
    }


    /**
     * 模糊匹配
     *
     * @param fieldName 限定字段名
     * @param values    限定值
     * @return 字段查询条件
     */
    public static Criteria like(String fieldName, Object... values) {
        return getCriteria(fieldName, OpType.LIKE, values);
    }

    /**
     * 获取字段查询条件
     *
     * @param fieldName 限定字段名
     * @param opType    限定条件
     * @param value     限定值
     * @return 字段查询条件
     */
    private static Criteria getCriteria(String fieldName, OpType opType, Object value) {
        Criterion criterion = new Criterion(opType, value);
        Criteria criteria = new Criteria(fieldName, criterion);
        return criteria;
    }

    /**
     * 获取字段查询条件
     *
     * @param fieldName 限定字段名
     * @param opType    限定条件
     * @param values    限定值
     * @return 字段查询条件
     */
    private static Criteria getCriteria(String fieldName, OpType opType, Object... values) {
        Criterion criterion = new Criterion(opType, values);
        Criteria criteria = new Criteria(fieldName, criterion);
        return criteria;
    }

}
