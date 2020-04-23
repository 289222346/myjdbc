package com.myjdbc.mymongodb.service.impl;

import com.myjdbc.core.service.ActionRetrieve;
import com.myjdbc.core.service.ActionSaveAndUpdate;
import com.myjdbc.mymongodb.dao.MongoDAO;
import org.springframework.data.mongodb.core.MongoTemplate;

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


    protected MyMongoAction(MongoTemplate mongoTemplate, String ip, int port, String databaseName) {
        //暂时写死，等到需要时在做成配置文件
        MongoDAO dao = new MongoDAO(ip, port, databaseName);
        actionRetrieve = new MyMongoRetrieveImpl(mongoTemplate, dao);
        actionSaveAndUpdate = new MyMongoSaveAndUpdateImpl(mongoTemplate, actionRetrieve);
    }


}
