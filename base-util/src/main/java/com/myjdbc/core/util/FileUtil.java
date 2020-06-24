package com.myjdbc.core.util;

import java.io.*;

public class FileUtil {

    /**
     * 如果文件夹不存在，则创建
     *
     * @param file
     */
    public static void checkDirExists(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * @Author 陈文
     * @Date 2019/12/18  1:02
     * @Description 判断文件夹存不存在
     */
    public static boolean isExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @Author 陈文
     * @Date 2019/12/18  1:01
     * @Description 获取文件夹内所有文件
     */
    public static File[] getFiles(File file) {
        return file.listFiles();
    }

    /**
     * @Author 陈文
     * @Date 2019/12/18  11:12
     * @Description 获取文件名（不含后缀）
     */
    public static String getFileName(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index != -1) {
            fileName = StringUtil.leftSubstring(fileName, index);
        }
        return fileName;
    }

    public static void main(String[] args) {
        String type = getFileType("abcdefgh.txt");
        System.out.println(type);
    }

    /**
     * @Author 陈文
     * @Date 2020/05/05  11:14
     * @Description 获取文件后缀名
     */
    public static String getFileType(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index != -1) {
            fileName = StringUtil.rightSubstring(fileName, index + 1);
        }
        return fileName;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/6  15:41
     * @Description 将文件流转换成字符串
     */
    public static StringBuffer readFileContent(File file) {

        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
//            reader = new BufferedReader(new FileReader(file));
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr.trim());
            }
            reader.close();
            return sbf;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf;
    }

    public static StringBuffer getFileValue(String path) {
        File file = new File(path);
        if (file.isFile()) {
            StringBuffer buffer = readFileContent(file);
            return buffer;
        }
        return null;
    }

    @Deprecated
    public static StringBuffer getSql(String path) {
        File file = new File(path);
        if (file.isFile()) {
            StringBuffer sql = readFileContent(file);
            return sql;
        }
        return null;
    }

}
