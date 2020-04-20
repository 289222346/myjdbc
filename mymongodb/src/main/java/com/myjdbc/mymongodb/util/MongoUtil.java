package com.myjdbc.mymongodb.util;

import com.myjdbc.core.util.ClassUtil;
import com.myjdbc.core.util.StringUtil;
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
    public static <T> Document poToDocument(T t) {
        Document document = new Document(MongoUtil.mongoPOToMap(t));
        return document;
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2020/3/29  20:09
     * @Description 将对象属性反射成Map(Document)
     */
    public static <T> Map<String, Object> mongoPOToMap(T obj) {
        Class<?> cls = obj.getClass();
        // 获取该类所有属性名
        List<Field> fieldList = ClassUtil.getAllFieldsList(cls);
        //排除掉非ApiModelProperty属性
        for (int i = 0; i < fieldList.size(); i++) {
            ApiModelProperty apiModelProperty = fieldList.get(i).getAnnotation(ApiModelProperty.class);
            if (apiModelProperty == null) {
                fieldList.remove(i--);
            }
        }
        //整理后的有效属性名
        Field[] fields = fieldList.toArray(new Field[fieldList.size()]);

        // 声明Map对象，存储属性
        Map map = new LinkedHashMap();
        for (Field field : fields) {
            // 获取要设置的属性的set方法名称
            String getField = ClassUtil.getField(field.getName());
            try {
                // 声明类函数方法，并获取和设置该方法型参类型
                Method getMethod = cls.getMethod(getField);

                //属性名
                String fieldName = field.getName();
                // 把获得的值设置给map对象

                Object value = getMethod.invoke(obj);
                if (!ObjectUtils.isEmpty(value)) {
                    //若ID属性不为_id，则添加默认ID属性
                    Id id = field.getAnnotation(Id.class);
                    //存在Id注解，则为主键，Mongo中默认主键为_id（带索引）
                    if (id != null && !"_id".equals(fieldName)) {
                        map.put("_id", value);
                    } else {
                        map.put(fieldName, value);
                    }
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

    /**
     * 获取模型名称(数据库表名)
     *
     * @Author: 陈文
     * @Date: 2020/4/20 11:26
     */
    public static String getModelName(Class cls) {
        ApiModel apiModel = (ApiModel) cls.getAnnotation(ApiModel.class);
        String modelName = null;
        if (apiModel != null) {
            //collectionName
            modelName = apiModel.value();
        }
        if (StringUtil.isEmpty(modelName)) {
            modelName = cls.getSimpleName();
        }
        return modelName;
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
        map.forEach((key, value) -> {
            query.addCriteria(Criteria.where(key).is(value));
        });
        return query;
    }

}
