package com.myjdbc.web.core.automation.util;

import com.myjdbc.web.api.controller.PublicInfoController;
import com.myjdbc.web.core.automation.model.ScriptTemplateModel;
import com.myjdbc.web.core.user.controller.UserController;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成控制层JS
 *
 * @author 陈文
 */
public class JavaScriptGenerate {

    private static final String CONTROLLER_TEMPLATE = "ControllerTemplate.template";

    @Test
    public void t() {
        Class cls = PublicInfoController.class;

        String json = generateScriptStr(cls);
        System.out.println(json);
    }


    public static String generateScriptStr(Class cls) {
        String path = cls.getClassLoader().getResource(CONTROLLER_TEMPLATE).getPath();
        File file = new File(path);
        //获取模板
        List<StringBuffer> list = readTemplateFileContent(file);
        List<StringBuffer> javaStr = javaToStr(cls);
        //生成模板对象
        ScriptTemplateModel model = new ScriptTemplateModel(list, cls, javaStr);

        //返回JS字符串
        return model.getJavaScriptStr();
    }

    /**
     * java文件转String
     *
     * @param cls 文件类型
     * @return 文件内容字符串
     */
    private static List<StringBuffer> javaToStr(Class cls) {
        StringBuffer path = new StringBuffer(System.getProperty("user.dir"));
        path.append("\\src\\main\\java\\");
        path.append(cls.getPackage().getName().replace(".", "\\"));
        path.append("\\").append(cls.getSimpleName()).append(".java");
        File file = new File(path.toString());
        List<StringBuffer> fileValue = readJavaFileContent(file);
        return fileValue;
    }

    private static List<StringBuffer> readJavaFileContent(File file) {
        List<StringBuffer> fileValue = readFileContent(file);
        for (int i = 0; i < fileValue.size(); i++) {
            StringBuffer buffer = fileValue.get(i);
            //移除包声明
            if (buffer.indexOf("package") == 0) {
                fileValue.remove(i--);
            }

            //移除导包
            if (buffer.indexOf("import") == 0) {
                fileValue.remove(i--);
            }
        }
        return fileValue;
    }

    /**
     * 获取模板文件内容
     */
    private static List<StringBuffer> readTemplateFileContent(File file) {
        List<StringBuffer> fileValue = readFileContent(file);
        List<StringBuffer> newFileValue = new ArrayList<>();
        boolean blockComment = false;
        for (int i = 0; i < fileValue.size(); i++) {
            StringBuffer tempBuff = fileValue.get(i);

            //识别 /** xx */ 块注释，结束
            if (blockComment) {
                if (tempBuff.indexOf("*/") == 0) {
                    blockComment = false;
                }
                continue;
            }
            if (tempBuff.length() > 2) {
                //识别 /** xx */ 块注释，开始
                if (tempBuff.indexOf("/*") == 0) {
                    blockComment = true;
                    continue;
                }

                //识别 双斜杠// 行注释
                if (tempBuff.indexOf("//") == 0) {
                    continue;
                }
            }
            newFileValue.add(tempBuff);
        }
        return newFileValue;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/6  15:41
     * @Description 将文件流转换成字符串
     */
    private static List<StringBuffer> readFileContent(File file) {
        BufferedReader reader = null;
        List<StringBuffer> sbf = new ArrayList<>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                StringBuffer tempBuff = new StringBuffer(tempStr.trim());
                //空行跳过
                if (tempBuff.length() == 0) {
                    continue;
                }
                sbf.add(tempBuff);
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
}
