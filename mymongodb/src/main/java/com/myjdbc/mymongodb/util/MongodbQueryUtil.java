package com.myjdbc.mymongodb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 查询条件构造器
 *
 * @author 陈文
 * @Description
 * @date 2019/7/15 9:47
 */
public class MongodbQueryUtil<T> {
    private static final Logger logger = LoggerFactory.getLogger(MongodbQueryUtil.class);


//    public MongodbQueryUtil(Class<T> cls, T t) {
//        //实体映射成document文档
//        Document document = MongoDBUtil.poToDocument(t);
//        document.forEach((key, value) -> {
//            //插入查询条件
//            if (String.class.equals(value.getClass())) {
//                //限定值为String类型且带有*号，查询条件为like（模糊匹配），其他查询条件为eq（完全相等）
//                String v1 = value + "";
//                int index1 = v1.indexOf("*");
//                if (index1 == -1) {
//                    eq(key, value);
//                } else {
//                    v1 = v1.replace("*", "");
//                    like(key, v1);
//                }
//            } else {
//                eq(key, value);
//            }
//        });
//    }

}
