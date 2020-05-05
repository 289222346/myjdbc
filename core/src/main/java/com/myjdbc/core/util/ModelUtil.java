package com.myjdbc.core.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.springframework.data.annotation.Id;

import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelUtil {

    /**
     * 内部模型属性集合存储器
     */
    private static final Map<String, Map<String, String>> MODEL_PROPERTY_MAP = new HashMap<>();

    /**
     * 获取属性名称
     * 如果存在{@link ApiModelProperty}注解的 ,且其{@code name}参数不为空，则用{@code name}作为属性名称
     * 如果不存在{@link ApiModelProperty}注解的 ,或者{@code name}参数为空的，则使用属性本身名字
     *
     * @param field 实体属性
     * @return 经过处理后的属性名称
     * @Author 陈文
     * @Date 2020/4/21  17:03
     */
    public static String getPropertyName(Field field) {
        Id id = field.getAnnotation(Id.class);
        //mongodb中，默认使用_id作为主键
        // 因为mongodb会自动为该字段创建索引，所以主键不管在实体中是何名称，到数据库统一使用_id
        if (id != null) {
            return "_id";
        }
        ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
        /**
         * 没有模型属性，或者{@code name}参数为空，则返回属性原本的名字
         */
        if (apiModelProperty == null || StringUtil.isEmpty(apiModelProperty.name())) {
            return field.getName();
        }
        //返回模型属性重定义的名字
        return apiModelProperty.name();
    }

    public static String getPropertyName(Class cls, String fieldName) {
        if (cls == null) {
            return fieldName;
        }
        Field[] fields = ClassUtil.getValidFields(cls);
        for (Field field : fields) {
            //符合其中一个属性名，则返回
            if (fieldName.equals(field)) {
                return ModelUtil.getPropertyName(field);
            }
        }
        return fieldName;
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

}
