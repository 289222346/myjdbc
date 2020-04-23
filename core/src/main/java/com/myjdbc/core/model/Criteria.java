package com.myjdbc.core.model;

import com.myjdbc.core.util.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 字段查询条件
 *
 * @Author 陈文
 * @Date: 2020/4/18 12:48
 * @Description 不加注释，反正加了你们也看不懂
 */
public class Criteria {

    /**
     * 属性名
     */
    private final String fieldName;
    /**
     * 限定条件集
     */
    private final List<Criterion> criterions = new ArrayList<>();

    public Criteria(String fieldName, Criterion criterion) {
        this.fieldName = fieldName;
        criterions.add(criterion);
    }

    /**
     * 将同一字段的两个查询条件汇总
     *
     * @param criteria 查询条件
     */
    public void addCriterion(Criteria criteria) {
        List<Criterion> criterions = criteria.getCriterions();
        if (ListUtil.isNotEmpty(criterions)) {
            this.criterions.addAll(criterions);
        }
    }

    public String getFieldName() {
        return fieldName;
    }

    public List<Criterion> getCriterions() {
        return criterions;
    }
}
