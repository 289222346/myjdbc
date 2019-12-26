package com.myjdbc.redis;


import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface MyRedisBaseService {

    /**
     * 默认存储时间（天）
     */
    long SAVE_TIME = 7;
    String NULL = "DataNotFound_MYJDBC";

    /**
     * 删除Key及其Value
     *
     * @param key
     */
    void delete(String key);

    /**
     * 根据Key获取Value
     *
     * @param key
     * @return 存储在Redis中的字符串内容
     */
    String get(String key);

    /**
     * 保存Value
     *
     * @param key      键
     * @param value    值
     * @param timeout  存储时间
     * @param timeUnit 时间单位
     */
    void set(String key, String value, long timeout, TimeUnit timeUnit);

    /**
     * 保存Value
     *
     * @param key     键
     * @param value   值
     * @param timeout 存储时间（天）
     */
    void set(String key, String value, long timeout);

    /**
     * 保存Value
     *
     * @param key   键
     * @param value 值
     * @see #SAVE_TIME 使用默认存储时间（天）;
     */
    void set(String key, String value);

    /**
     * 获取Map
     *
     * @param key 键
     * @return 存储在Redis中的Map
     */
    Map<String, Object> getMap(String key);

    /**
     * 保存Map
     *
     * @param key     键
     * @param map     值
     * @param timeout 存储时间（天）
     */
    void setMap(String key, Map<? extends String, ? extends Object> map, long timeout);

    /**
     * 保存Map
     *
     * @param key 键
     * @param map 值
     * @see #SAVE_TIME 使用默认存储时间（天）;
     */
    void setMap(String key, Map<? extends String, ? extends Object> map);

}
