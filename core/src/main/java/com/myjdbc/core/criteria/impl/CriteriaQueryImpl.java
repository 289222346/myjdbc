package com.myjdbc.core.criteria.impl;

import com.myjdbc.core.criteria.util.Restrictions;
import com.myjdbc.core.entity.Pag;
import com.myjdbc.core.entity.Criteria;
import com.myjdbc.core.entity.OrderBo;
import com.myjdbc.core.constants.OrderType;
import com.myjdbc.core.criteria.CriteriaQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询条件构造器实现
 *
 * @author 陈文
 * @Description
 * @Date: 2020/4/18 14:22
 */
public class CriteriaQueryImpl<T> implements CriteriaQuery {

    /**
     * 分页属性
     */
    protected Pag pag;
    /**
     * 查询实体
     */
    protected final Class<T> cls;
    /**
     * 实体查询条件
     */
    protected final T queryT;
    /**
     * 查询条件集合
     */
    protected final Map<String, Criteria> criteriaMap = new HashMap<>();
    /**
     * 排序条件
     */
    protected OrderBo orderBo;

    public CriteriaQueryImpl(Class<T> cls) {
        this.cls = cls;
        queryT = null;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/10  10:53
     * @Description 实体中的参数转换成eq参数
     */
    public CriteriaQueryImpl(Class<T> cls, T queryT) {
        this.cls = cls;
        this.queryT = queryT;
    }

    /**
     * 添加一个查询标准
     * <p>
     * 话分两头，情况分两种。
     * 1.{@code criteriaMap}查询标准集合中没有该字段的查询标准，则将该标准添加进去
     * 2.{@code criteriaMap}查询标准集合中已经有该字段的查询标准，
     * 则将该标准的查询条件添加给已有标准，让已有标准成为条件集合。
     *
     * @param criteria 一个查询标准
     */
    private void add(Criteria criteria) {
        String filedName = criteria.getFieldName();
        if (criteriaMap.containsKey(filedName)) {
            criteriaMap.get(filedName).addCriterion(criteria);
        } else {
            criteriaMap.put(filedName, criteria);
        }
    }

    @Override
    public void eq(String fieldName, Object value) {
        add(Restrictions.eq(fieldName, value));
    }

    @Override
    public void gt(String fieldName, Object value) {
        add(Restrictions.gt(fieldName, value));
    }

    @Override
    public void lt(String fieldName, Object value) {
        add(Restrictions.lt(fieldName, value));
    }

    @Override
    public void ge(String fieldName, Object value) {
        add(Restrictions.ge(fieldName, value));
    }

    @Override
    public void le(String fieldName, Object value) {
        add(Restrictions.le(fieldName, value));
    }

    @Override
    public void like(String fieldName, Object value) {
        add(Restrictions.like(fieldName, value));
    }

    @Override
    public void in(String fieldName, Object... value) {
        add(Restrictions.in(fieldName, value));
    }

    @Override
    public OrderBo getOrder() {
        return orderBo;
    }

    @Override
    public void setOrder(OrderType orderType, String... fieldNames) {
        orderBo = new OrderBo(fieldNames, orderType);
    }

    @Override
    public void eqProperty(String fieldName, String fieldName2) {
        add(Restrictions.eqProperty(fieldName, fieldName2));
    }

    @Override
    public Class getCls() {
        return cls;
    }

    @Override
    public Map<String, Criteria> getCriteriaMap() {
        return criteriaMap;
    }

    @Override
    public long getTotal() {
        return pag.getTotal();
    }

    @Override
    public void setTotal(long total) {
        pag.setTotal(total);
    }

    @Override
    public Pag getPag() {
        return pag;
    }

    @Override
    public void setPag(int page, int rows) {
        pag = new Pag(page, rows);
    }

    @Override
    public int getPage() {
        return pag.getPage();
    }

    @Override
    public int getRows() {
        return pag.getRows();
    }

}
