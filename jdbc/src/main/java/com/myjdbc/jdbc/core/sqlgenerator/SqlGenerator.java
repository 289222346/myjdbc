package com.myjdbc.jdbc.core.sqlgenerator;


import com.myjdbc.jdbc.core.bo.DeleteEntity;
import com.myjdbc.jdbc.core.bo.SavaEntity;
import com.myjdbc.jdbc.core.bo.SavaListEntity;
import com.myjdbc.jdbc.core.sqlcriteria.CriteriaQuery;

import java.util.List;


/**
 * SQL组装库
 * 本类不直接访问JDBC
 * 本类仅生成拼接后的SQL
 * <p>
 * 实体转换成SQL时，如果有JAP注解，有限遵循JAP
 * 类、属性无注解时，默认属性名转换成大写后，作为字段名
 * 如果没有@Id注解，则默认主键属性名为id,这时如果没有id属性和getId、setId方法，会产生错误
 * <p>
 *
 * @author 陈文
 * @Description
 * @date 2019/7/15 8:52
 * @see com.myjdbc.jdbc.core.service.BaseService 由BaseService调用生成器
 */
public interface SqlGenerator {

    /**
     * @retur 单纯获取记录条数的查询SQL
     * @Author 陈文
     * @Date 2020/3/8  5:51
     * @Description
     */
    String getCount(CriteriaQuery criteriaQuery);

    /**
     * @param cls 实体对象类
     * @return java.lang.String 查询SQL
     * @author 陈文
     * @description
     * @date 2019/7/15 11:48
     */
    public <T> String findById(Class<T> cls);

    /**
     * 返回条件查询语句(条件查询)
     *
     * @param criteriaQuery 查询条件
     * @return String 查询SQL
     * @author 陈文
     * @date 2019/7/15 9:28
     */
    public <T> String findAll(CriteriaQuery criteriaQuery);


    /**
     * @Author 陈文
     * @Date 2019/12/3  19:18
     * @Description 直接传入SQL, 处理后再返回新SQL
     */
    public String findAll(CriteriaQuery criteriaQuery, String sql);

    /**
     * 删除对象(级联删除)
     *
     * @return java.util.List<java.lang.String> List<sql>
     * @author ChenWen
     * @description
     * @date 2019/7/12 11:01
     */
    public <T> DeleteEntity delete(T t);


    /**
     * 返回保存单个对象的语句(主键为null则新增，否则为根据主键Id去修改)
     *
     * @param po
     * @return void
     * @author ChenWen
     * @description
     * @date 2019/7/12 12:04
     */
    public <T> SavaEntity save(T po);

    /**
     * 返回保存多个对象的语句（只能新增）
     *
     * @param po
     * @return
     * @Author 陈文
     * @Date 2019/12/26  16:30
     * @Description 不加注释，反正加了你们也看不懂
     */
    public <T> SavaListEntity save(List<T> po);

}
