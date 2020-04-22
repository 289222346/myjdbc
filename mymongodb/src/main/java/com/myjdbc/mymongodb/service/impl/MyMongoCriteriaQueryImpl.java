package com.myjdbc.mymongodb.service.impl;

import ch.qos.logback.classic.layout.TTLLLayout;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.myjdbc.core.constants.OpType;
import com.myjdbc.core.constants.OrderType;
import com.myjdbc.core.criteria.CriteriaQuery;
import com.myjdbc.core.criteria.impl.CriteriaQueryImpl;
import com.myjdbc.core.entity.Criterion;
import com.myjdbc.core.entity.OrderBo;
import com.myjdbc.core.service.ActionCriteriaQuery;
import com.myjdbc.core.util.ListUtil;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.mymongodb.dao.MongoDAO;
import com.myjdbc.mymongodb.util.MongoUtil;
import org.bson.Document;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
        criteriaQuery = spliceCriteriaQuery(criteriaQuery);
        //MongoDB的Query查询构造器
        BasicDBObject query = new BasicDBObject();
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
        String collectionName = MongoUtil.getModelName(criteriaQuery.getCls());
        //判断是否需要分页
//        if (criteriaQuery.getPag() != null) {
        //总数据量
//            long total = mongoTemplate.count(query, collectionName);
//            criteriaQuery.setTotal(total);
//        }


//        List<T> list = mongoTemplate.find(query, criteriaQuery.getCls(), collectionName);
        List<T> list = dao.find(query, criteriaQuery.getCls(), criteriaQuery.getPag(), criteriaQuery.getOrder());
        return list;
    }


    @Override
    public int getCount(CriteriaQuery criteriaQuery) {
        return 0;
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
        criterionList.forEach(criterion -> {
            //限定条件
            OpType op = criterion.getOp();

            //字段值为空匹配
            if (op == OpType.IS_NULL) {
                query.append(filedName, new BasicDBObject("$exists", false));
                return;
            }

            if (op == OpType.IS_NOT_NULL) {
                query.append(filedName, new BasicDBObject("$exists", true));
                return;
            }

            //限定值
            List<Object> values = criterion.getFieldValue();
            if (ListUtil.isEmpty(values)) {
                throw new NullPointerException(op.getRemark() + "  限定条件,限定值不能为空！");
            }

            //完全相等
            if (op == OpType.EQ) {
                query.append(filedName, new BasicDBObject("$eq", values.get(0)));
                return;
            }

            //模糊查询
            if (op == OpType.LIKE) {
                values.forEach(value -> {
                    //正则表达式
                    Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
                    query.append(filedName, new BasicDBObject("$regex", pattern));
                });
                return;
            }

            //包含相等
            if (op == OpType.IN) {
                query.append(filedName, new BasicDBObject("$in", values));
                return;
            }

            //大于
            if (op == OpType.GT) {
                query.append(filedName, new BasicDBObject("$gt", values.get(0)));
                return;
            }

            //小于
            if (op == OpType.LT) {
                query.append(filedName, new BasicDBObject("$lt", values.get(0)));
                return;
            }

            //大于等于
            if (op == OpType.GE) {
                query.append(filedName, new BasicDBObject("$gte", values.get(0)));
                return;
            }

            //小于等于
            if (op == OpType.LE) {
                query.append(filedName, new BasicDBObject("$lte", values.get(0)));
                return;
            }
        });
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
