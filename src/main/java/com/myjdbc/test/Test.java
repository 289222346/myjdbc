package com.myjdbc.test;

import com.myjdbc.util.DBUtil;
import com.myjdbc.util.DBconfig;
public class Test {


    public static void main(String[] args0) {
        /* 驱动 */
        String driver = "oracle.jdbc.OracleDriver";    //驱动标识符
        /* 数据库URL */
        String url = "jdbc:oracle:thin:@192.168.100.125:1521:GS";
        /* 用户 */
        String username = "GS";
        /* 密码 */
        String password = "root";
        /* 最大连接数 */
        int maxCount = 10;
        /* 最小连接数 */
        int minCount = 1;
        /* 关闭空闲连接扫描时间（分钟） */
        int closeTime = 5;
        DBconfig dBconfig = new DBconfig(driver, url, username, password, maxCount, minCount, closeTime);
        DBUtil.setDbconfig(dBconfig);

        WExamTrainDao dao = new WExamTrainDao(WExamTrain.class, "id");
        WExamTrain w = dao.findById("4028e4e46b00f8fd016b00ff590c0002");
        System.out.println(w);
    }
}
