package com.myjdbc.core.criteria;

import com.myjdbc.core.constants.OrderType;
import com.myjdbc.core.entity.Criteria;
import com.myjdbc.core.entity.OrderBo;
import com.myjdbc.core.entity.Pag;

import java.util.Map;

/**
 * 查询构造器
 *
 * @Author 陈文
 * @Date 2019/12/27  22:49
 */
public interface CriteriaQuery<T> {

    /**
     * 获取查询实体
     * 该实体在实现构造器时传入
     * 为实现不同数据库实现之间的解耦，对查询实体的操作，由具体数据库业务层来执行
     *
     * @return 查询实体
     */
    T getQueryT();

    /**
     * 执行 {@literal $eq} 相等运算
     *
     * @param fieldName 限定字段
     * @param value     限定值
     * @Description 字段内容完全等于某个值
     */
    void eq(String fieldName, Object value);

    /**
     * 执行 {@literal $gt} 大于运算
     *
     * @param fieldName 限定字段
     * @param value     限定值
     * @Description 字段内容大于某个值
     */
    void gt(String fieldName, Object value);

    /**
     * 执行 {@literal $lt} 小于运算
     *
     * @param fieldName 限定字段
     * @param value     限定值
     */
    void lt(String fieldName, Object value);

    /**
     * 执行 {@literal $ge} 大于等于运算
     *
     * @param fieldName 限定字段
     * @param value     限定值
     */
    void ge(String fieldName, Object value);

    /**
     * 执行 {@literal $le} 小于等于运算
     *
     * @param fieldName 限定字段
     * @param value     限定值
     */
    void le(String fieldName, Object value);

    /**
     * 执行包含运算，可以理解为：
     * 对多个值，执行 {@literal $eq} 相等运算，只要一个满足即可
     *
     * @param fieldName 限定字段
     * @param value     限定值
     * @Description 字段内容完全等于某一些值
     */
    void in(String fieldName, Object... value);

    /**
     * 模糊匹配
     *
     * @param fieldName 限定字段
     * @param value     限定值
     * @Description 字段内容模糊匹配某个值
     */
    void like(String fieldName, Object value);

    /**
     * 对两个属性的值执行 {@literal $eq} 相等运算
     *
     * @param fieldName  限定字段
     * @param fieldName2 限定字段2
     * @Description 两个字段内容完全相等，例如SELECT * FROM TABLE WHERE TABLE.A=TABLE.B
     */
    void eqProperty(String fieldName, String fieldName2);

    /**
     * 指定字段要 等于 空值 Null 或者 空字符串
     *
     * @param fieldName 限定字段
     */
    void isNull(String fieldName);

    /**
     * 指定字段要 不等于 空值 Null 或者 空字符串
     *
     * @param fieldName 限定字段
     */
    void isNotNull(String fieldName);

    /**
     * 获取排序规则
     *
     * @return 排序规则
     */
    OrderBo getOrder();

    /**
     * 根据某个字段排序
     *
     * @param orderType  排序规则
     * @param fieldNames 排序字段
     */
    void setOrder(OrderType orderType, String... fieldNames);

    /**
     * 查询构造器所映射的实体类型
     *
     * @return 构造器所映射的实体类型
     */
    Class<T> getCls();

    /**
     * 获取查询结果总数
     *
     * @return 结果总数
     */
    long getTotal();

    /**
     * 设置查询结果总数
     *
     * @param total 结果总数
     */
    void setTotal(long total);

    /**
     * 获取分页信息
     *
     * @return 当分页信息
     */
    Pag getPag();

    /**
     * 设置分页
     *
     * @param page 当前页
     * @param rows 每页最大记录数
     */
    void setPag(int page, int rows);

    /**
     * 获取当前页
     *
     * @return 当前页
     */
    int getPage();

    /**
     * 获取每页显示记录数
     *
     * @return 每页最大记录数
     */
    int getRows();

    /**
     * 获取查询条件集合
     *
     * @return 查询条件集合
     */
    Map<String, Criteria> getCriteriaMap();
}
