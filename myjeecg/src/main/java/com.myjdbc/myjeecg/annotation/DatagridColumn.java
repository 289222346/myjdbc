package com.myjdbc.myjeecg.annotation;

import java.lang.annotation.*;


/**
 * @Author 陈文
 * @Date 2019/12/1  15:29
 * @Description 拥有该注解代表该属性为容器展示类，会被传递到前台并显示
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DatagridColumn {

    /**
     * @Author 陈文
     * @Date 2019/12/16  11:03
     * @Description 列ID
     */
    String id() default "";

    /**
     * @Author 陈文
     * @Date 2019/12/16  11:07
     * @Description 列名称
     */
    String title() default "";

    /**
     * @Author 陈文
     * @Date 2019/12/16  11:07
     * @Description 如果为True，则隐藏列。
     */
    boolean hidden() default false;

}
