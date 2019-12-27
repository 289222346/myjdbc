package com.myjdbc.jdbc.core.sqlgenerator.impl;

import com.myjdbc.core.util.ClassUtil;
import com.myjdbc.core.util.ListUtil;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.jdbc.constants.MyCascade;
import com.myjdbc.jdbc.core.bo.DeleteEntity;
import com.myjdbc.jdbc.core.bo.SavaEntity;
import com.myjdbc.jdbc.core.bo.SavaListEntity;
import com.myjdbc.jdbc.core.service.CriteriaQuery;
import com.myjdbc.jdbc.core.sqlgenerator.SqlGenerator;
import com.myjdbc.jdbc.util.BeanUtil;
import com.myjdbc.jdbc.util.BeanUtilOracle;
import com.myjdbc.jdbc.util.UUIDHexGenerator;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SqlGeneratorBase implements SqlGenerator {

    protected static final String todate = "date_format(?, '%Y-%m-%d %H:%i:%s')";

    @Override
    public <T> String findById(Class<T> cls) {
        //parentFlag:是否查找父类属性
        String primaryKey = BeanUtil.getPrimaryKeyFieldName(cls);
        String tableName = BeanUtil.getTableName(cls);
        String sql = "select " + sqlFields(cls) + " from " + sqlTables(cls) + " where " + tableName + "." + primaryKey + "=?";
        return sql;
    }

    @Override
    public String findAll(CriteriaQuery criteriaQuery) {
        String tableName = sqlTables(criteriaQuery.getCls());

        //获取查询字段
        String sqlFields = sqlFields(criteriaQuery.getCls());

        //获取查询条件
        String sqlWhere = criteriaQuery.getSqlString();

        //组装要查询字段
        StringBuffer sql = new StringBuffer("select ").append(sqlFields).append(" from ").append(tableName).append(" where ").append(sqlWhere);

        //分页组装
        if (criteriaQuery.getPag() != null) {
            //组装并保存分页总数查询SQL
            StringBuffer pagSql = new StringBuffer("SELECT  /*+ROWID(USER)*/   count(*) FROM (").append(sql).append(")");
            criteriaQuery.setSql(pagSql.toString());
            //返回分页的SQL
            return pagSplice(sql.toString(), criteriaQuery.getPage(), criteriaQuery.getRows()).toString();
        }
        //返回不分页的SQL
        return sql.toString();
    }


    public String findAll(CriteriaQuery criteriaQuery, String sql) {
        //分页组装
        if (criteriaQuery.getPag() != null) {
            //组装并保存分页总数查询SQL
            StringBuffer pagSql = new StringBuffer("SELECT  /*+ROWID(USER)*/   count(*) FROM (").append(sql).append(")");
            criteriaQuery.setSql(pagSql.toString());
            //返回分页的SQL
            return pagSplice(sql, criteriaQuery.getPage(), criteriaQuery.getRows()).toString();
        }
        return sql;
    }


//    @Override
//    public <T> String findAll(Class<T> cls) {
//        String tableName = BeanUtil.getTableName(cls);
//        String sql = "select " + sqlFields(cls) + " from " + tableName;
//        return sql;
//    }

    @Override
    public <T> DeleteEntity delete(T t) {
        String fieldName = BeanUtil.getPrimaryKey(t.getClass());
        String primaryKey = BeanUtil.getSqlFormatName(t.getClass(), fieldName);

        List<String> sqlList = deleteSql(new ArrayList<String>(), "", primaryKey, t.getClass());
        Collections.reverse(sqlList);
        DeleteEntity deleteEntity = new DeleteEntity();
        deleteEntity.setSqlList(sqlList);
        deleteEntity.setPkValue(BeanUtil.reflexField(t, fieldName));
        return deleteEntity;
    }

    @Override
    public <T> SavaEntity save(T po) {
        String fieldName = BeanUtil.getPrimaryKey(po.getClass());
        String primaryKey = BeanUtil.getSqlFormatName(po.getClass(), fieldName);

        Object pkValue = BeanUtil.reflexField(po, fieldName);
        String tableName = BeanUtil.getTableName(po.getClass());

        SavaEntity savaEntity = poToParameter(po, primaryKey, pkValue != null && !"".equals(pkValue) ? false : true);
        String sql;
        if (pkValue != null && !"".equals(pkValue)) {
            sql = "UPDATE " + tableName + " SET " + savaEntity.getFieles() + " WHERE "
                    + primaryKey + " =? ";
        } else {
            sql = "insert into " + tableName + "(" +
                    savaEntity.getFieles() + ") values(" + savaEntity.getValues() + ")";
        }
        savaEntity.setSql(sql);
        return savaEntity;
    }


    @Override
    public <T> SavaListEntity save(List<T> pos) {
        T po = ListUtil.isNotEmpty(pos) ? pos.get(0) : null;
        String fieldName = BeanUtil.getPrimaryKey(po.getClass());
        String primaryKey = BeanUtil.getSqlFormatName(po.getClass(), fieldName);

        Object pkValue = BeanUtil.reflexField(po, fieldName);
        String tableName = BeanUtil.getTableName(po.getClass());

        SavaListEntity savaListEntity = poToParameter(pos, primaryKey,
                pkValue != null && !"".equals(pkValue) ? false : true);
        String sql;
//        if (pkValue != null) {
//            sql = "UPDATE " + tableName + " SET " + savaListEntity.getFieles() + " WHERE "
//                    + primaryKey + " =? ";
//        } else {
        sql = "into " + tableName + "(" +
                savaListEntity.getFieles() + ") values(" + savaListEntity.getValues() + ")";
//        }
        savaListEntity.setSql(sql);
        return savaListEntity;
    }


    /**
     * @Author 陈文
     * @Date 2019/12/3  0:37
     * @Description 分页的组装拼接
     */
    protected StringBuffer pagSplice(String sql, int page, int rows) {
        int end = page * rows;
        int start = end - rows;
        StringBuffer newSql = new StringBuffer("SELECT * FROM ( SELECT ROWNUM AS no,a.* FROM (")
                .append(sql).append(") a WHERE ROWNUM <= ").append(end).append(") WHERE no > ").append(start);
        return newSql;
    }


    /**
     * @return
     * @Author 陈文
     * @Date 2019/12/27  16:41
     * @Description 拼接查询表（单表或多表）
     */
    private String sqlTables(Class tableClass) {

        //继承者
        Class sonTable = tableClass;
        //继承者名称
        String sonTableName = BeanUtil.getTableName(sonTable);

        //表组合
        String tableNames = sonTableName;

        //父级
        Class<?> currentClass = sonTable.getSuperclass();
        while (currentClass != null) {
            Inheritance inheritance = currentClass.getAnnotation(Inheritance.class);
            if (inheritance != null && inheritance.strategy().equals(InheritanceType.JOINED)) {
                //允许inheritance注解,且为JOINED类别的父类的属性作为可能的数据库字段被映射
                String newTableName = BeanUtil.getTableName(currentClass);
                tableNames += " LEFT JOIN " + newTableName +
                        " ON " + sonTableName + "." + BeanUtil.getPrimaryKeyJoinColumnName(sonTable) + " = "
                        + newTableName + "." + BeanUtil.getPrimaryKeyJoinColumnName(currentClass);
                sonTableName = newTableName;
            }
            currentClass = currentClass.getSuperclass();
        }

        return tableNames;
    }


    /**
     * @return
     * @Author 陈文
     * @Date 2019/12/27  16:41
     * @Description 拼接查询字段
     */
    private String sqlFields(Class tableClass) {
        //获取所有属性
        Class<?> currentClass = tableClass;
        String fieldNames = "";
        String tableName = BeanUtil.getTableName(currentClass);
        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                //排除静态字段
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                String getField = BeanUtil.getField(field.getName());
                try {
                    Method getMethod = currentClass.getMethod(getField);
                    String fieldName = null;

                    fieldName = BeanUtilOracle.getSqlFormatName(currentClass, field);
                    //遍历JPA注解
                    Annotation[] annotations = getMethod.getAnnotations();
                    fieldName = annotationsToFieldName(annotations, fieldName);

                    if (StringUtil.isEmpty(fieldName)) {
                        continue;
                    }
                    fieldNames += "," + tableName + "." + fieldName;

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            currentClass = currentClass.getSuperclass();
            MappedSuperclass mappedSuperclass = currentClass.getAnnotation(MappedSuperclass.class);
            if (mappedSuperclass != null) {
                //允许MappedSuperclass注解的父类的属性作为可能的数据库字段被映射
                continue;
            }

            Inheritance inheritance = currentClass.getAnnotation(Inheritance.class);
            if (inheritance != null && inheritance.strategy().equals(InheritanceType.JOINED)) {
                //允许inheritance注解,且为JOINED类别的父类的属性作为可能的数据库字段被映射
                tableName = BeanUtil.getTableName(currentClass);
                continue;
            }

//            MappedSuperclass mappedSuperclass = currentClass.getAnnotation(MappedSuperclass.class);
//            if (mappedSuperclass != null) {
//                //只允许MappedSuperclass注解的父类的属性作为可能的数据库字段被映射
//                continue;
//            }
            currentClass = null;
        }
        if (fieldNames.length() > 0) {
            fieldNames = fieldNames.substring(1);
        }
        return fieldNames;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/7  0:06
     * @Description 不加注释，反正加了你们也看不懂
     */
    protected String annotationsToFieldName(Annotation[] annotations, String fieldName) {
        MyCascade myCascade = null;
        for (Annotation annotation : annotations) {
            //处理一对多情况
            if (annotation.annotationType() == OneToMany.class) {
                myCascade = MyCascade.ONE_TO_MANY;
                break;
            } else if (annotation.annotationType() == OneToOne.class) {
                myCascade = MyCascade.ONE_TO_ONE;
                break;
            } else if (annotation.annotationType() == Transient.class) {
                myCascade = MyCascade.TRANSIENT;
                break;
            } else if (annotation.annotationType() == ManyToOne.class) {
                myCascade = MyCascade.MANY_TO_ONE;
            } else if (annotation.annotationType() == JoinColumn.class) {
                //获取字段名称
                JoinColumn joinColumn = (JoinColumn) annotation;
                if (joinColumn != null && StringUtil.isNotEmpty(joinColumn.name())) {
                    fieldName = joinColumn.name();
                }
            } else if (annotation.annotationType() == Column.class) {
                //获取字段名称
                Column column = (Column) annotation;
                if (column != null && StringUtil.isNotEmpty(column.name())) {
                    fieldName = column.name();
                }
            }
        }

        if (myCascade != null && (
                myCascade == MyCascade.ONE_TO_ONE ||
                        myCascade == MyCascade.ONE_TO_MANY ||
                        myCascade == MyCascade.TRANSIENT)) {
            fieldName = null;
        }
        return fieldName;
    }

    /**
     * 拼接删除SQL
     *
     * @param sqlList  已生成的SQL集合
     * @param sql      累积SQL
     * @param mappedBy WHERE条件（主键或者外键）
     * @param cls      实体类
     * @return java.util.List<java.lang.String>
     * @author ChenWen
     * @description 包含级联删除SQL, 一张表一条SQL
     * @date 2019/7/12 11:48
     */
    protected <T> List<String> deleteSql(List<String> sqlList, String sql, String mappedBy, Class<T> cls) {
        String primaryKey = BeanUtil.getPrimaryKeyFieldName(cls);

        String tableName = BeanUtil.getTableName(cls);

        String sql2 = "";
        if (sql.equals("")) {
            sql = "SELECT " + primaryKey + " FROM " + tableName + " WHERE " + mappedBy + " = ? ";
            sql2 = "DELETE FROM " + tableName + " WHERE " + primaryKey + " = ? ";
        } else {
            sql = "SELECT " + primaryKey + " FROM " + tableName + " WHERE " + mappedBy + " IN (" + sql + ")";
            sql2 = "DELETE FROM " + tableName + " WHERE " + primaryKey + " IN (" + sql + ")";
        }
        sqlList.add(sql2);
        //遍历该实体，查看其属性是否需要级联删除
//        Field[] fields = cls.getDeclaredFields();
//        for (Field field : fields) {
//            try {
//                String getField = BeanUtil.getField(field.getName());
//                Method getMethod = cls.getMethod(getField);
//                OneToMany oneToMany = getMethod.getAnnotation(OneToMany.class);
//                if (oneToMany != null) {
//                    for (CascadeType cascadeType : oneToMany.cascade()) {
//                        if (cascadeType == CascadeType.ALL || cascadeType == CascadeType.REMOVE) {
//                            //解析对象并生成删除SQL
//                            String typeName = field.getType().getSimpleName();
//                            Class cls2 = judgeFieldType(field);
//                            if (cls2 != null) {
//                                String mappedBy2 = oneToMany.mappedBy() != null ? BeanUtil.getSqlFormatName(oneToMany.mappedBy()) : BeanUtil.getSqlFormatName(field.getName());
//                                deleteSql(sqlList, sql, mappedBy2, cls2);
//                            }
//                            break;
//                        }
//                    }
//                }
//
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
//        }
        return sqlList;
    }


    /**
     * 将对象转换成拼接参数(增加时)
     *
     * @param po
     * @param <T>
     * @return
     */
    protected <T> SavaEntity poToParameter(T po, String pkName, boolean flag) {
        String fieles = "";//字段名
        String values = "";//值位置替代符
        Object pkValue = "";
        Class<?> cls = po.getClass();
        // 获取该类所有属性名
        Field[] fields = ClassUtil.getAllFields(cls, ClassUtil.DB);
        List<Object> objs = new ArrayList();
        // 声明Map对象，存储属性
        for (Field field : fields) {
            try {
                // 获取要设置的属性的set方法名称
                String getField = BeanUtil.getField(field.getName());
                // 声明类函数方法，并获取和设置该方法型参类型
                Method getMethod = cls.getMethod(getField);
                Annotation[] annotations = getMethod.getAnnotations();
                String fieldName = field.getName();

                //遍历JPA注解
                fieldName = annotationsToFieldName(annotations, fieldName);
                if (StringUtil.isEmpty(fieldName)) {
                    continue;
                }
                //主键使用新生成的UUID，然后将UUID反射进实体，传递给调用者
                Object value = getMethod.invoke(po);
                if (fieldName.equals(pkName)) {
                    if (flag) {
                        String uuid = UUIDHexGenerator.getUUID();
                        fieles += "," + pkName;
                        values += ",?";
                        objs.add(uuid);
                        String setField = BeanUtil.setField(field.getName());
                        Method setMethod = cls.getMethod(setField, String.class);
                        setMethod.invoke(po, uuid);
                    } else {
                        pkValue = value;
                    }
                    continue;
                }
                //值类型适配处理
                String values2 = null;//用于替换？占位符
                if (value != null) {
                    if (field.getType().getName().equals("java.util.Date")) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        value = dateFormat.format(value);
                        values2 = todate;//"date_format(?, '%Y-%m-%d %H:%i:%s')";
                    }
                }

                //只有value值不等于null时，才计算该字段
                if (StringUtil.isNotEmpty(value)) {
                    if (flag) {
                        fieles += "," + fieldName;
                        values += ("," + (values2 == null ? "?" : values2));
                    } else {
                        fieles += "," + fieldName + "=" + (values2 == null ? "?" : values2);
                    }
                    objs.add(value);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        SavaEntity savaEntity = new SavaEntity();
        savaEntity.setFieles(fieles.substring(1));
        if (flag) {
            savaEntity.setValues(values.substring(1));
        } else {
            objs.add(pkValue);
        }
        savaEntity.setObjs(objs);
        return savaEntity;
    }

    protected <T> SavaListEntity poToParameter(List<T> pos, String pkName, boolean flag) {
        String fieles = "";//字段名
        String values = "";//值位置替代符
        Object pkValue = "";
        SavaListEntity savaListEntity = new SavaListEntity();
        List<Object[]> objsList = new ArrayList<>();
        T po = pos.get(0);
        Class<?> cls = po.getClass();
        // 获取该类所有属性名
        Field[] fields = ClassUtil.getAllFields(cls, ClassUtil.DB);
        // 声明Map对象，存储属性
        //方法集合
        List<Method> methodList = new ArrayList<>();
        //字段名集合
        List<String> fieldNameList = new ArrayList<>();
//        属性名集合
//        List<String> attributeNameList = new ArrayList<>();
        //级联关系集合
//        List<MyCascade> myCascadeList = new ArrayList<>();
        //属性集合
        List<Field> fieldList = new ArrayList<>();

        List objs = new ArrayList();
        for (Field field : fields) {
            try {
                // 获取要设置的属性的set方法名称
                String getField = BeanUtil.getField(field.getName());
                // 声明类函数方法，并获取和设置该方法型参类型
                Method getMethod = cls.getMethod(getField);
                Annotation[] annotations = getMethod.getAnnotations();
                String fieldName = field.getName();

                //遍历JPA注解
                fieldName = annotationsToFieldName(annotations, fieldName);
                if (StringUtil.isEmpty(fieldName)) {
                    continue;
                }

                methodList.add(getMethod);
//                attributeNameList.add(field.getName());
                fieldList.add(field);
                fieldNameList.add(fieldName);
//                myCascadeList.add(myCascade);

                Object value = getMethod.invoke(po);
                //主键使用新生成的UUID，然后将UUID反射进实体，传递给调用者
                if (fieldName.equals(pkName)) {
                    if (flag) {
                        String uuid = UUIDHexGenerator.getUUID();
                        fieles += "," + pkName;
                        values += ",?";
                        objs.add(uuid);
                        String setField = BeanUtil.setField(field.getName());
                        Method setMethod = cls.getMethod(setField, String.class);
                        setMethod.invoke(po, uuid);
                    } else {
                        pkValue = value;
                    }
                    continue;
                }
//                多对一关系处理
//                if (myCascade == MyCascade.MANY_TO_ONE) {
//                    String[] pk = getPrimaryKey(value.getClass()).split(",");
//                    String primaryKey = pk[1];
//                    String fieldName2 = pk[0];
//                    String getField2 = BeanUtil.getField(fieldName2);
//                    Method getMethod2 = value.getClass().getMethod(getField2);
//                    value = getMethod2.invoke(value);
//                }
                if (flag) {
                    fieles += "," + fieldName;
                    values += ",?";
                } else {
                    fieles += "," + fieldName + "=?";
                }
                //值类型适配处理
                value = getValue(field, value);

                objs.add(value);
            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
//                System.out.println(e.getMessage());
            } catch (IllegalAccessException e) {
                //e.printStackTrace();
//                System.out.println(e.getMessage());
            } catch (InvocationTargetException e) {
                //e.printStackTrace();
//                System.out.println(e.getMessage());
            }
        }
        if (!flag) {
            objs.add(pkValue);
        }
        objsList.add(objs.toArray());

        for (int q = 1; q < pos.size(); q++) {
            List objs2 = new ArrayList();
            Object pkValue2 = "";
            for (int i = 0; i < methodList.size(); i++) {
                try {
                    Method getMethod = methodList.get(i);
//                    String attributeName = attributeNameList.get(i);
                    Field field = fieldList.get(i);
                    String filedName = fieldNameList.get(i);
//                    MyCascade myCascade = myCascadeList.get(i);

                    Object value = getMethod.invoke(pos.get(q));
                    //主键使用新生成的UUID，然后将UUID反射进实体，传递给调用者
                    if (filedName.equals(pkName)) {
                        if (flag) {
                            String uuid = UUIDHexGenerator.getUUID();
                            objs2.add(uuid);
                            String setField = BeanUtil.setField(field.getName());
                            Method setMethod = cls.getMethod(setField, String.class);
                            setMethod.invoke(pos.get(q), uuid);
                        } else {
                            pkValue2 = value;
                        }
                        continue;
                    }
                    //多对一关系处理
//                    if (myCascade == MyCascade.MANY_TO_ONE) {
//                        String[] pk = getPrimaryKey(value.getClass()).split(",");
//                        String primaryKey = pk[1];
//                        String fieldName = pk[0];
//                        String getField2 = BeanUtil.getField(fieldName);
//                        Method getMethod2 = value.getClass().getMethod(getField2);
//                        value = getMethod2.invoke(value);
//                    }
                    //值类型适配处理
                    value = getValue(field, value);

                    objs2.add(value);
                } catch (NoSuchMethodException e) {
                    //e.printStackTrace();
                } catch (IllegalAccessException e) {
                    //e.printStackTrace();
                } catch (InvocationTargetException e) {
                    //e.printStackTrace();
                }
            }
            if (!flag) {
                objs2.add(pkValue2);
            }
            objsList.add(objs2.toArray());
        }
        savaListEntity.setFieles(fieles.substring(1));
        if (flag) {
            savaListEntity.setValues(values.substring(1));
        }
        savaListEntity.setObjs(objsList);
        return savaListEntity;
    }

    protected Object getValue(Field field, Object value) {
        if (field.getType().getName().equals("java.util.Date")) {
            if (value != null) {
                java.util.Date date = (java.util.Date) value;
                value = new java.sql.Date(date.getTime());
            }
        }
        return value;
    }


    /**
     * 解析集合中元素的类型
     *
     * @param field 属性
     * @return java.lang.Class
     * @author ChenWen
     * @descriptio 现已支持：【List】
     * @date 2019/7/12 11:51
     */
    protected Class judgeFieldType(Field field) {
        String name = field.getName();
        if (field.getGenericType() instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) field.getGenericType();
            //判断具体类的类型
            if (pt.getRawType().equals(List.class)) {
                // 判断泛型类的类型
                pt.getActualTypeArguments();
                Class cls = (Class) pt.getActualTypeArguments()[0];
                return cls;
            }
        }
        return null;
    }

}
