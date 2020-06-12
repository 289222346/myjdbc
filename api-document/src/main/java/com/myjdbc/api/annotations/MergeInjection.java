package com.myjdbc.api.annotations;

import java.lang.annotation.*;

/**
 * 合并注入
 *
 * @author 陈文
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MergeInjection {

    /**
     * 被注入注解的属性名
     *
     * @return
     */
    String value() default "";

    /**
     * 被注入的注解
     */
    Class<? extends Annotation> annotation();
}
