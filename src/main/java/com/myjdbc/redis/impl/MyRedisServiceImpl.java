package com.myjdbc.redis.impl;

import com.myjdbc.core.util.ListUtil;
import com.myjdbc.jdbc.core.service.BaseService;
import com.myjdbc.jdbc.util.BeanUtil;
import com.myjdbc.redis.MyRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Service("myRedisService")
public class MyRedisServiceImpl extends MyRedisBaseServiceImpl implements MyRedisService {
    private static final Logger logger = LoggerFactory.getLogger(MyRedisServiceImpl.class);

    @Autowired
    private BaseService baseService;

    @Override
    public <T> T getPojo(Class<T> cls, String id) {
        Map<String, Object> map = getPojoMap(cls, id);
        if (map != null) {
            T t = BeanUtil.mapToPrjo(cls, map);
            return t;
        }
        return null;
    }

    @Override
    public void setPojo(Object obj) {
        try {
            baseService.save(obj);
            setPojoMap(obj.getClass(), getKey(obj), BeanUtil.pojoToMap(obj, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void updatePojo(Class<?> cls, String id) {
        updatePojoMap(cls, id);
    }

    @Override
    public void updatePojoMap(Class<?> cls, String id) {
        super.delete(getKey(cls, id));
        getPojoMap(cls, id);
    }

    @Override
    public Map<String, Object> getPojoMap(Class<?> cls, String id) {
        String key = getKey(cls, id);
        Map<String, Object> map = super.getMap(key);
        //如果存在，直接返回
        if (map != null && map.size() > 0) {
            return map;
        }
        try {
            //从数据库查找实体
            map = baseService.findMapById(cls, id);
            setPojoMap(cls, key, map);
            return map;
        } catch (SQLException e) {
            logger.error("缓存工具->获取实体故障：" + e.getMessage());
            return null;
        }
    }

    @Override
    public void setPojoMap(Class cls, String key, Map<String, Object> map) {
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
        try {
            fieldName = BeanUtil.getPrimaryName(cls, fieldName);
            list = baseService.criteriaEq(cls, fieldName, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public Object getDictionaryValue(Class<?> cls, String replaceName, String id) {
        Map<String, Object> map = getPojoMap(cls, id);
        if (map != null) {
            return map.get(replaceName);
        }
        return null;
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
        return tableName.toUpperCase() + id;
    }

}
