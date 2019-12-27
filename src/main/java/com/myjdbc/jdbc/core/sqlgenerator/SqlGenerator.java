package com.myjdbc.jdbc.core.sqlgenerator;


import com.myjdbc.jdbc.core.bo.DeleteEntity;
import com.myjdbc.jdbc.core.bo.SavaEntity;
import com.myjdbc.jdbc.core.bo.SavaListEntity;
import com.myjdbc.jdbc.core.service.CriteriaQuery;

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
     * @param cls        实体对象类
     * @param parentFlag 查询语句中包含父对象的属性时为true,否则为flase
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
     * @param parentFlag    查询语句中包含父对象的属性时为true,否则为flase
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


//    /**
//     * @Author 陈文
//     * @Date 2019/12/12  14:53
//     * @Description 查全表SQL
//     */
//    public <T> String findAll(Class<T> cls);

//    /**
//     * 返回列表中所有元素
//     *
//     * @param pageNo   当前页
//     * @param pageSize 每页显示数
//     * @return java.util.List<T>
//     * @author ChenWen
//     * @description 分页展示
//     * @date 2019/7/10 22:40
//     */
//    List<T> findAll(int pageNo, int pageSize);

//    /**
//     * 返回对象数组(根据pojo类中的非null数据去完全匹配，找到全部数据)
//     * 如果pojo中不含主键或其他唯一值，则可能查询出多条数据
//     *
//     * @param po pojo对象
//     * @return
//     */
//    List<T> findAll(T po);
//
//    /**
//     * 返回对象数组(将Map转换成参数，再去匹配该数据)
//     *
//     * @param map key对应数据表中的字段，value对应该字段的值
//     * @return
//     */
//    List<T> findAll(Map<String, Object> map);
//

//
//    /**
//     * 保存对象(主键为null则新增，否则为根据主键Id去修改)
//     *
//     * @param po 要保存的对象
//     */
//    void save(T po);
//
//    /**
//     * 批量保存对象
//     *
//     * @param po 要保存的对象
//     */
//    void save(List<T> po);
//

//

//
//
//    /**
//     * 查询多条数据
//     * 返回List<Object[]>
//     *
//     * @param sql  自定义Sql语句
//     * @param objs 传入的查询参数
//     * @return
//     */
//    List<Object[]> findAllObjects(String sql, Object... objs);
//
//    /**
//     * 查询多条数据
//     * 返回List<Map>
//     *
//     * @param sql  自定义Sql语句
//     * @param objs 传入的查询参数
//     * @return
//     */
//    List<Map<String, Object>> findAllMap(String sql, Object... objs);


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
