package com.myjdbc.mymongodb.service.impl;

import com.myjdbc.mymongodb.service.SystemMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * MongoService单独使用的情况下，使用此类
 * 此类不支持继承
 * 若需要继承使用MongoService,请继承{@link MongodbServiceImpl}
 */
@Service("systemMongoService")
public final class SystemMongoServiceImpl extends MongodbServiceImpl implements SystemMongoService {

    @Autowired
    public SystemMongoServiceImpl(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    public SystemMongoServiceImpl(String ip, int port, String databaseName) {
        super(ip, port, databaseName);
    }

}
