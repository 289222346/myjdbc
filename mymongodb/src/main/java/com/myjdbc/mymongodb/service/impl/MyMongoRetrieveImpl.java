package com.myjdbc.mymongodb.service.impl;

import com.myjdbc.core.service.ActionRetrieve;
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

    private MongoTemplate mongoTemplate;

    public MyMongoRetrieveImpl(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public <T> T findById(Class<T> cls, Serializable id) {
        return mongoTemplate.findById(id, cls, MongoUtil.getModelName(cls));
    }

    @Override
    public <T> List<T> findAll(Class<T> cls) {
        return mongoTemplate.findAll(cls, MongoUtil.getModelName(cls));
    }

    @Override
    public <T> List<T> findAll(Class<T> cls, Map<String, Object> map) {
        return mongoTemplate.find(MongoUtil.mapToQuery(map), cls, MongoUtil.getModelName(cls));
    }


}
