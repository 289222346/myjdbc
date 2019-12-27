package com.myjdbc.jdbc.core.service.impl;


import com.myjdbc.core.util.ClassUtil;
import com.myjdbc.core.util.ListUtil;
import com.myjdbc.jdbc.core.bo.DeleteEntity;
import com.myjdbc.jdbc.core.bo.SavaEntity;
import com.myjdbc.jdbc.core.bo.SavaListEntity;
import com.myjdbc.jdbc.core.dao.Dao;
import com.myjdbc.jdbc.core.dao.impl.DaoImpl;
import com.myjdbc.jdbc.core.service.BaseService;
import com.myjdbc.jdbc.core.service.CriteriaQuery;
import com.myjdbc.jdbc.core.sqlgenerator.SqlGenerator;
import com.myjdbc.jdbc.pool.DBUtil;
import com.myjdbc.jdbc.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("baseService")
public class BaseServiceImpl implements BaseService {
    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Autowired
    private SqlGenerator sqlGenerator;

    @Autowired
    private Dao dao = new DaoImpl();

    protected Connection connection = null;
    private boolean connectionFlag;

    /**
     * @Author 陈文
     * @Date 2019/12/7  11:23
     * @Description 开启事务
     * 注意：开启事务，一定要记得提交或者回滚
     * 默认执行半自动事务处理
     */
    public void transactionStatus() throws SQLException {
        transactionStatus(true);
    }

    /**
     * @param connectionFlag True表示半自动事务处理,false表示全手动事务处理
     * @Author 陈文
     * @Date 2019/12/7  11:29
     * @Description 开启事务
     * 半自动事务处理：回滚或者提交的时候结束事务
     * 全手动事务处理：需要手动结束事务
     * 但即使是半自动方式，没有执行提交或者回滚操作，仍然需要手动关闭事务
     */
    public void transactionStatus(boolean connectionFlag) throws SQLException {
        connection = DBUtil.newConn();
        connection.setAutoCommit(false);
        this.connectionFlag = connectionFlag;
        logger.info("事务开启：" + connectionFlag);
    }

    /**
     * @Author 陈文
     * @Date 2019/12/7  11:27
     * @Description 回滚事务
     */
    public void transactionRollback() throws SQLException {
        if (connection != null) {
            connection.rollback();
            logger.info("事务回滚");
            if (!connection.getAutoCommit() && connectionFlag) {
                transactionClose();
            }
        }
    }

