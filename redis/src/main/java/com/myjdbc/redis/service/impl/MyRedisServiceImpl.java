package com.myjdbc.redis.service.impl;

import com.myjdbc.core.util.ListUtil;
import com.myjdbc.jdbc.core.service.BaseService;
import com.myjdbc.jdbc.util.BeanUtil;
import com.myjdbc.redis.service.MyRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


@Service("myRedisService")
public class MyRedisServiceImpl extends MyRedisBaseServiceImpl implements MyRedisService {
    private static final Logger logger = LoggerFactory.getLogger(MyRedisServiceImpl.class);

    @Autowired
    private BaseService baseService;

    @Override
    public <T> T getPojo(Class<T> cls, String id) {
        String key = getKey(cls, id);
        T t = getPojoCache(cls, key);
        if (t == null) {
            t = baseService.findById(cls, id);
            setPojo(t);
        }
        return t;
    }

    @Override
    public <T> T getPojo(Class<T> cls, String fieldName, String value) {
        String key = getKey(cls, fieldName + "_" + value);
        T t = getPojoCache(cls, fieldName + "_" + value);
        if (t == null) {
            List<T> list = baseService.criteriaEq(cls, fieldName, value);
            if (ListUtil.isNotEmpty(list)) {
                t = list.get(0);
                setPojo(t);
            }
        }
        return t;
    }


    @Override
    public void setPojo(Object obj) {
        try {
            baseService.save(obj);
            setPojo(obj, getKey(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPojo(Object obj, String key) {
        setPojoMap(obj.getClass(), key, BeanUtil.pojoToMap(obj, true));
    }

    @Override
    public void deletePojo(Object obj) {
        try {
            baseService.delete(obj);
            super.delete(getKey(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    @Override
//    public void updatePojo(Class<?> cls, String id) {
//        updatePojoMap(cls, id);
//    }
//
//    @Override
//    public void updatePojoMap(Class<?> cls, String id) {
//        super.delete(getKey(cls, id));
//        getPojoMap(cls, id);
//    }

    private <T> T getPojoCache(Class<?> cls, String key) {
        Map<String, Object> map = super.getMap(key);
        //如果存在，直接返回
        if (map != null && map.size() > 0) {
            T t = BeanUtil.mapToPrjo(cls, map);
            return t;
        }
        return null;
    }

    private void setPojoMap(Class cls, String key, Map<String, Object> map) {
        super.setMap(key, map);
        //同步分开保存缓存实体的字典字段
//        setPojoMapDictionary(cls, map);
    }


    @Override
    public Object getDictionaryValue(String key) {
        return super.get(key);
    }

    @Override
    public <T> Object getDictionaryValue(Class<T> cls, String fieldName, String replaceName, String value) {
        List<T> list = null;
        fieldName = BeanUtil.getPrimaryName(cls, fieldName);
        list = baseService.criteriaEq(cls, fieldName, value);
        if (ListUtil.isNotEmpty(list)) {
            T t = list.get(0);
            try {
                replaceName = BeanUtil.getPrimaryName(cls, replaceName);
                Method getMethod = BeanUtil.getMethod(cls, BeanUtil.getField(replaceName));
                Object obj = getMethod.invoke(t);
                return obj;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public <T> Object getDictionaryValue(Class<T> cls, String replaceName, String id) {
        String key = getKey(cls, id);
        T t = getPojoCache(cls, key);
        if (t == null) {
            t = baseService.findById(cls, id);
            setPojo(t);
        }
        Object value = BeanUtil.getValue(t, replaceName, cls);
        return value;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/14  23:50
     * @Description 获取缓存键
     */
    private String getKey(Object t) {
        return getKey(t.getClass(), BeanUtil.getId(t));
    }

    /**
     * @Author 陈文
     * @Date 2019/12/14  23:50
     * @Description 获取缓存键
     */
    private String getKey(Class<? extends Object> cls, String id) {
        return getKey(BeanUtil.getTableName(cls), id);
    }

    /**
     * @Author 陈文
     * @Date 2019/12/14  23:55
     * @Description 获取缓存键
     */
    private String getKey(String tableName, String id) {
        return tableName.toUpperCase() + "_" + id;
    }

}
