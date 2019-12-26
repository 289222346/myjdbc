package com.myjdbc.redis;

import java.util.Map;

public interface MyRedisService extends MyRedisBaseService {

    /**
     * 获取实体（先查找缓存、再查找数据库）
     *
     * @param cls 实体类
     * @param id  主键
     * @param <T> 返回类型
     */
    <T> T getPojo(Class<T> cls, String id);

    /**
     * 保存实体（并写入/更新缓存）
     *
     * @param obj 实体
     */
    void setPojo(Object obj);

    /**
     * 删除实体（并删除缓存）
     *
     * @param obj 实体
     */
    void deletePojo(Object obj);

    /**
     * 获取Map形式返回的实体
     *
     * @param cls 实体类
     * @param id  主键
     */
    Map<String, Object> getPojoMap(Class<? extends Object> cls, String id);

    /**
     * 以Map的形式存储实体
     *
     * @param cls 实体类
     * @param key 主键
     * @param map Map（实体）
     */
    void setPojoMap(Class cls, String key, Map<String, Object> map);

    /**
     * 获取字典值（仅查找缓存中存在的数据，不会查找数据库）
     *
     * @param key 键
     * @return
     */
    Object getDictionaryValue(String key);


    <T> Object getDictionaryValue(Class<T> cls, String fieldName, String replaceName, String value);

    /**
     * 获取字典值
     * 通过唯一字段值，找到该行数据，返回映射字段值
     * 优先查找缓存
     *
     * @param replaceName 映射字段名
     * @param id          主鍵值
     * @return
     */
    public Object getDictionaryValue(Class<?> cls, String replaceName, String id);


    /**
     * 更新缓存实体
     *
     * @param cls
     * @param id
     */
    public void updatePojo(Class<?> cls, String id);

    /**
     * 更新缓存实体
     *
     * @param cls
     * @param id
     */
    public void updatePojoMap(Class<?> cls, String id);

}
