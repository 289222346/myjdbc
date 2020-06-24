package com.myjdbc.web.core.automation.util;

import com.myjdbc.core.util.FileUtil;
import com.myjdbc.web.api.controller.PublicInfoController;
import com.myjdbc.web.api.controller.WebController;
import com.myjdbc.web.core.automation.model.ScriptTemplateModel;
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
        Class cls = WebController.class;
        String s = System.getProperty("user.dir");
        String a = "\\src\\main\\java\\";
        String b = cls.getPackage().getName().replace(".", "\\");
        String d = "\\" + cls.getSimpleName() + ".java";
        String path = s + a + b + d;
        StringBuffer buffer = FileUtil.getFileValue(path);
        System.out.println(buffer);
    }


    public static String generateScriptStr(Class cls) {
        String path = cls.getClassLoader().getResource(CONTROLLER_TEMPLATE).getPath();
        File file = new File(path);
        //获取模板
        List<StringBuffer> list = readFileContent(file);
        //生成模板对象
        ScriptTemplateModel model = new ScriptTemplateModel(list, cls);

        //返回JS字符串
        return model.getJavaScriptStr();
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
            boolean blockComment = false;
            while ((tempStr = reader.readLine()) != null) {
                StringBuffer tempBuff = new StringBuffer(tempStr.trim());

                //空行跳过
                if (tempBuff.length() == 0) {
                    continue;
                }

                //识别 /** xx */ 块注释，结束
                if (blockComment) {
                    if ("*/".equals(tempBuff.substring(tempBuff.length() - 2))) {
                        blockComment = false;
                    }
                    continue;
                }
                if (tempBuff.length() > 2) {
                    //识别 /** xx */ 块注释，开始
                    if ("/*".equals(tempBuff.substring(0, 2))) {
                        blockComment = true;
                        continue;
                    }

                    //识别 双斜杠// 行注释
                    if ("//".equals(tempBuff.substring(0, 2))) {
                        continue;
                    }
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
