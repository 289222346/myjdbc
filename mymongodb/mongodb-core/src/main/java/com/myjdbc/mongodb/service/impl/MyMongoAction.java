package com.myjdbc.mongodb.service.impl;

import com.myjdbc.core.config.ConfigUtil;
import com.myjdbc.core.config.properties.DbConfig;
import com.myjdbc.core.service.ActionRetrieve;
import com.myjdbc.core.service.ActionSaveAndUpdate;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.mongodb.dao.MongoDAO;
import com.myjdbc.mongodb.pool.MongoPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.concurrent.TimeUnit;

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

    @Autowired
    protected MyMongoAction(MongoTemplate mongoTemplate) {
        DbConfig dbConfig = ConfigUtil.getDbConfig();
        //如果获取不到数据库配置，进入多线程，等待
        if (dbConfig == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取数据库配置时，如果获取不到，需要空转等待
                    while (true) {
                        DbConfig dbConfig = ConfigUtil.getDbConfig();
                        if (dbConfig != null) {
                            //初始化
                            initMongoAction(mongoTemplate, dbConfig);
                            break;
                        }
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    protected MyMongoAction(MongoTemplate mongoTemplate, DbConfig dbConfig) {
        initMongoAction(mongoTemplate, dbConfig);
    }

    protected MyMongoAction(MongoTemplate mongoTemplate, String url, String databaseName) {
        initMongoAction(mongoTemplate, url, null, null, databaseName, null);
    }

    protected MyMongoAction(MongoTemplate mongoTemplate, String url, String username, String password, String databaseName) {
        initMongoAction(mongoTemplate, url, username, password, databaseName, null);
    }

    /**
     * 初始化MongoDB
     */
    private void initMongoAction(MongoTemplate mongoTemplate, String url, String username, String password, String databaseName, String publicKey) {
        MongoDAO dao = MongoPool.newInstance(url, username, password, databaseName, publicKey);
        actionRetrieve = new MyMongoRetrieveImpl(mongoTemplate, dao);
        actionSaveAndUpdate = new MyMongoSaveAndUpdateImpl(mongoTemplate, actionRetrieve);
    }

    /**
     * 初始化MongoDB
     */
    private void initMongoAction(MongoTemplate mongoTemplate, DbConfig dbConfig) {
        String url = dbConfig.getUrl();
        String username = dbConfig.getUsername();
        String password = dbConfig.getPassword();
        String databaseName = dbConfig.getDatabaseName();
        String publicKey = dbConfig.getPublicKey();
        if (StringUtil.isEmpty(url)) {
            throw new NullPointerException("MongoDB数据库连接故障：找不到url参数");
        }
        if (StringUtil.isEmpty(databaseName)) {
            throw new NullPointerException("MongoDB数据库连接故障：找不到databaseName参数");
        }
        initMongoAction(mongoTemplate, url, username, password, databaseName, publicKey);
    }


}