    /**
     * @Author 陈文
     * @Date 2019/12/7  11:34
     * @Description 提交事务
     */
    public void transactionCommit() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit();
            if (connectionFlag) {
                transactionClose();
            }
        } else {
            logger.error("执行手动提交事务时，检测到已开启自动提交。已经友好拦截，请检查代码");
        }

    }

    /**
     * @Author 陈文
     * @Date 2019/12/7  11:37
     * @Description 关闭事务
     */
    public void transactionClose() throws SQLException {
        if (connection != null) {
            connection.setAutoCommit(true);
            connection.close();
            connection = null;
            logger.info("事务关闭");
        }
    }


    @Override
    public <T> void delete(T t) throws Exception {
        List<T> list = new ArrayList<>();
        list.add(t);
        delete(list);
    }


    @Override
    public <T> void delete(List<T> list) throws Exception {
        Connection conn = getConn();
        for (T t : list) {
            delete(conn, t);
        }
        closeConn(conn);
    }

    private <T> void delete(Connection conn, T t) throws Exception {
        DeleteEntity deleteEntity = sqlGenerator.delete(t);
        for (String sql : deleteEntity.getSqlList()) {
            dao.update(conn, sql, new Object[]{deleteEntity.getPkValue()});
        }
    }

    @Override
    public <T> void save(T t) throws Exception {
        SavaEntity savaEntity = sqlGenerator.save(t);
        String sql = savaEntity.getSql();
        Connection conn = getConn();
        dao.update(conn, sql, savaEntity.getObjs().toArray());
        closeConn(conn);
    }

    @Override
    public <T> void save(List<T> t) throws SQLException {
        if (ListUtil.isNotEmpty(t)) {
            SavaListEntity savaListEntity = sqlGenerator.save(t);
            String sql = savaListEntity.getSql();
            Connection conn = getConn();
            dao.batchAdd(conn, sql, savaListEntity.getObjs());

            closeConn(conn);
        }
    }

    @Override
    public List<Integer> getSeq(String seqName, int size) throws SQLException {
        Connection conn = getConn();
        List<Integer> list = dao.getSeq(conn, seqName, size);
        closeConn(conn);
        return list;
    }

    @Override
    public Map<String, Object> findMapById(Class<? extends Object> cls, Serializable id) throws SQLException {
        String sql = sqlGenerator.findById(cls);
        Connection conn = getConn();
        List<Map<String, Object>> list = dao.findMap(conn, sql, id);
        closeConn(conn);
        if (ListUtil.isNotEmpty(list)) {
            Map<String, Object> map = list.get(0);
            map = matchMapAnnotation(cls, map, 0);
            return map;
        }
        return null;
    }

    @Override
    public <T> T findById(Class<T> cls, Serializable id) throws SQLException {
        String sql = sqlGenerator.findById(cls);
        Connection conn = getConn();
        List<T> list = dao.find(conn, sql, cls, id);
        closeConn(conn);
        if (ListUtil.isNotEmpty(list)) {
            T t = matchAnnotation(cls, list.get(0), 0);
            return t;
        }
        return null;
    }

    @Override
    public <T> List<T> criteriaEq(Class<T> cls, String fieldName, Object filedValue) throws SQLException {
        return criteriaEq(cls, fieldName, filedValue, true);
    }


    @Override
    public <T> List<T> criteriaEq(Class<T> cls, String fieldName, Object filedValue, boolean parentFlag) throws SQLException {
        CriteriaQuery criteriaQuery = new CriteriaQueryOracle(cls);
        criteriaQuery.eq(fieldName, filedValue);
        return findAll(criteriaQuery);
    }

    @Override
    public <T> List<T> findAll(Class<T> cls) throws SQLException {
        CriteriaQuery criteriaQuery = new CriteriaQueryOracle(cls);
        return findAll(criteriaQuery);
    }


    @Override
    public <T> List<T> findAll(CriteriaQuery<T> criteriaQuery) {
        return findAllDao(criteriaQuery, 0);
    }


    private <T> List<T> findAllDao(CriteriaQuery<T> criteriaQuery, int findCount) {
        String sql = sqlGenerator.findAll(criteriaQuery);
        Connection conn = getConn();
        Object[] values = criteriaQuery.getValues();
        if (criteriaQuery.getPag() != null) {
            //查询分页总数
            int total = dao.findCount(conn, criteriaQuery.getSql(), values);
            criteriaQuery.setTotal(total);
        }
        //查询数据
//        List<T> list = dao.find(conn, sql, criteriaQuery.getCls(), values);
        //查询数据
        List<T> list = findAll(conn, sql, criteriaQuery, values);
        closeConn(conn);

        //级联查询最多向下三层
        if (ListUtil.isNotEmpty(list) && findCount < 2) {
            //获取一对多属性值
            //获取一对一属性值
            list = matchAnnotation(criteriaQuery.getCls(), list, ++findCount);
        }
        return list;
    }


    private List findAll(Connection conn, String sql, CriteriaQuery cq, Object... values) {
        List list;
        if (cq.getCls() == Map.class) {
            list = dao.findMap(conn, sql, values);
        } else {
            list = dao.find(conn, sql, cq.getCls(), values);
        }
        return list;
    }

    @Override
    public List findAll(CriteriaQuery criteriaQuery, String sql, Object... values) throws SQLException {
        sql = sqlGenerator.findAll(criteriaQuery, sql);
        Connection conn = getConn();
        if (criteriaQuery.getPag() != null) {
            //查询分页总数
            int total = dao.findCount(conn, criteriaQuery.getSql(), values);
            criteriaQuery.setTotal(total);
        }

        //查询数据
        List list = findAll(conn, sql, criteriaQuery, values);

        closeConn(conn);
        return list;
    }

    @Override
    public List<Object[]> findAll(String sql, Object... values) throws SQLException {
        //查询数据
        Connection conn = getConn();
        List<Object[]> list = dao.findObjects(conn, sql, values);
        closeConn(conn);
        return list;
    }

    @Override
    public <T> List<T> findAll(Class<T> cls, String sql, Object... values) throws SQLException {
        Connection conn = getConn();
        List<T> list = dao.find(conn, sql, cls, values);
        closeConn(conn);
        return list;
    }


    @Override
    public <T> List<T> criteriaIn(Class<T> cls, String fieldName, Object[] values) throws SQLException {
        CriteriaQuery criteriaQuery = new CriteriaQueryOracle(cls);
        criteriaQuery.in(fieldName, values);
        return findAll(criteriaQuery);
    }


