//package com.myjdbc.jdbc.core.service.impl;

//此类需要放置到一个额外的项目中，该项目应该引用多个myjdbc的子项目，用于各项目之间沟通，且联合使用
//
//
//import com.myjdbc.core.util.ClassUtil;
//import com.myjdbc.core.util.StringUtil;
//import com.myjdbc.jdbc.core.service.BaseService;
//import com.myjdbc.jdbc.core.sqlcriteria.CriteriaQuery;
//import com.myjdbc.jdbc.core.service.impl.BaseServiceImplJdbc;
//import com.myjdbc.jdbc.util.BeanUtil;
//import com.myjdbc.redis.annotation.Dictionary;
//import com.myjdbc.redis.annotation.RedisCache;
//import com.myjdbc.redis.service.MyRedisService;
//import com.myjdbc.redis.service.impl.MyRedisDictionary;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.Serializable;
//import java.lang.reflect.Field;
//import java.sql.Connection;
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * 整合myjdbc框架内容，提供外部接口和配置选项
// */
//@Service("baseService")
//public class BaseServiceImpl extends BaseServiceImplJdbc implements BaseService {
//    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);
//
//    //缓存开启标志（true为开启缓存,默认为true）
//    private static final boolean redisFlag = true;
//
//    @Autowired
//    private MyRedisDictionary myRedisDictionary;
//    @Autowired
//    private MyRedisService redisService;
//
//
////    @Override
////    public Map<String, Object> findMapById(Class<? extends Object> cls, Serializable id) {
////        Map<String, Object> map = super.findMapById(cls, id);
////        return map;
////    }
//
//    @Override
//    public <T> T findById(Class<T> cls, Serializable id) {
//        RedisCache redisCache = cls.getAnnotation(RedisCache.class);
//        String value = "";
//        if (redisFlag && redisCache != null) {
//            value = redisCache.value();
//            if (StringUtil.isEmpty(value)) {
//                value = BeanUtil.getTableName(cls);
//            }
//            T t = redisService.getPojo(cls, value + id.toString());
//            if (t != null) {
//                return t;
//            }
//        }
//        T t = super.findById(cls, id);
//        addDictionaryT(t, cls);
//        if (redisFlag && redisCache != null) {
//            redisService.setPojo(t, value + id.toString());
//        }
//        return t;
//    }
//
//    @Override
//    public <T> List<T> criteriaEq(Class<T> cls, String fieldName, Object filedValue) {
//        //非最终方法
//        return criteriaEq(cls, fieldName, filedValue, true);
//    }
//
//
//    @Override
//    public <T> List<T> criteriaEq(Class<T> cls, String fieldName, Object filedValue, boolean parentFlag) {
//        List<T> list = super.criteriaEq(cls, fieldName, filedValue, parentFlag);
//        addDictionary(list, cls);
//        return list;
//
//    }
//
//
//    @Override
//    public <T> List<T> findAll(CriteriaQuery<T> criteriaQuery) {
//        //非最终方法
//        return findAllDao(criteriaQuery, 0);
//    }
//
//
//    protected <T> List<T> findAllDao(CriteriaQuery<T> criteriaQuery, int findCount) {
//        List<T> list = super.findAllDao(criteriaQuery, findCount);
//        addDictionary(list, criteriaQuery.getCls());
//        return list;
//    }
//
//
//    protected List findAll(Connection conn, String sql, CriteriaQuery criteriaQuery, Object... values) {
//        List list = super.findAll(conn, sql, criteriaQuery, values);
//        addDictionary(list, criteriaQuery.getCls());
//        return list;
//    }
//
//    @Override
//    public List findAll(CriteriaQuery criteriaQuery, String sql, Object... values) {
//        List list = super.findAll(criteriaQuery, sql, values);
//        addDictionary(list, criteriaQuery.getCls());
//        return list;
//    }
//
//    @Override
//    public List<Object[]> findAll(String sql, Object... values) {
//        List<Object[]> list = super.findAll(sql, values);
//        /**
//         *
//         */
//        return list;
//    }
//
//    @Override
//    public <T> List<T> findAll(Class<T> cls, String sql, Object... values) {
//        List<T> list = super.findAll(cls, sql, values);
//        addDictionary(list, cls);
//        return list;
//    }
//
//
//    @Override
//    public <T> List<T> criteriaIn(Class<T> cls, String fieldName, Object[] values) {
//        List<T> list = super.criteriaIn(cls, fieldName, values);
//        addDictionary(list, cls);
//        return list;
//    }
//
//
//    /**
//     * 添加（处理）字典
//     * 单个元素的包装方法（）
//     *
//     * @param t
//     * @param cls
//     * @param <T>
//     */
//    private <T> void addDictionaryT(T t, Class<T> cls) {
//        List<T> list = new ArrayList<>();
//        list.add(t);
//        addDictionary(list, cls);
//    }
//
//    /**
//     * 添加（处理）字典
//     *
//     * @param list
//     * @param cls
//     */
//    private <T> void addDictionary(List<T> list, Class<T> cls) {
//        //获取所有字段（包括父类）
//        Field[] fields = ClassUtil.getAllFields(cls, null);
//
//        //获取含有字典注解的字段
//        List<Field> fieldList = new ArrayList<>();
//        for (Field field : fields) {
//            if (field.getAnnotation(Dictionary.class) != null) {
//                fieldList.add(field);
//            }
//        }
//
//        //为字典注解字段，解析值
//        for (T t : list) {
//            for (Field field : fieldList) {
//                Object value = BeanUtil.getValue(t, field, cls);
//                value = myRedisDictionary.valueToDictionary(cls, field, value, t);
//                t = BeanUtil.setValue(cls, field, t, value, false);
//            }
//        }
//    }
//}
