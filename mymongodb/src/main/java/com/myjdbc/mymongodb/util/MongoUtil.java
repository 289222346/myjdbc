package com.myjdbc.mymongodb.util;

import com.mongodb.BasicDBObject;
import com.myjdbc.core.constants.OpType;
import com.myjdbc.core.util.ClassUtil;
import com.myjdbc.core.util.ModelUtil;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.mymongodb.constants.MongodbConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.bson.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class MongoUtil {

    public static void main(String[] args) {
//        UserExample userExample = new UserExample();
//        userExample.setPhone("18676826110");
//        userExample.setName("陈文");
//        Document document = getDocument(userExample);
//        System.out.println(document);
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2020/3/26  14:26
     * @Description 获取Mongo的Document文档
     */
    public static <T> Document mongoPOJOToDocument(T t) {
        Document document = new Document(MongoUtil.mongoPOJOToMap(t));
        return document;
    }

    public static <T> T documentToMongoPOJO(Document document, Class<T> cls) {
        return mapToMongoPOJO(document, cls);
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2020/3/29  20:09
     * @Description 将对象属性反射成Map(Document)
     */
    public static <T> Map<String, Object> mongoPOJOToMap(T obj) {
        Class<?> cls = obj.getClass();
        Field[] fields = getValidFields(cls);
        // 声明Map对象，存储属性
        Map map = new LinkedHashMap();
        for (Field field : fields) {
            // 获取要设置的属性的set方法名称
            String getField = ClassUtil.getField(field.getName());
            try {
                // 声明类函数方法，并获取和设置该方法型参类型
                Method getMethod = cls.getMethod(getField);
                //属性名
                String propertyName = ModelUtil.getPropertyName(field);
                // 把获得的值设置给map对象
                Object value = getMethod.invoke(obj);
                if (!ObjectUtils.isEmpty(value)) {
                    map.put(propertyName, value);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static <T> T mapToMongoPOJO(Map<String, Object> map, Class<T> cls) {
        T t = null;
        try {
            t = cls.newInstance();
            Field[] fields = getValidFields(cls);
            for (Field field : fields) {
                // 获取要设置的属性的set方法名称
                String setField = ClassUtil.setField(field.getName());
                try {
                    // 声明类函数方法，并获取和设置该方法型参类型
                    Method setMethod = cls.getMethod(setField, field.getType());
                    //属性名
                    String propertyName = ModelUtil.getPropertyName(field);
                    Object value = map.get(propertyName);
                    setMethod.invoke(t, value);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将{@link Map}转换成 {@link Query}。
     * 内容示意：Map{key,value} 其中{@code key} 是字段名，{@code value} 是限定值。限定条件全部为EQ(完全相等)
     *
     * @Author: 陈文
     * @Date: 2020/4/20 12:40
     */
    public static Query mapToQuery(Map<String, Object> map) {
        Query query = new Query();
        if (map != null) {
            map.forEach((key, value) -> {
                query.addCriteria(Criteria.where(key).is(value));
            });
        }
        return query;
    }

    public static Field[] getValidFields(Class<?> cls) {
        // 获取该类所有属性名
        List<Field> fieldList = ClassUtil.getAllFieldsList(cls);
        //排除拥有ApiModelProperty属性，且hidden属性为ture
        for (int i = 0; i < fieldList.size(); i++) {
            ApiModelProperty apiModelProperty = fieldList.get(i).getAnnotation(ApiModelProperty.class);
            if (apiModelProperty != null && apiModelProperty.hidden() == true) {
                fieldList.remove(i--);
            }
        }

        //整理后的有效属性名
        return fieldList.toArray(new Field[fieldList.size()]);
    }

    public static BasicDBObject toCondition(OpType op, Object value) {
        //字段值为空匹配
        if (op == OpType.IS_NULL) {
            return new BasicDBObject(MongodbConstants.OP_EXISTS, false);
        }

        if (op == OpType.IS_NOT_NULL) {
            return new BasicDBObject(MongodbConstants.OP_EXISTS, true);
        }

        //限定值
        if (ObjectUtils.isEmpty(value)) {
            throw new NullPointerException(op.getRemark() + "  限定条件,限定值不能为空！");
        }

        //完全相等
        if (op == OpType.EQ) {
            return new BasicDBObject(MongodbConstants.OP_EQ, value);
        }

        //模糊查询
        if (op == OpType.LIKE) {
            Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
            return new BasicDBObject(MongodbConstants.OP_REGEX, pattern);
        }

        //包含相等
        if (op == OpType.IN) {
            return new BasicDBObject(MongodbConstants.OP_IN, value);
        }

        //大于
        if (op == OpType.GT) {
            return new BasicDBObject(MongodbConstants.OP_GT, value);
        }

        //小于
        if (op == OpType.LT) {
            return new BasicDBObject(MongodbConstants.OP_LT, value);
        }

        //大于等于
        if (op == OpType.GE) {
            return new BasicDBObject(MongodbConstants.OP_GE, value);
        }

        //小于等于
        if (op == OpType.LE) {
            return new BasicDBObject(MongodbConstants.OP_LE, value);
        }

        throw new NullPointerException(op.getRemark() + "  无效限定条件！");
    }

}
