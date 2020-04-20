package com.myjdbc.mymongodb.service.impl;

import com.myjdbc.core.criteria.CriteriaQuery;
import com.myjdbc.core.criteria.impl.CriteriaQueryImpl;
import com.myjdbc.core.service.ActionRetrieve;
import com.myjdbc.core.service.ActionSaveAndUpdate;
import com.myjdbc.core.util.ListUtil;
import com.myjdbc.mymongodb.util.MongoUtil;
import io.swagger.annotations.ApiModel;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * mongodb的查询构造器操作实现类
 *
 * @Author: 陈文
 * @Date: 2020/4/20 10:59
 */
public class MyMongoSaveAndUpdateImpl implements ActionSaveAndUpdate {

    private  MongoTemplate mongoTemplate;

    private  ActionRetrieve actionRetrieve;

    public MyMongoSaveAndUpdateImpl(MongoTemplate mongoTemplate, ActionRetrieve actionRetrieve) {
        this.mongoTemplate = mongoTemplate;
        this.actionRetrieve = actionRetrieve;
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

    @Override
    public <T> int delete(T t) {
        if (ListUtil.isList(t)) {
            return FAILURE_NO_LIST;
        }
        try {
            mongoTemplate.remove(t, MongoUtil.getModelName(t.getClass()));
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
            mongoTemplate.remove(query, MongoUtil.getModelName(cls));
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
            List<Serializable> ids = new ArrayList<>();
            for (T t : list) {
                Document document = MongoUtil.poToDocument(t);
                Serializable id = (Serializable) document.get("_id");
                ids.add(id);
            }
            query.addCriteria(Criteria.where("_id").in(ids.toArray()));
            mongoTemplate.remove(query, MongoUtil.getModelName(list.get(0).getClass()));
        } catch (Exception e) {
            e.printStackTrace();
            return FAILURE_INSIDE_ERROR;
        }
        return SUCCESS;
    }

    @Override
    public <T> List<T> findAndDelete(Class<T> cls, Map<String, Object> query) {
        return mongoTemplate.findAllAndRemove(MongoUtil.mapToQuery(query), cls, MongoUtil.getModelName(cls));
    }

    @Override
    public <T> List<T> findAndDelete(CriteriaQuery<T> criteriaQuery) {
        List<T> list = actionRetrieve.findAll(criteriaQuery);
        int code = batchDelete(list);
        if (code != SUCCESS) {
            throw new Error(getDesc(code));
        }
        return list;
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
            Document document = MongoUtil.poToDocument(t);
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
        T tempT = actionRetrieve.findById(cls, id);
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
        CriteriaQuery criteriaQuery = new CriteriaQueryImpl(cls);
        criteriaQuery.in("_id", ids.toArray());
        List<T> list = actionRetrieve.findAll(criteriaQuery);
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

    /**
     * 替换操作
     *
     * @Author: 陈文
     * @Date: 2020/4/20 11:49
     */
    private int replaceAction(Serializable id, Document document, String collectionName) {
        //查询条件
        Bson filter = new Document("_id", id);
        mongoTemplate.getCollection(collectionName).replaceOne(filter, document);
        return SUCCESS;
    }

}
