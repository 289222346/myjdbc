package com.myjdbc.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Author 陈文
 * @Date 2019/12/10  17:35
 * @Description 注解只对String类型有效
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RedisCache {

    /**
     * @return
     * @Author 陈文
     * @Date 2020/3/6  11:45
     * @Description 区别于其他缓存的key类型名称
     */
    String value() default "";

}