//    @Override
//    public <T> BaseDao<T> getDao(Class<T> cls) {
//        BaseDao<T> dao;
//        if (DBType.ORACLE.getValue().equals(DB_TYPE)) {
//            new BaseDaoOracle(cls);
//        }
//        return null;
//    }

//    @Override
//    public <T> BaseDao<T> getDao(Class<T> cls, PoolConnection pconn) {
//        BaseDao<T> dao;
//        if (DBType.ORACLE.getValue().equals(DB_TYPE)) {
//            return new BaseDaoOracle(cls, pconn);
//        }
//        return null;
//    }

//    @Override
//    public <T> List<T> finAll(Class<T> cls, int pageNo, int pageSize) {
//        BaseDao dao = getDao(cls);
//        return dao.findAll(pageNo, pageSize);
//    }


//
//    @Override
//    public <T> void delete(List<T> ts) {
//        if (ListUtil.isNotEmpty(ts)) {
//            List<Object[]> values = new ArrayList<>();
//            BaseDao dao = null;
//            for (T t : ts) {
//                if (dao == null) {
//                    dao = new BaseDaoOracle(t.getClass());
//                }
//                Object value = BeanUtil.reflexField(t, "id");
//                if (value != null) {
//                    values.add(new Object[]{value});
//                }
//            }
//            dao.delete(values);
//        }
//    }

