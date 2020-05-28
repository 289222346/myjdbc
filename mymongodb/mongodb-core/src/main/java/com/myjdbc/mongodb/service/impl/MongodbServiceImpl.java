package com.myjdbc.mongodb.service.impl;

import com.myjdbc.core.config.properties.DbConfig;
import com.myjdbc.core.criteria.CriteriaQuery;
import com.myjdbc.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 基础数据服务层的Mongodb实现
 * {@code actionRetrieve} 是查询操作
 * {@code actionSaveAndUpdate} 是增删操作
 * <p>
 * {@link MongoTemplate} 本实现，完全依赖于spring框架下的mongo模板，底层完全使用该模板。
 * 本实现的作用是使myjdbc用户保持一致的操作习惯，而不用关系mongo底层原理(包括mongodb模板).
 *
 * @Author: 陈文
 * @Date: 2020/4/20 12:05
 */
@Service("mongodbService")
public abstract class MongodbServiceImpl extends MyMongoAction implements BaseService {

    @Autowired
    public MongodbServiceImpl(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    @Autowired
    public MongodbServiceImpl(MongoTemplate mongoTemplate, DbConfig dbConfig) {
        super(mongoTemplate, dbConfig);
    }

    @Autowired
    public MongodbServiceImpl(MongoTemplate mongoTemplate, String ip, Integer port, String databaseName) {
        super(mongoTemplate, ip + ":" + port, databaseName);
    }

    @Autowired
    public MongodbServiceImpl(MongoTemplate mongoTemplate, String url, String databaseName) {
        super(mongoTemplate, url, databaseName);
    }

    @Override
    public long findCount(Class cls, Serializable id) {
        return actionRetrieve.findCount(cls, id);
    }

    @Override
    public long findCount(String collectionName, Serializable id) {
        return actionRetrieve.findCount(collectionName, id);
    }

    @Override
    public long findCount(CriteriaQuery criteriaQuery) {
        return actionRetrieve.findCount(criteriaQuery);
    }

    @Override
    public <T> T findById(Class<T> cls, Serializable id) {
        return actionRetrieve.findById(cls, id);
    }

    @Override
    public <T> List<T> findAll(Class<T> cls) {
        return actionRetrieve.findAll(cls);
    }

    @Override
    public <T> List<T> findAll(Class<T> cls, Map<String, Object> query) {
        return actionRetrieve.findAll(cls, query);
    }

    @Override
    public <T> List<T> criteriaEq(Class<T> cls, String fieldName, Object filedValue) {
        return actionRetrieve.criteriaEq(cls, fieldName, filedValue);
    }

    @Override
    public <T> List<T> criteriaIn(Class<T> cls, String fieldName, Object[] values) {
        return actionRetrieve.criteriaIn(cls, fieldName, values);
    }

    @Override
    public <T> List<T> findAll(CriteriaQuery<T> criteriaQuery) {
        return actionRetrieve.findAll(criteriaQuery);
    }

    @Override
    public <T> int save(T t) {
        return actionSaveAndUpdate.save(t);
    }

    @Override
    public <T> int batchSave(List<T> list) {
        return actionSaveAndUpdate.batchSave(list);
    }

    @Override
    public <T> int update(T t) {
        return actionSaveAndUpdate.update(t);
    }

    @Override
    public <T> int replace(T t) {
        return actionSaveAndUpdate.replace(t);
    }

    @Override
    public <T> int delete(T t) {
        return actionSaveAndUpdate.delete(t);
    }

    @Override
    public int delete(Serializable id, Class<?> cls) {
        return actionSaveAndUpdate.delete(id, cls);
    }

    @Override
    public <T> int batchDelete(List<T> list) {
        return actionSaveAndUpdate.batchDelete(list);
    }

    @Override
    public <T> List<T> findAndDelete(Class<T> cls, Map<String, Object> query) {
        return actionSaveAndUpdate.findAndDelete(cls, query);
    }

    @Override
    public <T> List<T> findAndDelete(CriteriaQuery<T> criteriaQuery) {
        return actionSaveAndUpdate.findAndDelete(criteriaQuery);
    }

    @Override
    public void transactionStatus() throws SQLException {

    }

    @Override
    public void transactionStatus(boolean connectionFlag) throws SQLException {

    }

    @Override
    public void transactionRollback() throws SQLException {

    }

    @Override
    public void transactionCommit() throws SQLException {

    }

    @Override
    public void setConnection(Connection connection) {

    }

    @Override
    public void closeConnection() {

    }
}
