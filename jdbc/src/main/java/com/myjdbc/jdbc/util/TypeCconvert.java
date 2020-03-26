package com.myjdbc.jdbc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;


/**
 * @Author 陈文
 * @Date 2019/12/6  12:03
 * @Description 类型转换公共方法
 */
public class TypeCconvert {

    /**
     * @Author 陈文
     * @Date 2019/12/6  12:03
     * @Description oracle.sql.Clob类型转换成String类型
     */
    public static String ClobToString(Clob clob) {
        String reString = "";
        try {
            Reader is = null;// 得到流
            is = clob.getCharacterStream();
            BufferedReader br = new BufferedReader(is);
            String s = br.readLine();
            StringBuffer sb = new StringBuffer();
            while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
                sb.append(s);
                s = br.readLine();
            }
            reString = sb.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reString;
    }
}
