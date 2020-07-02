package com.myjdbc.mymongodb.service.impl;

import com.myjdbc.core.util.config.properties.DbConfig;
import com.myjdbc.core.service.ActionRetrieve;
import com.myjdbc.core.service.ActionSaveAndUpdate;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.mymongodb.dao.MongoDAO;
import com.myjdbc.mymongodb.pool.MongoPool;

/**
 * mongodb的查询构造器操作实现类
 *
 * @Author: 陈文
 * @Date: 2020/4/20 10:59
 */
public class MyMongoAction {
    /**
     * 查询操作
     *
     * @Author: 陈文
     * @Date: 2020/4/20 11:40
     */
    protected ActionRetrieve actionRetrieve;

    /**
     * 保存操作
     */
    protected ActionSaveAndUpdate actionSaveAndUpdate;

    protected MyMongoAction() {
        initMongoAction();
    }

    protected MyMongoAction(String databaseName) {
        initMongoAction(databaseName);
    }

    /**
     * 初始化MongoDB
     */
    private void initMongoAction(String databaseName) {
        String url = DbConfig.URL;
        String username = DbConfig.USERNAME;
        String password = DbConfig.PASSWORD;
        String publicKey = DbConfig.PUBLIC_KEY;
        initMongoAction(url, username, password, databaseName, publicKey);
    }

    /**
     * 初始化MongoDB
     */
    private void initMongoAction() {
        String url = DbConfig.URL;
        String username = DbConfig.USERNAME;
        String password = DbConfig.PASSWORD;
        String databaseName = DbConfig.DATABASE;
        String publicKey = DbConfig.PUBLIC_KEY;
        if (StringUtil.isEmpty(databaseName)) {
            throw new NullPointerException("MongoDB数据库连接故障：找不到databaseName参数");
        }
        initMongoAction(url, username, password, databaseName, publicKey);
    }

    /**
     * 初始化MongoDB
     */
    private void initMongoAction(String url, String username, String password, String databaseName, String publicKey) {
        MongoDAO dao = MongoPool.newInstance(url, username, password, databaseName, publicKey);
        actionRetrieve = new MyMongoRetrieveImpl(dao);
        actionSaveAndUpdate = new MyMongoSaveAndUpdateImpl(actionRetrieve, dao);
    }

}
