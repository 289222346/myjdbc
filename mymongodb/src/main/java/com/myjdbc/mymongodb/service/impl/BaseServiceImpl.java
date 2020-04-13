package com.myjdbc.mymongodb.service.impl;

import com.myjdbc.core.constants.OpType;
import com.myjdbc.core.criteria.CriteriaQuery;
import com.myjdbc.core.criteria.impl.CriteriaQueryFactory;
import com.myjdbc.core.entity.Criterion;
import com.myjdbc.core.service.ActionSaveAndUpdate;
import com.myjdbc.core.service.BaseService;
import com.myjdbc.core.util.ListUtil;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.mymongodb.util.MongoDBUtil;
import io.swagger.annotations.ApiModel;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service("baseService")
public class BaseServiceImpl implements BaseService {

    @Autowired
    protected MongoTemplate mongoTemplate;

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

    @Override
    public <T> T findById(Class<T> cls, Serializable id) {
        return mongoTemplate.findById(id, cls, getModelName(cls));
    }

    @Override
    public <T> List<T> findAll(Class<T> cls) {
        return mongoTemplate.findAll(cls, getModelName(cls));
    }

    @Override
    public <T> List<T> findAll(Class<T> cls, Map<String, Object> query) {
        Query query1 = new Query();
        query.forEach((key, value) -> {
            query1.addCriteria(Criteria.where(key).is(value));
        });
        return mongoTemplate.find(query1, cls, getModelName(cls));
    }

    @Override
    public <T> int delete(T t) {
        if (ListUtil.isList(t)) {
            return FAILURE_NO_LIST;
        }
        try {
            mongoTemplate.remove(t, getModelName(t.getClass()));
        } catch (Exception e) {
            e.printStackTrace();
            return FAILURE_INSIDE_ERROR;
        }
        return SUCCESS;
    }

    @Override
    public int delete(Serializable id, Class<?> cls) {
        try {
            Query query = Query.query(Criteria.where("_id").is(id));
            mongoTemplate.remove(query, getModelName(cls));
        } catch (Exception e) {
            e.printStackTrace();
            return FAILURE_INSIDE_ERROR;
        }
        return SUCCESS;
    }

    @Override
    public <T> int batchDelete(List<T> list) {
        if (ListUtil.isEmpty(list)) {
            return FAILURE_ALL_NULL;
        }
        try {
            Query query = new Query();
            for (T t : list) {
                Document document = MongoDBUtil.poToDocument(t);
                Serializable id = (Serializable) document.get("_id");
                query.addCriteria(Criteria.where("_id").is(id));
            }
            mongoTemplate.remove(query, getModelName(list.get(0).getClass()));
        } catch (Exception e) {
            e.printStackTrace();
            return FAILURE_INSIDE_ERROR;
        }
        return SUCCESS;
    }

    @Override
    public <T> int save(T t) {
        return saveAndUpdate(t, ActionSaveAndUpdate.ACTION_SAVE);
    }

    @Override
    public <T> int batchSave(List<T> list) {
        if (ListUtil.isEmpty(list)) {
            return FAILURE_ALL_NULL;
        }
        for (T t : list) {
            save(t);
        }
        return SUCCESS;
    }

    @Override
    public <T> int update(T t) {
        return saveAndUpdate(t, ActionSaveAndUpdate.ACTION_UPDATE);
    }

