package com.myjdbc.jdbc.core.service;

import com.myjdbc.jdbc.constants.OrderType;
import com.myjdbc.jdbc.core.bo.Pag;


/**
 * @Author 陈文
 * @Date 2019/12/27  22:49
 * @return
 * @Description 查询构造器
 */
public interface CriteriaQuery<T> {

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:23
     * @Description 字段内容完全等于某个值
     */
    public void eq(String fieldName, Object value);

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:24
     * @Description 字段内容完全等于某一些值
     */
    public void in(String fieldName, Object value);

    /**
     * @Author 陈文
     * @Date 2019/12/10  11:06
     * @Description 字段内容模糊匹配某个值
     */
    public void like(String fieldName, Object value);

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:25
     * @Description 两个字段内容完全相等，例如SELECT * FROM TABLE WHERE TABLE.A=TABLE.B
     */
    public void eqProperty(String fieldName, String fieldName2);

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:28
     * @Description 查询构造器所映射的实体类型
     */
    public Class<T> getCls();

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:31
     * @Description 不加注释，反正加了你们也看不懂
     */
    public Object[] getValues();

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:32
     * @Description 获取查询结果总数
     */
    public int getTotal();

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:32
     * @Description 设置查询结果总数
     */
    public void setTotal(int total);

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:32
     * @Description 获取分页实体
     */
    public Pag getPag();

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:35
     * @Description 设置分页
     */
    public void setPag(int page, int rows);

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:35
     * @Description 获取当前页
     */
    public int getPage();

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:35
     * @Description 获取每页显示记录
     */
    public int getRows();

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:36
     * @Description 获取用于保存查询结果总数的SQL
     */
    public String getSql();

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:36
     * @Description 设置用于保存查询结果总数的SQL
     */
    public void setSql(String sql);

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:37
     * @Description 获取拼接的SQL查询条件
     */
    public String getSqlString();

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:24
     * @Description 根据某个字段排序
     */
    public void setOrder(OrderType orderType, String... fieldNames);


}
