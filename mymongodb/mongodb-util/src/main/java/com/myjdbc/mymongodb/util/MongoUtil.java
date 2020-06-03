package com.myjdbc.mymongodb.util;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;
import com.myjdbc.core.constants.OpType;
import com.myjdbc.core.criteria.CriteriaQuery;
import com.myjdbc.core.criteria.impl.CriteriaQueryImpl;
import com.myjdbc.core.model.Criterion;
import com.myjdbc.core.util.*;
import com.myjdbc.mymongodb.constants.MongodbConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.annotation.Id;
import org.springframework.util.ObjectUtils;

import javax.persistence.OneToOne;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

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
        Document document = new Document(MongoUtil.poToMap(t));
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
    public static <T> Map<String, Object> poToMap(T obj) {
        //字段禁止为空
        Class<?> cls = obj.getClass();
        Field[] fields = ClassUtil.getValidFields(cls);
        // 声明Map对象，存储属性
        Map map = new LinkedHashMap();
        for (Field field : fields) {
            // 获取要设置的属性的set方法名称
            String getField = ClassUtil.getField(field.getName());
            try {
                // 声明类函数方法，并获取和设置该方法型参类型
                Method getMethod = cls.getMethod(getField);
                //属性名
                String propertyName = getPropertyName(field);
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
            Field[] fields = ClassUtil.getValidFields(cls);
            for (Field field : fields) {
                try {
                    //数据库字段名
                    String propertyName = getPropertyName(field);
                    //预备赋与的属性值
                    Object value = map.get(propertyName);
                    //属性类型
                    Class fileType = field.getType();

                    //如果值为List<Document>集合，则进入关联表处理
                    if (ListUtil.isListAndNotEmpty(value)) {
                        List valueList = (List) value;
                        if (valueList.get(0).getClass().equals(Document.class)) {
                            List list = handleForeignResult(field, (List<Document>) value);
                            if (!fileType.equals(List.class)) {
                                value = list.get(0);
                            } else {
                                value = list;
                            }
                        }
                    }

                    //如果属性值为空，则不赋值
                    if (ObjectUtils.isEmpty(value)) {
                        continue;
                    }


                    // 获取要设置的属性的set方法名称
                    String setField = ClassUtil.setField(field.getName());
                    // 声明类函数方法，并获取和设置该方法型参类型
                    Method setMethod = cls.getMethod(setField, fileType);
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

    public static <T> BasicDBObject modelToFilter(T t) {
        Map<String, Object> map = poToMap(t);
        return mapToFilter(t.getClass(), map);
    }

    /**
     * 将{@link Map}转换成 {@link BasicDBObject filter}。
     * 内容示意：Map{key,value} 其中{@code key} 是字段名，{@code value} 是限定值。限定条件全部为EQ(完全相等)
     *
     * @Author: 陈文
     * @Date: 2020/4/20 12:40
     */
    public static BasicDBObject mapToFilter(Class cls, Map<String, Object> map) {
        CriteriaQuery criteriaQuery = new CriteriaQueryImpl(cls);
        for (String key : map.keySet()) {
            criteriaQuery.eq(key, map.get(key));
        }
        return spliceQuery(criteriaQuery);
    }

    public static BasicDBObject idToFilter(Serializable id) {
        BasicDBObject condition = MongoUtil.toCondition(OpType.EQ, id);
        BasicDBObject query = new BasicDBObject("_id", condition);
        return query;
    }

    public static void getCriteria(BasicDBObject query, String filedName, List<Criterion> criterionList) {
        if (query == null) {
            throw new NullPointerException("query-查询器不能为空！");
        }

        if (StringUtil.isEmpty(filedName)) {
            throw new NullPointerException("filedName-限定字段名不能为空！");
        }

        if (ListUtil.isEmpty(criterionList)) {
            throw new NullPointerException("criterionList-限定条件不能为空！");
        }

        BasicDBObject condition = MongoUtil.toCondition(criterionList);
        query.append(filedName, condition);
    }

    public static <T> BasicDBObject spliceQuery(CriteriaQuery<T> criteriaQuery) {
        Class cls = criteriaQuery.getCls();
        criteriaQuery = spliceCriteriaQuery(criteriaQuery);
        //MongoDB的Query查询构造器
        BasicDBObject query = new BasicDBObject();
        //遍历Myjdbc查询构造器，并适配成MongoDB的Query查询构造器
        criteriaQuery.getCriteriaMap().values().forEach(criteria -> {
            //查询条件
            List<Criterion> criterions = criteria.getCriterions();
            //限定字段名
            String filedName = getPropertyName(cls, criteria.getFieldName());
            //获取匹配方式
            MongoUtil.getCriteria(query, filedName, criterions);
        });
        return query;
    }

    public static <T> CriteriaQuery<T> spliceCriteriaQuery(CriteriaQuery<T> criteriaQuery) {
        T t = criteriaQuery.getQueryT();
        if (t == null) {
            return criteriaQuery;
        }
        Document document = MongoUtil.mongoPOJOToDocument(t);
        document.forEach((fieldName, value) -> {
            criteriaQuery.eq(fieldName, value);
        });
        return criteriaQuery;
    }

    public static BasicDBObject toCondition(OpType op, Object value) {
        List<Criterion> criterionList = new ArrayList<>();
        Criterion criterion = new Criterion(op, value);
        criterionList.add(criterion);
        return toCondition(criterionList);
    }

    public static BasicDBObject toCondition(List<Criterion> criterionList) {
        BasicDBObject basicDBObject = new BasicDBObject();
        for (Criterion criterion : criterionList) {
            OpType op = criterion.getOp();
            Object value = criterion.getFieldValue();


            //字段值为空匹配
            if (op == OpType.IS_NULL) {
                basicDBObject.append(MongodbConstants.OP_EXISTS, false);
                continue;
            }

            if (op == OpType.IS_NOT_NULL) {
                basicDBObject.append(MongodbConstants.OP_EXISTS, true);
                continue;
            }

            //限定值
            if (ObjectUtils.isEmpty(value)) {
                throw new NullPointerException("限定条件：【" + op.getRemark() + "】 ,限定值不能为空！");
            }

            //完全相等
            if (op == OpType.EQ) {
                basicDBObject.append(MongodbConstants.OP_EQ, value);
                continue;
            }

            //模糊查询
            if (op == OpType.LIKE) {
                Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
                basicDBObject.append(MongodbConstants.OP_REGEX, pattern);
                continue;
            }

            //包含相等
            if (op == OpType.IN) {
                basicDBObject.append(MongodbConstants.OP_IN, value);
                continue;
            }

            //大于
            if (op == OpType.GT) {
                basicDBObject.append(MongodbConstants.OP_GT, value);
                continue;
            }

            //小于
            if (op == OpType.LT) {
                basicDBObject.append(MongodbConstants.OP_LT, value);
                continue;
            }

            //大于等于
            if (op == OpType.GE) {
                basicDBObject.append(MongodbConstants.OP_GE, value);
                continue;
            }

            //小于等于
            if (op == OpType.LE) {
                basicDBObject.append(MongodbConstants.OP_LE, value);
                continue;
            }

            throw new NullPointerException(op.getRemark() + ":  无效限定条件！");
        }
        return basicDBObject;
    }


    /**
     * Document集合转成List实体集合
     *
     * @param documents mongo文档集合
     * @param cls       实体类型
     * @param <T>       实体类指定泛型
     * @return
     */
    public static <T> List<T> documentIterableToPOJOList(Iterable<Document> documents, Class<T> cls) {
        List<T> list = new ArrayList<>();
        for (Document document : documents) {
            T t = documentToMongoPOJO(document, cls);
            if (t != null) {
                list.add(t);
            }
        }
        return list;
    }

    /**
     * 处理表关联
     *
     * @param cls 实体类
     * @return
     */
    public static List<Bson> handleForeign(Class cls) {
        List<Bson> joinList = new ArrayList<>();
        for (Field field : ClassUtil.getAllFieldsList(cls)) {
            if (field.getAnnotation(OneToOne.class) != null) {
                joinList.add(handleOneToOne(field));
            }
        }
        return joinList;
    }

    /**
     * 处理一对一关联
     *
     * @param field 字段
     * @return
     */
    private static Bson handleOneToOne(Field field) {
        OneToOne oneToOne = field.getAnnotation(OneToOne.class);
        String table = ModelUtil.getModelName(field.getType());
        String localField = "_id";
        String foreignField = oneToOne.mappedBy();
        String as = field.getName();
        if (StringUtil.isEmpty(foreignField)) {
            foreignField = field.getName();
        }
        //创建一个$lookup管道阶段，将当前集合与通过使用本地字段和外部字段之间的相等匹配指定的集合连接在一起
        Bson join = Aggregates.lookup(table, localField, foreignField, as);
        return join;
    }

    /**
     * 处理表关联结果
     *
     * @param field     关联字段(关联表）
     * @param documents 结果
     * @return
     */
    public static <T> List<T> handleForeignResult(Field field, List<Document> documents) {
        if (field.getAnnotation(OneToOne.class) != null) {
            return handleOneToOneResult(field, documents);
        }
        //增加关联处理，扩展位置
        return null;
    }

    /**
     * 获取MongoDB连接URL
     * 如果存在公钥，则对用户密码进行解密处理
     *
     * @param dbUri      原始(IP)地址
     * @param dbUserName 用户名
     * @param dbPwd      密码
     * @param publicKey  公钥
     * @return
     */
    public static String getDbUri(String dbUri, String dbUserName, String dbPwd, String publicKey) {
        //是否配置了密码
        if (!StringUtil.isEmpty(dbUserName) && !StringUtil.isEmpty(dbPwd)) {
            if (StringUtil.isNotEmpty(publicKey)) {
                dbUserName = SecretUtil.decrypt(dbUserName, publicKey);
                dbPwd = SecretUtil.decrypt(dbPwd, publicKey);
            }
            String key = dbUserName + ":" + dbPwd + "@";
            dbUri = key + dbUri;
        }
        dbUri = "mongodb://" + dbUri;
        log.println("mongodb获取了一次url:" + dbUri);
        return dbUri;
    }

    private static <T> List<T> handleOneToOneResult(Field field, List<Document> documents) {
        Class<T> cls = (Class<T>) field.getType();
        List<T> list = documentIterableToPOJOList(documents, cls);
        return list;
    }

    public static String getPropertyName(Field field) {
        Id id = field.getAnnotation(Id.class);
        //mongodb中，默认使用_id作为主键
        // 因为mongodb会自动为该字段创建索引，所以主键不管在实体中是何名称，到数据库统一使用_id
        if (id != null) {
            return "_id";
        }
        return ModelUtil.getPropertyName(field);
    }

    /**
     * 获取属性名称
     *
     * @param cls       实体类
     * @param fieldName 实体属性名称
     * @return 经过处理后的属性名称，如果属性不存在，则返回{@code fieldName}本身
     * @Author 陈文
     * @Date 2020/4/21  17:03
     */
    public static String getPropertyName(Class cls, String fieldName) {
        if (cls == null) {
            return fieldName;
        }
        Field[] fields = ClassUtil.getValidFields(cls);
        for (Field field : fields) {
            //符合其中一个属性名，则返回
            if (fieldName.equals(field.getName())) {
                return getPropertyName(field);
            }
        }
        return fieldName;
    }

}
