package com.myjdbc.core.criteria;

import com.myjdbc.core.constants.OrderType;
import com.myjdbc.core.entity.Criteria;
import com.myjdbc.core.entity.OrderBo;
import com.myjdbc.core.entity.Pag;

import java.util.List;

/**
 * @Author 陈文
 * @Date 2019/12/27  22:49
 * @return
 * @Description 查询构造器
 */
public interface CriteriaQuery<T> {

    /**
     * 执行 {@literal $eq} 相等运算
     *
     * @param fieldName 限定字段
     * @param value     限定值
     * @Author 陈文
     * @Date 2019/12/8  16:23
     * @Description 字段内容完全等于某个值
     */
    void eq(String fieldName, Object value);

    /**
     * 执行 {@literal $gt} 大于运算
     *
     * @param fieldName 限定字段
     * @param value     限定值
     * @Author 陈文
     * @Date 2020/4/15  10:55
     * @Description 字段内容大于某个值
     */
    void gt(String fieldName, Object value);

    /**
     * 执行 {@literal $lt} 小于运算
     *
     * @param fieldName 限定字段
     * @param value     限定值
     * @Author 陈文
     * @Date 2020/4/15  10:55
     */
    void lt(String fieldName, Object value);

    /**
     * 执行 {@literal $ge} 大于等于运算
     *
     * @param fieldName 限定字段
     * @param value     限定值
     * @Author 陈文
     * @Date 2020/4/15  11:05
     */
    void ge(String fieldName, Object value);

    /**
     * 执行 {@literal $le} 小于等于运算
     *
     * @param fieldName 限定字段
     * @param value     限定值
     * @Author 陈文
     * @Date 2020/4/15  11:05
     */
    void le(String fieldName, Object value);

    /**
     * 执行包含运算，可以理解为：
     * 对多个值，执行 {@literal $eq} 相等运算，只要一个满足即可
     *
     * @param fieldName 限定字段
     * @param value     限定值
     * @Author 陈文
     * @Date 2019/12/8  16:24
     * @Description 字段内容完全等于某一些值
     */
    void in(String fieldName, Object... value);

    /**
     * 模糊匹配
     *
     * @param fieldName 限定字段
     * @param value     限定值
     * @Author 陈文
     * @Date 2019/12/10  11:06
     * @Description 字段内容模糊匹配某个值
     */
    void like(String fieldName, Object value);

    /**
     * 对两个属性的值执行 {@literal $eq} 相等运算
     *
     * @param fieldName  限定字段
     * @param fieldName2 限定字段2
     * @Author 陈文
     * @Date 2019/12/8  16:25
     * @Description 两个字段内容完全相等，例如SELECT * FROM TABLE WHERE TABLE.A=TABLE.B
     */
    void eqProperty(String fieldName, String fieldName2);

    /**
     * 获取排序规则
     *
     * @return
     * @Author 陈文
     * @Date 2020/4/16  18:41
     * @Description
     */
    OrderBo getOrder();

    /**
     * 根据某个字段排序
     *
     * @param orderType  排序规则
     * @param fieldNames 排序字段
     * @Author 陈文
     * @Date 2019/12/8  16:24
     * @Description 根据某个字段排序
     */
    void setOrder(OrderType orderType, String... fieldNames);

    /**
     * 查询构造器所映射的实体类型
     *
     * @return 构造器所映射的实体类型
     * @Author 陈文
     * @Date 2019/12/8  16:28
     * @Description 查询构造器所映射的实体类型
     */
    Class<T> getCls();

    /**
     * 获取查询条件参数
     *
     * @return 条件参数
     * @Author 陈文
     * @Date 2019/12/8  16:31
     * @Description 获取查询条件参数
     */
    Object[] getValues();

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:32
     * @Description 获取查询结果总数
     */
    long getTotal();

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:32
     * @Description 设置查询结果总数
     */
    void setTotal(long total);

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:32
     * @Description 获取分页实体
     */
    Pag getPag();

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:35
     * @Description 设置分页
     */
    void setPag(int page, int rows);

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:35
     * @Description 获取当前页
     */
    int getPage();

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:35
     * @Description 获取每页显示记录数
     */
    int getRows();

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:36
     * @Description 获取用于保存查询结果总数的SQL
     */
    String getSql();

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:36
     * @Description 设置用于保存查询结果总数的SQL
     */
    void setSql(String sql);

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:37
     * @Description 获取拼接的SQL查询条件
     */
    String getSqlString();


    List<Criteria> getCriteriaList();
}
