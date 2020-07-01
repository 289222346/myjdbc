package com.myjdbc.web.core.automation.model;

import com.myjdbc.api.util.AnnotationUtil;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.web.core.automation.constants.TemplateConstant;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 脚本模板对象
 *
 * @author 陈文
 */
public class ScriptTemplateModel {

    private static final String[] SPLIT_LINE = new String[]{"{", ";", "}"};

    public ScriptTemplateModel(List<StringBuffer> list, Class cls, List<StringBuffer> javaStr) {
        for (StringBuffer temple : list) {
            this.setTemplate(temple);
        }
        this.javaStr = javaStr;
        this.set(cls);
    }

    /**
     * java原始代码
     */
    private List<StringBuffer> javaStr;

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
        if (b.indexOf(header) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 写入JS脚本(字符串)
     *
     * @param buffer 脚本模板
     * @param names  变量名(模板代码)
     * @param values 变量值
     */
    private void setJavaScriptStr(StringBuffer buffer, String[] names, String[] values) {
        int index = 0;
        StringBuffer tempBuff = new StringBuffer();
        for (int i = 0; i < names.length; i++) {
            //模板加入注释
            tempBuff.append(getAnnotate(names[i], values[i]));
            int indexPath0 = buffer.indexOf(names[i]);
            int indexPath1 = indexPath0 + names[i].length();
            tempBuff.append(buffer.substring(index, indexPath0));
            tempBuff.append(values[i]);
            if (i == names.length - 1) {
                tempBuff.append(buffer.substring(indexPath1));
            }
            index += indexPath1;
        }

        //换行符号
        int spaceUnit = 4;
        StringBuffer spaceTotal = new StringBuffer();
        for (String s : SPLIT_LINE) {
            int splitLine = 0;
            while ((splitLine = tempBuff.indexOf(s, splitLine + 1)) != -1) {
                if (s.equals("{")) {
                    for (int i = 0; i < spaceUnit; i++) {
                        spaceTotal.append(" ");
                    }
                } else if (s.equals("}")) {
                    for (int i = 0; i < spaceUnit; i++) {
                        spaceTotal.deleteCharAt(0);
                    }
                }

                tempBuff.insert(splitLine + 1, "\r\n");
            }
        }

        javaScriptStr.append(tempBuff);
    }

    /**
     * 注入控制层
     *
     * @param cls 控制层类
     */
    private void set(Class cls) {
        RequestMapping requestMapping = AnnotationUtil.findAnnotaion(cls, RequestMapping.class);
        if (requestMapping == null) {
            return;
        }
        String name = requestMapping.name();
        if (StringUtil.isEmpty(name)) {
            name = cls.getSimpleName();
        }
        //映射类
        List<String[]> list = mappingResolution(name, requestMapping);
        this.setConstValue(list);

        //获取方法的映射
        List<String[]> methodJs = new ArrayList<>();
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            String[] strings = this.set(method);
            if (strings != null) {
                methodJs.add(strings);
            }
        }
        //注入方法
        for (String[] methodJ : methodJs) {
            this.setMethodValue(methodJ[0], methodJ[1]);
        }

        //注入最终调用方法
        this.setMethodValue(list.get(0)[0]);
    }

    /**
     * 注入方法
     *
     * @param method 控制层方法
     */
    private String[] set(Method method) {
        RequestMapping requestMapping = AnnotationUtil.findAnnotaion(method, RequestMapping.class);
        if (requestMapping == null) {
            return null;
        }
        String name = requestMapping.name();
        if (StringUtil.isEmpty(name)) {
            name = method.getName();
        }

        //映射注入JS
        List<String[]> list = mappingResolution(name, requestMapping);
        this.setConstValue(list);

        return new String[]{name, list.get(0)[0]};
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
            String[] strings = new String[]{name, "'" + modulePath + "'"};
            list.add(strings);
            modelIndex++;
        }
        return list;
    }

    /**
     * 返回JS脚本
     *
     * @return JavaScript字符串
     */
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
     * 添加方法脚本
     */
    private void setMethodValue(String name, String path) {
        setJavaScriptStr(this.methodTemp,
                new String[]{TemplateConstant.METHOD_NAME, TemplateConstant.METHOD_PATH},
                new String[]{name, path});
    }

    /**
     * 添加方法脚本(最终请求方法)
     */
    private void setMethodValue(String moudlePath) {
        setJavaScriptStr(this.requestMethodTemp,
                new String[]{TemplateConstant.MOUDLE_PATH},
                new String[]{moudlePath});
    }

    /**
     * 添加常量脚本
     *
     * @param constName  常量名称
     * @param constValue 常量值
     */
    public void setConstValue(String constName, String constValue) {
        setJavaScriptStr(this.constTemp,
                new String[]{TemplateConstant.CONSTANT_NAME, TemplateConstant.CONSTANT_VALUE},
                new String[]{constName, constValue});
    }

    private StringBuffer getAnnotate(String methodNameKey, String methodName) {
        StringBuffer stringBuffer = new StringBuffer();
        if (methodNameKey.equals(TemplateConstant.METHOD_NAME)) {
            for (int i1 = 0; i1 < javaStr.size(); i1++) {
                StringBuffer jstr = javaStr.get(i1);
                if (jstr.indexOf(methodName + "(") != -1 && jstr.indexOf("public") == 0) {
                    int i2 = 1;
                    int annotationStart = 0;
                    int annotationEnd = 0;
                    while (i1 > 0) {
                        StringBuffer jstr2 = javaStr.get(i1 - i2++);
                        //跳过注解
                        if (jstr2.indexOf("@") == 0) {
                            continue;
                        }

                        if (jstr2.indexOf("*/") != -1) {
                            annotationEnd = i1 - i2 + 1;
                        }

                        if (jstr2.indexOf("/*") != -1) {
                            annotationStart = i1 - i2 + 1;
                            for (int x = annotationStart; x <= annotationEnd; x++) {
                                stringBuffer.append(javaStr.get(x)).append("\r\n");
                            }
                            break;
                        }

                        //运行到此，如果还未找到注释，则认为没有注释
                        if (annotationEnd == 0) {
                            break;
                        }

                    }
                }
            }
        }
        return stringBuffer;
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
