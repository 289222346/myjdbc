package com.myjdbc.mymongodb.pool;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.myjdbc.mymongodb.dao.MongoDAO;
import com.myjdbc.mymongodb.util.MongoUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Mongo连接池
 * 连接池仅仅针在mymongodb下，不同Service实现，都要分别创建一个MongoClient的问题
 *
 * @author 陈文
 */
public class MongoPool {

    private static Map<String, MongoClient> mongoDAOMap = new LinkedHashMap<>();

    /**
     * @param url          数据库连接地址(IP:端口)
     * @param username     用户名
     * @param password     密码
     * @param databaseName 数据库名
     * @param publicKey    公钥
     * @Author 陈文
     * @Date 2020/4/21  19:44
     */
    public static MongoDAO newInstance(String url, String username, String password, String databaseName, String publicKey) {
        //如果数据库连接地址已经连接，则取用老的，否则新建一个连接
        url = MongoUtil.getDbUri(url, username, password, publicKey);
        if (mongoDAOMap.containsKey(url)) {
            MongoDAO mongoDAO = new MongoDAO(mongoDAOMap.get(url), databaseName);
            if (mongoDAO == null) {
                mongoDAOMap.remove(url);
                //如果Map中取出的连接已经失效，则重新生成
                return newInstance(url, username, password, databaseName, publicKey);
            }
            return mongoDAO;
        } else {
            MongoClientURI connectionString = new MongoClientURI(url);
            MongoClient mongoClient = new MongoClient(connectionString);
            mongoDAOMap.put(url, mongoClient);
            return new MongoDAO(mongoClient, databaseName);
        }
    }

}
