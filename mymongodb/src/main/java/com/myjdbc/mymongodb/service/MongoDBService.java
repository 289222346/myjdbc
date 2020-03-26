package com.myjdbc.mymongodb.service;

import org.bson.Document;
import org.springframework.stereotype.Service;

@Service
public interface MongoDBService {

    void save(String collectionName, Document document);

}
