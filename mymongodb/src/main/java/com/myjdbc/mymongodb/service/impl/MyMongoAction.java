package com.myjdbc.mymongodb.service.impl;

import com.myjdbc.core.service.ActionRetrieve;
import com.myjdbc.core.service.ActionSaveAndUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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


    public MyMongoAction(MongoTemplate mongoTemplate) {
        actionRetrieve = new MyMongoRetrieveImpl(mongoTemplate);
        actionSaveAndUpdate = new MyMongoSaveAndUpdateImpl(mongoTemplate, actionRetrieve);
    }


}