//    @Override
//    public <T> Serializable save(T entity) {
//        BaseDao dao = getDao(entity.getClass());
//        dao.save(entity);
//        return null;
//    }
//
//    @Override
//    public <T> Serializable save(List<T> list) {
//        if (ListUtil.isNotEmpty(list)) {
//            BaseDao dao = getDao(list.get(0).getClass());
//            dao.save(list);
//        }
//        return null;
//    }


    private Connection getConn() {
        return connection != null ? connection : DBUtil.newConn();
    }

    private void closeConn(Connection conn) {
        //只对内部构建的Connection进行释放
        //外部传入的由外部处理
        if (!conn.equals(connection)) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private <T> T matchAnnotation(Class<T> cls, T t, int findCount) {
        List<T> list = new ArrayList<>();
        list.add(t);
        return matchAnnotation(cls, list, findCount).get(0);
    }


    private Map<String, Object> matchMapAnnotation(Class<? extends Object> cls, Map<String, Object> map, int findCount) {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map);
        return matchMapAnnotation(cls, list, findCount).get(0);
    }

    private List<Map<String, Object>> matchMapAnnotation(Class<? extends Object> cls, List<Map<String, Object>> list, int findCount) {
        return (List<Map<String, Object>>) matchAnnotation(cls, null, list, findCount);
    }

    private <T> List<T> matchAnnotation(Class<T> cls, List<T> list, int findCount) {
        return (List<T>) matchAnnotation(cls, list, null, findCount);
    }

    /**
     * @param cls       处理的对象类型
     * @param list1     原始集合
     * @param findCount 级联查询深度
     * @return 处理后的集合
     * @Author 陈文
     * @Date 2019/12/13  17:49
     */
    private <T> List<?> matchAnnotation(Class<T> cls, List<T> list1, List<Map<String, Object>> list2, int findCount) {
        Field[] fields = ClassUtil.getAllFields(cls, null);
        List list = null;
        if (list1 != null) {
            //实体形式
            list = list1;
        }
        if (list2 != null) {
            //Map键值对形式
            list = list2;
        }

        //遍历所有属性
        // 暂时不支持延时FetchType延时注解,后期修改为支持，应该修改这里
        for (Field field : fields) {
            //一对多处理
            OneToMany oneToMany = BeanUtil.getOneToMany(cls, field);
            if (oneToMany != null) {
                if (field.getType().equals(List.class)) {
                    // 当前集合的泛型类型
                    Type genericType = field.getGenericType();
                    if (null == genericType) {
                        continue;
                    }
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        // 外键对象
                        Class<?> clsA = (Class<?>) pt.getActualTypeArguments()[0];

                        //被关联的字段(从表)
                        String fkName = oneToMany.mappedBy();
                        //关联字段(主表)
                        String pkName = "id";
                        //有外键映射注解时，修改关联字段
                        JoinColumn joinColumn = BeanUtil.getJoinColumn(cls, field);
                        if (joinColumn != null) {
                            fkName = joinColumn.name();
                            pkName = BeanUtil.getPrimaryName(cls, joinColumn.referencedColumnName());
                        }
                        //子查询关联查找
                        if (list1 != null) {
                            //实体形式
                            list1 = getChildrenA(list1, cls, field, clsA, pkName, fkName, findCount);
                            list = list1;
                        }
                        if (list2 != null) {
                            //键值对形式
                            list2 = getChildreB(list2, cls, field, clsA, pkName, fkName, findCount);
                            list = list2;
                        }
                    }
                }
            } else {
                ManyToOne manyToOne = BeanUtil.getManyToOne(cls, field);
                if (manyToOne != null) {
                    if (list1 != null) {
                        //实体形式
//                        list = getChildrenA(list1, cls, field, clsA, pkName, fkName, findCount);
                        try {
                            //获取属性值的方法
                            Method getMethod1 = BeanUtil.getGetMethod(cls, field);

                            //获取属性的子属性值的方法
                            String referencedColumnName = BeanUtil.getReferencedColumnName(cls, field);
                            Field field1 = ClassUtil.findField(field.getType(), referencedColumnName);
                            Method getMethod2 = BeanUtil.getGetMethod(field.getType(), field1);
                            for (T t : list1) {
                                //多对一属性
                                Object value = getMethod1.invoke(t);
                                //多对一属性的外键
                                if (value == null) {
                                    continue;
                                }
                                Object valueId = getMethod2.invoke(value);
                                //根据外键，查询数据
                                CriteriaQuery criteriaQuery = new CriteriaQueryOracle(field.getType());
                                criteriaQuery.eq(referencedColumnName, valueId);
                                List<?> listA = findAllDao(criteriaQuery, findCount);

                                //将数据重新set进对象
                                if (ListUtil.isNotEmpty(listA)) {
                                    value = listA.get(0);
                                    String setField = BeanUtil.setField(field.getName());
                                    Method setMethod = cls.getMethod(setField, field.getType());
                                    setMethod.invoke(t, value);
                                }
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        list = list1;
                    }
                    if (list2 != null) {
                        //键值对形式
//                        list = getChildreB(list2, cls, field, clsA, pkName, fkName, findCount);
                    }
                }
            }
        }
        return list;
    }


    /**
     * @return
     * @Author 陈文
     * @Date 2019/12/27  13:54
     * @Description 一对多关联查询
     */
    private <T> List<T> getChildrenA(List<T> list1, Class<T> cls, Field field, Class<?> clsA, String pkName, String fkName, int findCount) {
        for (T t : list1) {
            CriteriaQuery<?> criteriaQuery = new CriteriaQueryOracle(clsA);
            Object value = BeanUtil.getPrimaryValue(t, pkName);
            if (value == null) {
                //如果关联查询的值为null,则跳过该关联
                continue;
            }
            criteriaQuery.eq(fkName, value);
            try {
                //一对多关联查询
                List<?> listA = findAllDao(criteriaQuery, findCount);
                Method setMethod = BeanUtil.getSetMethod(cls, field, field.getType());
                setMethod.invoke(t, listA);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return list1;
    }

    private <T> List<Map<String, Object>> getChildreB(List<Map<String, Object>> list2, Class<T> cls, Field field, Class<?> clsA, String pkName, String fkName, int findCount) {
        for (Map<String, Object> map : list2) {
            CriteriaQuery<?> criteriaQuery = new CriteriaQueryOracle(clsA);
            //如果Map里面没有该属性，考虑可能是数据库字段名
            if (!map.containsKey(pkName)) {
                pkName = BeanUtil.getSqlFormatName(cls, pkName);
            }
            Object value = map.get(pkName);
            if (value == null) {
                //如果关联查询的值为null,则跳过该关联
                continue;
            }
//            fkName = BeanUtil.getPrimaryName(cls, fkName);
            criteriaQuery.eq(fkName, value);
            //一对多关联查询
            List<Map<String, Object>> listA = (List<Map<String, Object>>) findAllDao(criteriaQuery, findCount);
            map.put(field.getName(), listA);
        }
        return list2;
    }
}
