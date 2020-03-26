package com.myjdbc.mymongodb.service.impl;

import com.myjdbc.mymongodb.service.MongoDBService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

public class MongoDBServiceImpl implements MongoDBService {



    @Override
    public void save(String collectionName, Document document) {
//        mongoTemplate.getCollection(collectionName).insertOne(document);
    }
}
