package com.myjdbc.mymongodb.service.impl;

import com.myjdbc.core.constants.OpType;
import com.myjdbc.core.constants.OrderType;
import com.myjdbc.core.criteria.CriteriaQuery;
import com.myjdbc.core.criteria.impl.CriteriaQueryImpl;
import com.myjdbc.core.entity.Criterion;
import com.myjdbc.core.entity.OrderBo;
import com.myjdbc.core.service.ActionSaveAndUpdate;
import com.myjdbc.core.service.BaseService;
import com.myjdbc.core.util.ListUtil;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.mymongodb.util.MongodbQueryUtil;
import com.myjdbc.mymongodb.util.MongoDBUtil;
import io.swagger.annotations.ApiModel;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service("mongodbService")
public class MongodbServiceImpl implements BaseService {

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
            List<Serializable> ids = new ArrayList<>();
            for (T t : list) {
                Document document = MongoDBUtil.poToDocument(t);
                Serializable id = (Serializable) document.get("_id");
                ids.add(id);
            }
            query.addCriteria(Criteria.where("_id").in(ids.toArray()));
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
        CriteriaQuery criteriaQuery = new CriteriaQueryImpl(cls);
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
    public <T> List<T> findAll(CriteriaQuery<T> criteriaQuery) {
        //MongoDB的Query查询构造器
        Query query = new Query();
        //遍历Myjdbc查询构造器，并适配成MongoDB的Query查询构造器
        criteriaQuery.getCriteriaMap().values().forEach(criteria -> {
            //查询条件
            List<Criterion> criterions = criteria.getCriterions();
            //限定字段名
            String filedName = criteria.getFieldName();
            //获取匹配方式
            getCriteria(query, filedName, criterions);
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
        //判断是否需要排序
        if (criteriaQuery.getOrder() != null) {
            OrderBo order = criteriaQuery.getOrder();
            if (order.getOrderType().equals(OrderType.ASC)) {
                for (String string : order.getFieldNames()) {
                    query.with(Sort.by(Sort.Order.asc(string)));
                }
            } else {
                for (String string : order.getFieldNames()) {
                    query.with(Sort.by(Sort.Order.desc(string)));
                }
            }
        }
        List<T> list = mongoTemplate.find(query, criteriaQuery.getCls(), collectionName);
        return list;
    }

    /**
     * 匹配成mongoDB查询器
     *
     * @param query         mongoDB查询器
     * @param filedName     限定字段名
     * @param criterionList 限定条件集
     * @return
     * @Author 陈文
     * @Date 2020/4/13  21:19
     */
    private void getCriteria(Query query, String filedName, List<Criterion> criterionList) {
        if (query == null) {
            throw new NullPointerException("query-查询器不能为空！");
        }

        if (StringUtil.isEmpty(filedName)) {
            throw new NullPointerException("filedName-限定字段名不能为空！");
        }

        if (ListUtil.isEmpty(criterionList)) {
            throw new NullPointerException("criterionList-限定条件不能为空！");
        }
        Criteria criteria = Criteria.where(filedName);

        criterionList.forEach(criterion -> {
            //限定条件
            OpType op = criterion.getOp();
            //限定值
            List<Object> values = criterion.getFieldValue();
            if (ListUtil.isEmpty(values)) {
                throw new NullPointerException("限定值不能为空！");
            }

            //完全相等
            if (op == OpType.EQ) {
                criteria.is(values.get(0));
                return;
            }

            //模糊查询
            if (op == OpType.LIKE) {
                values.forEach(value -> {
                    //正则表达式
                    Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
                    criteria.regex(pattern);
                });
                return;
            }

            //包含相等
            if (op == OpType.IN) {
                query.addCriteria(Criteria.where(filedName).in(values));
                return;
            }

            //大于
            if (op == OpType.GT) {
                criteria.gt(values.get(0));
                return;
            }

            //小于
            if (op == OpType.LT) {
                criteria.lt(values.get(0));
                return;
            }

            //大于等于
            if (op == OpType.GE) {
                criteria.gte(values.get(0));
                return;
            }

            //小于等于
            if (op == OpType.LE) {
                criteria.lte(values.get(0));
                return;
            }
        });
        query.addCriteria(criteria);
    }

    @Override
    public int getCount(CriteriaQuery criteriaQuery) {
        return 0;
    }
}
