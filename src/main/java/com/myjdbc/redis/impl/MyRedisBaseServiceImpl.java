package com.myjdbc.redis.impl;


import com.myjdbc.core.util.StringUtil;
import com.myjdbc.redis.MyRedisBaseService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @Author 陈文
 * @Date 2019/12/10  16:16
 * @Description Redis缓存工具
 */
@Service("myRedisBaseService")
public class MyRedisBaseServiceImpl implements MyRedisBaseService {
//    private static final Logger logger = LoggerFactory.getLogger(RedisBaseServiceImpl.class);

    @Resource
    private StringRedisTemplate redisTemplate;

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public String get(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (NULL.equals(value)) {
            return null;
        } else {
            return value;
        }
    }

    @Override
    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        if (StringUtil.isNotEmpty(value)) {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        } else {
            //不允许存储空值
            //如果是空值，则存入特定数据，保持60秒,用于防止缓存穿透
            redisTemplate.opsForValue().set(key, NULL, 60, TimeUnit.SECONDS);
        }
    }

    @Override
    public void set(String key, String value, long timeout) {
        set(key, value, timeout, TimeUnit.DAYS);
    }

    @Override
    public void set(String key, String value) {
        //默认有效时间7天
        set(key, value, SAVE_TIME);
    }

    @Override
    public Map<String, Object> getMap(String key) {
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        Map<String, Object> map = hash.entries(key);
        return map;
    }

    @Override
    public void setMap(String key, Map<? extends String, ? extends Object> map, long timeout) {
        delete(key);
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, timeout, TimeUnit.DAYS);
    }

    @Override
    public void setMap(String key, Map<? extends String, ? extends Object> map) {
        setMap(key, map, 365);
    }
}
