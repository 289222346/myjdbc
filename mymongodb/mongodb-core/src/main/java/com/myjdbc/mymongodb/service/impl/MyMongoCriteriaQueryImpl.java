package com.myjdbc.mymongodb.service.impl;

import com.mongodb.BasicDBObject;
import com.myjdbc.core.criteria.CriteriaQuery;
import com.myjdbc.core.criteria.impl.CriteriaQueryImpl;
import com.myjdbc.core.model.Criterion;
import com.myjdbc.core.service.ActionCriteriaQuery;
import com.myjdbc.core.util.ListUtil;
import com.myjdbc.core.util.ModelUtil;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.mymongodb.dao.MongoDAO;
import com.myjdbc.mymongodb.util.MongoUtil;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * mongodb的查询构造器操作实现类
 *
 * @Author: 陈文
 * @Date: 2020/4/20 10:59
 */
public class MyMongoCriteriaQueryImpl implements ActionCriteriaQuery {

    protected MongoDAO dao;

    protected MongoTemplate mongoTemplate;

    public MyMongoCriteriaQueryImpl(MongoTemplate mongoTemplate, MongoDAO dao) {
        this.mongoTemplate = mongoTemplate;
        this.dao = dao;
    }

    @Override
    public long findCount(CriteriaQuery criteriaQuery) {
        BasicDBObject query = spliceQuery(criteriaQuery);
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
        BasicDBObject query = spliceQuery(criteriaQuery);
        List<Bson> joinList = MongoUtil.handleForeign(criteriaQuery.getCls());
        List<T> list = dao.find(query, criteriaQuery.getCls(), criteriaQuery.getPag(), criteriaQuery.getOrder(), joinList);
        return list;
    }

    private <T> BasicDBObject spliceQuery(CriteriaQuery<T> criteriaQuery) {
        Class cls = criteriaQuery.getCls();

        criteriaQuery = spliceCriteriaQuery(criteriaQuery);
        //MongoDB的Query查询构造器
        BasicDBObject query = new BasicDBObject();
        //遍历Myjdbc查询构造器，并适配成MongoDB的Query查询构造器
        criteriaQuery.getCriteriaMap().values().forEach(criteria -> {
            //查询条件
            List<Criterion> criterions = criteria.getCriterions();
            //限定字段名
            String filedName = ModelUtil.getPropertyName(cls, criteria.getFieldName());
            //获取匹配方式
            getCriteria(query, filedName, criterions);
        });
        return query;

    }

    private void getCriteria(BasicDBObject query, String filedName, List<Criterion> criterionList) {
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

    private <T> CriteriaQuery<T> spliceCriteriaQuery(CriteriaQuery<T> criteriaQuery) {
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


}
