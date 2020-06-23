package com.myjdbc.web.core.automation.model;

import com.myjdbc.api.util.AnnotationUtil;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.web.core.automation.constants.TemplateConstant;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 脚本模板对象
 *
 * @author 陈文
 */
public class ScriptTemplateModel {

    public ScriptTemplateModel(List<StringBuffer> list, Class conCls) {
        for (StringBuffer temple : list) {
            this.setTemplate(temple);
        }
        this.set(conCls);
    }

    /**
     * 常量-模板
     */
    private StringBuffer constTemp;

    /**
     * 方法模板
     */
    private StringBuffer methodTemp;

    /**
     * 最终请求模板
     */
    private StringBuffer requestMethodTemp;

    /**
     * 生成的脚本
     */
    private StringBuffer javaScriptStr = new StringBuffer();

    /**
     * 设置模板
     *
     * @param b
     */
    private void setTemplate(StringBuffer b) {
        //记录常量模板
        if (matchHeader(b, TemplateConstant.CONSTANT)) {
            this.constTemp = b;
            return;
        }
        //记录方法模板
        if (matchHeader(b, TemplateConstant.FUNCTION)) {
            if (TemplateConstant.SERVER_REQUEST.equals(b.substring(TemplateConstant.FUNCTION.length(), b.indexOf("(")).trim())) {
                //最终请求方法模板
                this.requestMethodTemp = b;
            } else {
                //普通方法模板
                this.methodTemp = b;
            }
        }
    }

    /**
     * 校验头部信息
     *
     * @param b
     * @param header
     * @return
     */
    private boolean matchHeader(StringBuffer b, String header) {
        if (header.equals(b.substring(0, header.length()))) {
            return true;
        }
        return false;
    }


    private void setJavaScriptStr(String nameAlias, String valueAlias, StringBuffer buffer, String name, String value) {
        int indexPath0 = buffer.indexOf(nameAlias);
        int indexPath1 = indexPath0 + nameAlias.length();
        int indexValue0 = buffer.indexOf(valueAlias);
        int indexValue1 = indexValue0 + valueAlias.length();
        javaScriptStr.append(buffer.substring(0, indexPath0));
        javaScriptStr.append(name);
        javaScriptStr.append(buffer.substring(indexPath1, indexValue0));
        javaScriptStr.append(value);
        javaScriptStr.append(buffer.substring(indexValue1));
        javaScriptStr.append("\r\n");
    }


    private void set(Class conCls) {
        RequestMapping requestMapping = AnnotationUtil.findAnnotaion(conCls, RequestMapping.class);
        if (requestMapping == null) {
            return;
        }
        String name = requestMapping.name();
        if (StringUtil.isEmpty(name)) {
            name = conCls.getSimpleName();
        }
        //映射类
        List<String[]> list = mappingResolution(name, requestMapping);
        this.setConstValue(list);

        //获取方法的映射
        Method[] methods = conCls.getMethods();
        for (Method method : methods) {
            this.set(method);
        }
    }

    private void set(Method method) {
        RequestMapping requestMapping = AnnotationUtil.findAnnotaion(method, RequestMapping.class);
        if (requestMapping == null) {
            return;
        }
        String name = requestMapping.name();
        if (StringUtil.isEmpty(name)) {
            name = method.getName();
        }
        //映射注入JS
        List<String[]> list = mappingResolution(name, requestMapping);
        this.setConstValue(list);
    }

    /**
     * 映射解析
     *
     * @param name           类（方法）名
     * @param requestMapping 映射地址
     * @return
     */
    private List<String[]> mappingResolution(String name, RequestMapping requestMapping) {
        int modelIndex = 0;
        List list = new ArrayList();
        name = StringUtil.humpToUnderline(name);
        for (String modulePath : requestMapping.path()) {
            name = modelIndex > 0 ? name + modelIndex : name;
            String[] strings = new String[]{name, modulePath};
            list.add(strings);
            modelIndex++;
        }
        return list;
    }

    public String getJavaScriptStr() {
        return javaScriptStr.toString();
    }

    /**
     * 添加常量脚本
     */
    private void setConstValue(List<String[]> list) {
        for (String[] strings : list) {
            this.setConstValue(strings[0], strings[1]);
        }
    }

    /**
     * 添加常量脚本
     *
     * @param constName  常量名称
     * @param constValue 常量值
     */
    public void setConstValue(String constName, String constValue) {
        setJavaScriptStr(TemplateConstant.CONSTANT_NAME, TemplateConstant.CONSTANT_VALUE, this.constTemp, constName, constValue);
    }

    @Override
    public String toString() {
        return "ScriptTemplateModel{" +
                "constTemp=" + constTemp +
                ", methodTemp=" + methodTemp +
                ", requestMethodTemp=" + requestMethodTemp +
                '}';
    }
}
