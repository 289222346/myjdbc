package com.myjdbc.mymongodb.service.impl;

import com.mongodb.BasicDBObject;
import com.myjdbc.core.criteria.CriteriaQuery;
import com.myjdbc.core.criteria.impl.CriteriaQueryImpl;
import com.myjdbc.core.model.Criterion;
import com.myjdbc.core.service.ActionCriteriaQuery;
import com.myjdbc.core.util.ModelUtil;
import com.myjdbc.mymongodb.dao.MongoDAO;
import com.myjdbc.mymongodb.util.MongoUtil;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * mongodb的查询构造器操作实现类
 *
 * @Author: 陈文
 * @Date: 2020/4/20 10:59
 */
public class MyMongoCriteriaQueryImpl implements ActionCriteriaQuery {

    protected MongoDAO dao;

    public MyMongoCriteriaQueryImpl(MongoDAO dao) {
        this.dao = dao;
    }

    @Override
    public long findCount(CriteriaQuery criteriaQuery) {
        BasicDBObject query = MongoUtil.spliceQuery(criteriaQuery);
        return dao.findCount(query, criteriaQuery.getModelName());
    }

    @Override
    public <T> List<T> criteriaEq(Class<T> cls, String fieldName, Object filedValue) {
        CriteriaQuery<T> criteriaQuery = new CriteriaQueryImpl<>(cls);
        criteriaQuery.eq(fieldName, filedValue);
        return findAll(criteriaQuery);
    }

    @Override
    public <T> List<T> criteriaIn(Class<T> cls, String fieldName, Object[] values) {
        CriteriaQuery<T> criteriaQuery = new CriteriaQueryImpl<>(cls);
        criteriaQuery.in(fieldName, values);
        return findAll(criteriaQuery);
    }

    @Override
    public <T> List<T> findAll(CriteriaQuery<T> criteriaQuery) {
        BasicDBObject query = MongoUtil.spliceQuery(criteriaQuery);
        List<Bson> joinList = MongoUtil.handleForeign(criteriaQuery.getCls());
        List<T> list = dao.find(query, criteriaQuery.getCls(), criteriaQuery.getPag(), criteriaQuery.getOrder(), joinList);
        return list;
    }


}
