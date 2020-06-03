package com.myjdbc.mymongodb.service.impl;

import com.mongodb.BasicDBObject;
import com.myjdbc.core.annotations.IDAutoGenerator;
import com.myjdbc.core.criteria.CriteriaQuery;
import com.myjdbc.core.idgenerator.IdGeneratorUtil;
import com.myjdbc.core.service.ActionRetrieve;
import com.myjdbc.core.service.ActionSaveAndUpdate;
import com.myjdbc.core.util.AnnotationUtil;
import com.myjdbc.core.util.ClassUtil;
import com.myjdbc.core.util.ListUtil;
import com.myjdbc.core.util.ModelUtil;
import com.myjdbc.mymongodb.dao.MongoDAO;
import com.myjdbc.mymongodb.model.SaveAndUpdateBO;
import com.myjdbc.mymongodb.util.MongoUtil;
import io.swagger.annotations.ApiModelProperty;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
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

    private Logger logger = LoggerFactory.getLogger(MyMongoSaveAndUpdateImpl.class);


    protected MongoDAO dao;

    private ActionRetrieve actionRetrieve;

    public MyMongoSaveAndUpdateImpl(ActionRetrieve actionRetrieve, MongoDAO dao) {
        this.dao = dao;
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
        List<SaveAndUpdateBO> saveAndUpdateBOList = new ArrayList<>();
        //对所有操作对象进行前置检查
        for (T t : list) {
            SaveAndUpdateBO saveAndUpdateBO = saveAndUpdateChecking(t, ACTION_SAVE);
            //只要其中一个对象，检查不通过，立即返回错误代码
            if (saveAndUpdateBO.getCode() != ACTION_SAVE) {
                return saveAndUpdateBO.getCode();
            }
            saveAndUpdateBOList.add(saveAndUpdateBO);
        }

        batchSaveAction(saveAndUpdateBOList);
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
            BasicDBObject filter = MongoUtil.modelToFilter(t);
            dao.delete(t.getClass(), filter);
        } catch (Exception e) {
            e.printStackTrace();
            return FAILURE_INSIDE_ERROR;
        }
        return SUCCESS;
    }

    @Override
    public int delete(Serializable id, Class<?> cls) {
        try {
            BasicDBObject filter = MongoUtil.idToFilter(id);
            dao.delete(cls, filter);
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
        for (T t : list) {
            delete(t);
        }
//        try {
//            CriteriaQuery criteriaQuery = new CriteriaQueryImpl(ArrayList.class);
//            List<Serializable> ids = new ArrayList<>();
//            for (T t : list) {
//                Document document = MongoUtil.mongoPOJOToDocument(t);
//                Serializable id = (Serializable) document.get("_id");
//                ids.add(id);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return FAILURE_INSIDE_ERROR;
//        }
        return SUCCESS;
    }

    @Override
    public <T> List<T> findAndDelete(Class<T> cls, Map<String, Object> query) {
        List<T> list = actionRetrieve.findAll(cls, query);
        batchDelete(list);
        return list;
    }

    @Override
    public <T> List<T> findAndDelete(CriteriaQuery<T> criteriaQuery) {
        List<T> list = actionRetrieve.findAll(criteriaQuery);
        if (ListUtil.isEmpty(list)) {
            return null;
        }

        int code = batchDelete(list);
        if (code != SUCCESS) {
            throw new Error(getDesc(code));
        }
        return list;
    }

    /**
     * 保存与更新（替换）
     *
     * @param t          执行操作的实体
     * @param actionType 操作类型
     * @param <T>        执行操作的实体类型
     * @return 操作结果码
     * @Author 陈文
     * @Date 2020/4/11  9:29
     */
    private <T> int saveAndUpdate(T t, int actionType) {
        //前置检查结果
        SaveAndUpdateBO saveAndUpdateBO = saveAndUpdateChecking(t, actionType);
        if (actionType != saveAndUpdateBO.getCode()) {
            //操作类型已经改变，说明内部发生了错误
            //直接将错误码返回即可
            return saveAndUpdateBO.getCode();
        }
        //进行操作，并返回结果码
        return saveAndUpdateAction(saveAndUpdateBO);
    }

    /**
     * 保存与更新（替换）
     * 前置检查
     *
     * @param t          执行操作的实体
     * @param actionType 操作类型
     * @param <T>        执行操作的实体类型
     * @return {@link SaveAndUpdateBO}
     * @Author 陈文
     * @Date 2020/4/11  9:29
     */
    private <T> SaveAndUpdateBO saveAndUpdateChecking(T t, int actionType) {
        SaveAndUpdateBO saveAndUpdateBO = new SaveAndUpdateBO();
        Class<?> cls = t.getClass();
        try {
            if (ListUtil.isList(t)) {
                saveAndUpdateBO.setCode(FAILURE_NO_LIST);
                logger.error(getDesc(FAILURE_NO_LIST));
                return saveAndUpdateBO;
            }

            /***
             * 前置条件判断
             */
            //表（集合）名
            //要保存的mongoDB文件（对象）
            Document document = MongoUtil.mongoPOJOToDocument(t);
            if (document == null || document.size() == 0) {
                //保存失败，对象所有属性为空
                saveAndUpdateBO.setCode(FAILURE_ALL_NULL);
                logger.error(getDesc(FAILURE_ALL_NULL));
                return saveAndUpdateBO;
            }

            //校验必填字段是否非空
            List<Field> fieldList = ClassUtil.getAllFieldsList(cls);
            for (Field field : fieldList) {
                ApiModelProperty apiModelProperty = AnnotationUtil.get(field, ApiModelProperty.class);
                if (apiModelProperty != null) {
                    if (apiModelProperty.required()) {
                        String propertyName = MongoUtil.getPropertyName(field);
                        if (document.get(propertyName) == null) {
                            saveAndUpdateBO.setCode(FAILURE_REQUIRED_NULL);
                            logger.error(getDesc(FAILURE_REQUIRED_NULL) + t.getClass().getName() + ":" + propertyName);
                            return saveAndUpdateBO;
                        }
                    }
                }
            }

            //序列化ID
            Serializable id = (Serializable) document.get("_id");
            //判断ID
            if (ObjectUtils.isEmpty(id)) {
                //允许ID生成
                if (actionType == ActionSaveAndUpdate.ACTION_SAVE) {
                    IDAutoGenerator idAutoGenerator = AnnotationUtil.get(cls, IDAutoGenerator.class);
                    id = IdGeneratorUtil.generateID(idAutoGenerator);
                    if (ObjectUtils.isEmpty(id)) {
                        //操作失败，ID属性为空
                        saveAndUpdateBO.setCode(FAILURE_ID_NULL);
                        logger.error(getDesc(FAILURE_ID_NULL));
                        return saveAndUpdateBO;
                    } else {
                        document.put("_id", id);
                    }
                } else {
                    //操作失败，ID属性为空
                    saveAndUpdateBO.setCode(FAILURE_ID_NULL);
                    logger.error(getDesc(FAILURE_ID_NULL));
                    return saveAndUpdateBO;
                }
            }

            //校验结束，开始保存处理
            String collectionName = ModelUtil.getModelName(cls);
            saveAndUpdateBO.setCode(actionType);
            saveAndUpdateBO.setId(id);
            saveAndUpdateBO.setDocument(document);
            saveAndUpdateBO.setCollectionName(collectionName);
            return saveAndUpdateBO;
        } catch (Exception e) {
            saveAndUpdateBO.setCode(FAILURE_INSIDE_ERROR);
            logger.error(getDesc(FAILURE_INSIDE_ERROR));
            e.printStackTrace();
            return saveAndUpdateBO;
        }
    }

    /**
     * 保存与更新（替换）
     * 执行操作
     *
     * @param saveAndUpdateBO {@link SaveAndUpdateBO}
     * @return 操作结果码
     * @Author 陈文
     * @Date 2020/4/11  9:29
     */
    private int saveAndUpdateAction(SaveAndUpdateBO saveAndUpdateBO) {
        int actionType = saveAndUpdateBO.getCode();
        Serializable id = saveAndUpdateBO.getId();
        Document document = saveAndUpdateBO.getDocument();
        String collectionName = saveAndUpdateBO.getCollectionName();
        /**
         * 进入对应操作
         */
        if (actionType == ActionSaveAndUpdate.ACTION_SAVE) {
            //执行保存操作
            return saveAction(id, document, collectionName);
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
        logger.error(getDesc(FAILURE_TYPE_NULL));
        return FAILURE_TYPE_NULL;
    }

    /**
     * @return
     * @Author 陈文
     * @Date 2020/4/11  9:28
     * @Description 保存操作
     */
    private <T> int saveAction(Serializable id, Document document, String collectionName) {
        try {
            dao.add(collectionName, document);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(getDesc(FAILURE_INSIDE_ERROR));
            e.printStackTrace();
            return FAILURE_INSIDE_ERROR;
        }
    }


    private <T> int batchSaveAction(List<SaveAndUpdateBO> list) {
        String collectionName = list.get(0).getCollectionName();
        List<Document> documents = new ArrayList<>();
        List<Serializable> ids = new ArrayList<>();
        for (SaveAndUpdateBO saveAndUpdateBO : list) {
            ids.add(saveAndUpdateBO.getId());
            documents.add(saveAndUpdateBO.getDocument());
        }
        try {
            dao.batchAdd(collectionName, documents);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(getDesc(FAILURE_INSIDE_ERROR));
            e.printStackTrace();
            return FAILURE_INSIDE_ERROR;
        }
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
        try {
            dao.update(collectionName, filter, update);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(getDesc(FAILURE_INSIDE_ERROR));
            e.printStackTrace();
            return FAILURE_INSIDE_ERROR;
        }
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
        try {
            dao.replace(collectionName, filter, document);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }


}
