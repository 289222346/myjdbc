package com.myjdbc.mymongodb.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
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
        return find(null, cls);
    }

    /**
     * @param query 查询过滤器
     * @param cls   指定实体类型
     * @param <T>   指定目标接收实体
     * @return 指定的实体集合
     * @Author 陈文
     * @Date 2020/4/21  19:22
     */
    public <T> List<T> find(BasicDBObject query, Class<T> cls) {
        String collectionName = MongoUtil.getModelName(cls);
        //获取mongo集合
        MongoCollection collection = mongoDatabase.getCollection(collectionName);
        //查询迭代器
        FindIterable findIterable = collection.find(query);
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

}
