package com.myjdbc.web.core.automation.model;

import com.myjdbc.core.util.StringUtil;
import com.myjdbc.web.core.automation.constants.TemplateConstant;

import java.util.HashSet;
import java.util.Set;

/**
 * 脚本模板对象
 *
 * @author 陈文
 */
public class ScriptTemplateModel {

    private final Set<String> codePool;

    public ScriptTemplateModel() {
        codePool = new HashSet<>();
        codePool.add(TemplateConstant.MODULE_CODE);
        codePool.add(TemplateConstant.METHOD_CODE);
    }

    /**
     * 模块地址-模板
     */
    private String modulePath;

    /**
     * 方法地址-模板
     */
    private String methodPath;


    public String getModulePath() {
        return modulePath;
    }

    public String getMethodPath() {
        return methodPath;
    }

    public void setModulePath(String moduleName, String modulePath) {
        if (StringUtil.isNotEmpty(this.modulePath)) {
            this.modulePath = this.modulePath.replace(TemplateConstant.MODULE_PATH, moduleName);
            this.modulePath = this.modulePath.replace(TemplateConstant.MODULE_PATH_VALUE, "'" + modulePath + "'");
        }
    }

    public void setMethodPath(String methodName,String methodPath) {
        if (StringUtil.isNotEmpty(this.methodPath)) {
            this.methodPath = this.methodPath.replace(TemplateConstant.METHOD_PATH, methodName);
            this.methodPath = this.methodPath.replace(TemplateConstant.METHOD_PATH_VALUE, "'" + methodPath + "'");
        }
    }

    public void setValue(StringBuffer stringBuffer) {
        String code = stringBuffer.substring(0, 4);
        for (String s : codePool) {
            if (s.equals(code)) {
                stringBuffer.delete(0, 5);
                switch (code) {
                    case TemplateConstant.MODULE_CODE:
                        this.modulePath = stringBuffer.toString();
                        break;
                    case TemplateConstant.METHOD_CODE:
                        this.methodPath = stringBuffer.toString();
                        break;
                }
                codePool.remove(s);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "ScriptTemplateModel{" +
                ", modulePath='" + modulePath + '\'' +
                ", methodPath='" + methodPath + '\'' +
                '}';
    }
}
