package com.myjdbc.mymongodb.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.myjdbc.core.model.OrderBo;
import com.myjdbc.core.model.Pag;
import com.myjdbc.core.util.ModelUtil;
import com.myjdbc.mymongodb.util.MongoUtil;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * mongoDB直接操作层
 *
 * @Author 陈文
 * @Date 2020/4/21  19:42
 */
public class MongoDAO {

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    /**
     * @param ip           数据库IP地址
     * @param port         数据库端口
     * @param databaseName 数据库名
     * @Author 陈文
     * @Date 2020/4/21  19:44
     */
    public MongoDAO(String ip, int port, String databaseName) {
        //建立数据库连接
        this.mongoClient = new MongoClient(ip, port);
        //获取指定数据库
        this.mongoDatabase = mongoClient.getDatabase(databaseName);
    }

    public <T> List<T> find(Class<T> cls) {
        return find(null, cls, null, null);
    }

    public <T> List<T> find(BasicDBObject query, Class<T> cls) {
        return find(query, cls, null, null);
    }

    public <T> List<T> find(BasicDBObject query, Class<T> cls, Pag pag) {
        return find(query, cls, pag, null);
    }

    public long findCount(BasicDBObject query, String collectionName) {
        return findCount(query, getMongoCollection(collectionName));
    }

    public long findCount(MongoCollection collection) {
        return collection.countDocuments();
    }

    public long findCount(BasicDBObject query, MongoCollection collection) {
        return collection.countDocuments(query);
    }

    /**
     * @param query 查询过滤器
     * @param cls   指定实体类型
     * @param <T>   指定目标接收实体
     * @return 指定的实体集合
     * @Author 陈文
     * @Date 2020/4/21  19:22
     */
    public <T> List<T> find(BasicDBObject query, Class<T> cls, Pag pag, OrderBo order) {
        MongoCollection collection = getMongoCollection(cls);
        //查询迭代器
        FindIterable findIterable = collection.find(query);
        //添加分页
        if (pag != null) {
            long total = collection.countDocuments();
            pag.setTotal(total);
            int page = (pag.getPage() * pag.getRows());
            int rows = pag.getRows();
            findIterable.skip(page).limit(rows);
        }
        //添加排序
//        if (order != null) {
//            if (order.getOrderType().equals(OrderType.ASC)) {
//                for (String string : order.getFieldNames()) {
////                    query.with(Sort.by(Sort.Order.asc(string)));
////                    findIterable.sort(query);
//                }
//            } else {
//                for (String string : order.getFieldNames()) {
////                    query.with(Sort.by(Sort.Order.desc(string)));
//                }
//            }
//        }

        MongoCursor mongoCursor = findIterable.iterator();
        List<T> list = new ArrayList<>();
        while (mongoCursor.hasNext()) {
            Object object = mongoCursor.next();
            Document document = (Document) object;
            T t = MongoUtil.documentToMongoPOJO(document, cls);
            if (t != null) {
                list.add(t);
            }
        }
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
