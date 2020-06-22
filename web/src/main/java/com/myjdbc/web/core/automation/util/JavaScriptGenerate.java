package com.myjdbc.web.core.automation.util;

import com.myjdbc.web.api.controller.PublicInfoController;
import com.myjdbc.web.core.automation.constants.TemplateConstant;
import com.myjdbc.web.core.automation.model.ScriptTemplateModel;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成控制层JS
 *
 * @author 陈文
 */
public class JavaScriptGenerate {

    @Test
    public void t() {
        s();
    }

    public void s() {
        String path = getClass().getClassLoader().getResource("ControllerTemplate.template").getPath();
        File file = new File(path);
        //获取模板
        ScriptTemplateModel model = new ScriptTemplateModel();
        List<StringBuffer> list = readFileContent(file);
        for (StringBuffer temple : list) {
            model.setValue(temple);
        }


        String modulePath = "/api/public";
        String methodPath = "/hello";

        model.setModulePath("API_MODEL", modulePath);
        model.setMethodPath("HELLO_HTML", methodPath);

        System.out.println(model.getModulePath());
        System.out.println(model.getMethodPath());
    }


    /**
     * @Author 陈文
     * @Date 2019/12/6  15:41
     * @Description 将文件流转换成字符串
     */
    public List<StringBuffer> readFileContent(File file) {
        BufferedReader reader = null;
        List<StringBuffer> sbf = new ArrayList<>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String tempStr;
            boolean blockComment = false;
            while ((tempStr = reader.readLine()) != null) {
                StringBuffer tempBuff = new StringBuffer(tempStr);

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

                //识别 /** xx */ 块注释，开始
                if ("/*".equals(tempBuff.substring(0, 2))) {
                    blockComment = true;
                    continue;
                }

                //识别 双斜杠// 行注释
                if ("//".equals(tempBuff.substring(0, 2))) {
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
