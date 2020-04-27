package com.myjdbc.mymongodb.service.impl;

import com.mongodb.BasicDBObject;
import com.myjdbc.core.constants.OpType;
import com.myjdbc.core.service.ActionRetrieve;
import com.myjdbc.core.util.ListUtil;
import com.myjdbc.core.util.ModelUtil;
import com.myjdbc.mymongodb.dao.MongoDAO;
import com.myjdbc.mymongodb.util.MongoUtil;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * mongodb的查询构造器操作实现类
 *
 * @Author: 陈文
 * @Date: 2020/4/20 10:59
 */
public class MyMongoRetrieveImpl extends MyMongoCriteriaQueryImpl implements ActionRetrieve {

    public MyMongoRetrieveImpl(MongoTemplate mongoTemplate, MongoDAO dao) {
        super(mongoTemplate, dao);
    }

    @Override
    public long findCount(Class cls, Serializable id) {
        return findCount(ModelUtil.getModelName(cls), id);
    }

    @Override
    public long findCount(String collectionName, Serializable id) {
        BasicDBObject query = findByIdQuery(id);
        return dao.findCount(query, collectionName);
    }

    @Override
    public <T> T findById(Class<T> cls, Serializable id) {
        BasicDBObject query = findByIdQuery(id);
        List<T> list = dao.find(query, cls);
        if (ListUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    private BasicDBObject findByIdQuery(Serializable id) {
        BasicDBObject condition = MongoUtil.toCondition(OpType.EQ, id);
        BasicDBObject query = new BasicDBObject("_id", condition);
        return query;
    }

    @Override
    public <T> List<T> findAll(Class<T> cls) {
        return dao.find(cls);
    }

    @Override
    public <T> List<T> findAll(Class<T> cls, Map<String, Object> map) {
        return mongoTemplate.find(MongoUtil.mapToQuery(map), cls, ModelUtil.getModelName(cls));
    }


}