    @Override
    public <T> int replace(T t) {
        return saveAndUpdate(t, ActionSaveAndUpdate.ACTION_REPLACE);
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2020/4/11  9:29
     * @Description 保存操作前置判断
     */
    private <T> int saveAndUpdate(T t, int actionType) {
        try {
            if (ListUtil.isList(t)) {
                return FAILURE_NO_LIST;
            }
            ApiModel apiModel = t.getClass().getAnnotation(ApiModel.class);
            if (apiModel == null) {
                return FAILURE_LACK_MODEL;
            }
            /***
             * 前置条件判断
             */
            //表（集合）名
            String collectionName = apiModel.value();
            //要保存的mongoDB文件（对象）
            Document document = MongoDBUtil.poToDocument(t);
            if (document == null || document.size() == 0) {
                //保存失败，对象所有属性为空
                return FAILURE_ALL_NULL;
            }
            //序列化ID
            Serializable id = (Serializable) document.get("_id");
            if (ObjectUtils.isEmpty(id)) {
                //保存失败，IP属性为空
                return FAILURE_IP_NULL;
            }
            /**
             * 进入对应操作
             */
            if (actionType == ActionSaveAndUpdate.ACTION_SAVE) {
                //执行更新操作
                return saveAction(id, document, collectionName, t.getClass());
            }
            if (actionType == ActionSaveAndUpdate.ACTION_UPDATE) {
                //执行更新操作
                return updateAction(id, document, collectionName);
            }
            if (actionType == ActionSaveAndUpdate.ACTION_REPLACE) {
                //执行更新操作
                return replaceAction(id, document, collectionName);
            }
            //找不到操作响应
            return FAILURE_TYPE_NULL;
        } catch (Exception e) {
            return FAILURE_INSIDE_ERROR;
        }
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2020/4/11  9:28
     * @Description 保存操作
     */
    private <T> int saveAction(Serializable id, Document document, String collectionName, Class<T> cls) {
        T tempT = this.findById(cls, id);
        if (tempT != null) {
            //保存失败，ID冲突
            return FAILURE_ID_CLASH;
        }
        mongoTemplate.getCollection(collectionName).insertOne(document);
        //保存成功
        return SUCCESS;
    }

    /**
     * @return
     * @Date 2020/4/13  21:10
     * @Author 陈文
     * @Description 批量保存操作
     */
    private <T> int batchSaveAction(List<Serializable> ids, List<Document> documents, String collectionName, Class<T> cls) {
        //如果要保存的数据，有一部分已经存在于数据库中，则整体不再保存
        CriteriaQuery criteriaQuery = CriteriaQueryFactory.creatCriteriaQuery(cls);
        criteriaQuery.in("_id", ids.toArray());
        List<T> list = findAll(criteriaQuery);
        if (ListUtil.isNotEmpty(list)) {
            return FAILURE_ID_CLASH;
        }
        mongoTemplate.getCollection(collectionName).insertMany(documents);
        //保存成功
        return SUCCESS;
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2020/4/11  9:28
     * @Description 更新操作
     */
    private int updateAction(Serializable id, Document document, String collectionName) {
        //查询条件
        Bson filter = new Document("_id", id);
        //存储对象
        Bson update = new Document("$set", document);
        mongoTemplate.getCollection(collectionName).updateOne(filter, update);
        return SUCCESS;
    }

    private int replaceAction(Serializable id, Document document, String collectionName) {
        //查询条件
        Bson filter = new Document("_id", id);
        mongoTemplate.getCollection(collectionName).replaceOne(filter, document);
        return SUCCESS;
    }


    private String getModelName(Class cls) {
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

    @Override
    public <T> List<T> criteriaEq(Class<T> cls, String fieldName, Object filedValue) {
        CriteriaQuery<T> criteriaQuery = CriteriaQueryFactory.creatCriteriaQuery(cls);
        return findAll(criteriaQuery);
    }

    @Override
    public <T> List<T> criteriaIn(Class<T> cls, String fieldName, Object[] values) {
        return null;
    }

    @Override
    public <T> List<T> findAll(CriteriaQuery<T> criteriaQuery) {
        //MongoDB的Query查询构造器
        Query query = new Query();
        //遍历Myjdbc查询构造器，并适配成MongoDB的Query查询构造器
        criteriaQuery.getCriteriaList().forEach(criteria -> {
            //查询条件
            Criterion criterion = criteria.getCriterion();
            //限定字段名
            String filedName = criterion.getFieldName();
            //限定值
            List<Object> values = criteria.getFieldValue();
            //获取匹配方式
            getCriteria(query, values, filedName, criterion.getOp());
        });
        //集合名称（可以认为是MongoDB中的数据库表名称）
        String collectionName = getModelName(criteriaQuery.getCls());
        //判断是否需要分页
        if (criteriaQuery.getPag() != null) {
            //总数据量
            long total = mongoTemplate.count(query, collectionName);
            criteriaQuery.setTotal(total);
            //当前页
            int page = criteriaQuery.getPage();
            //每页显示数据
            int rows = criteriaQuery.getRows();
            //分页查询条件
            query.with(PageRequest.of(page, rows));
        }
        List<T> list = mongoTemplate.find(query, criteriaQuery.getCls(), collectionName);
        return list;
    }

    /**
     * @param query     mongoDB查询器
     * @param values    限定值
     * @param filedName 限定字段名
     * @param op        限定条件
     * @return
     * @Author 陈文
     * @Date 2020/4/13  21:19
     * @Description 不加注释，反正加了你们也看不懂
     */
    private void getCriteria(Query query, List<Object> values, String filedName, OpType op) {

        //完全相等
        if (op == OpType.EQ) {
            values.forEach(value -> {
                query.addCriteria(Criteria.where(filedName).is(value));
            });
            return;
        }

        //模糊查询
        if (op == OpType.LIKE) {
            values.forEach(value -> {
                //正则表达式
                Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
                query.addCriteria(Criteria.where(filedName).regex(pattern));
            });
            return;
        }

        //包含相等
        if (op == OpType.IN) {
            query.addCriteria(Criteria.where(filedName).in(values));
            return;
        }
    }

    @Override
    public int getCount(CriteriaQuery criteriaQuery) {
        return 0;
    }
}
