package com.myjdbc.mymongodb.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.result.DeleteResult;
import com.myjdbc.core.constants.OrderType;
import com.myjdbc.core.model.OrderBo;
import com.myjdbc.core.model.Pag;
import com.myjdbc.core.util.ListUtil;
import com.myjdbc.core.util.ModelUtil;
import com.myjdbc.mymongodb.util.MongoUtil;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 * mongoDB直接操作层
 *
 * @Author 陈文
 * @Date 2020/4/21  19:42
 */
public class MongoDAO {

    private MongoDatabase mongoDatabase;

    /**
     * @param mongoClient  Mongo连接
     * @param databaseName 数据库名
     * @Author 陈文
     * @Date 2020/4/21  19:44
     */
    public MongoDAO(MongoClient mongoClient, String databaseName) {
        this.mongoDatabase = mongoClient.getDatabase(databaseName);
    }

    public DeleteResult delete(Class cls, BasicDBObject filter) throws Exception {
        MongoCollection collection = getMongoCollection(cls);
        DeleteResult deleteResult = collection.deleteOne(filter);
        return deleteResult;
    }

//    public DeleteResult batchDelete(Class cls, BasicDBObject filter) throws Exception {
//        MongoCollection collection = getMongoCollection(cls);
//        collection.remo
//        return deleteResult;
//    }

    public void add(String collectionName, Document document) throws Exception {
        MongoCollection collection = getMongoCollection(collectionName);
        collection.insertOne(document);
    }

    public void batchAdd(String collectionName, List<Document> document) throws Exception {
        MongoCollection collection = getMongoCollection(collectionName);
        collection.insertMany(document);
    }

    public void update(String collectionName, Bson filter, Bson update) throws Exception {
        MongoCollection collection = getMongoCollection(collectionName);
        collection.updateOne(filter, update);
    }

    public void replace(String collectionName, Bson filter, Bson update) throws Exception {
        MongoCollection collection = getMongoCollection(collectionName);
        collection.replaceOne(filter, update);
    }

    public <T> List<T> find(Class<T> cls) {
        return find(new BasicDBObject(), cls);
    }

    public <T> List<T> find(BasicDBObject query, Class<T> cls) {
        return find(query, cls, null);
    }

    public <T> List<T> find(BasicDBObject query, Class<T> cls, Pag pag) {
        return find(query, cls, pag, null);
    }

    public <T> List<T> find(BasicDBObject query, Class<T> cls, Pag pag, OrderBo order) {
        return find(query, cls, pag, order, null);
    }


    public long findCount(BasicDBObject query, String collectionName) {
        return findCount(query, getMongoCollection(collectionName));
    }

    public long findCount(BasicDBObject query, Class cls) {
        return findCount(query, getMongoCollection(cls));
    }

    public long findCount(BasicDBObject query, MongoCollection collection) {
        return collection.countDocuments(query);
    }

    /**
     * @param query    查询过滤器
     * @param cls      指定实体类型
     * @param joinList 关联查询
     * @param <T>      指定目标接收实体
     * @return 指定的实体集合
     * @Author 陈文
     * @Date 2020/4/21  19:22
     */
    public <T> List<T> find(BasicDBObject query, Class<T> cls, Pag pag, OrderBo order, List<Bson> joinList) {
        MongoCollection collection = getMongoCollection(cls);
        List<Bson> aggregateList = new ArrayList<>(1);
        //匹配查询条件
        aggregateList.add(Aggregates.match(query));
        //增加表关联查询
        if (ListUtil.isNotEmpty(joinList)) {
            for (Bson join : joinList) {
                aggregateList.add(join);
            }
        }
        //添加排序
        if (order != null) {
            //默认DESC
            int orderType = -1;
            if (order.getOrderType().equals(OrderType.ASC)) {
                orderType = 1;
            }
            for (String fieldName : order.getFieldNames()) {
                BasicDBObject sort = new BasicDBObject(MongoUtil.getPropertyName(cls, fieldName), orderType);
                aggregateList.add(Aggregates.sort(sort));
            }
        }

        //添加分页
        if (pag != null) {
            long total = findCount(query, cls);
            pag.setTotal(total);
            int page = (pag.getPage() * pag.getRows());
            int rows = pag.getRows();
            aggregateList.add(Aggregates.skip(page));
            aggregateList.add(Aggregates.limit(rows));
        }

        AggregateIterable<Document> aggregateIterable = collection.aggregate(aggregateList);
        List<T> list = MongoUtil.documentIterableToPOJOList(aggregateIterable, cls);
        return list;
    }

    /**
     * 获取mongo集合
     *
     * @param collectionName 数据集合名称（数据库表名）
     * @return
     */
    private MongoCollection getMongoCollection(String collectionName) {
        MongoCollection collection = mongoDatabase.getCollection(collectionName);
        return collection;
    }

    /**
     * 获取mongo集合
     *
     * @param cls 实体类
     * @return
     */
    private MongoCollection getMongoCollection(Class cls) {
        String collectionName = ModelUtil.getModelName(cls);
        //获取mongo集合
        MongoCollection collection = mongoDatabase.getCollection(collectionName);
        return collection;
    }


}
