package com.myjdbc.mymongodb.test;

import com.myjdbc.core.service.BaseService;
import com.myjdbc.mymongodb.dao.MongoDAO;
import com.myjdbc.mymongodb.service.impl.MongodbServiceImpl;

import java.util.List;

public class Test {
    public static String ip = "localhost";
    public static int port = 27017;
    public static String databaseName = "imapi";


    public static BaseService getBaseService() {
        BaseService baseService = new MongodbServiceImpl(ip, port, databaseName);
        return baseService;
    }


}
