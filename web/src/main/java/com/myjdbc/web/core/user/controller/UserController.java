package com.myjdbc.web.core.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.myjdbc.api.annotations.IDAutoGenerator;
import com.myjdbc.api.annotations.MyApiModelProperty;
import com.myjdbc.api.annotations.MyID;
import com.myjdbc.api.model.AnnotationObject;
import com.myjdbc.core.util.AnnotationUtil;
import com.myjdbc.core.util.SecretUtil;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.mymongodb.service.SystemMongoService;
import com.myjdbc.web.core.common.controller.BaseController;
import com.myjdbc.web.core.common.util.Md5Util;
import com.myjdbc.web.core.user.model.BaseUserPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;

/**
 * 公共信息开放接口
 *
 * @author 陈文
 * @date 2020/05/27  22:38
 */
@RestController()
@RequestMapping(value = "/console/user")
public class UserController extends BaseController {

    @Test
    public void test() throws NoSuchFieldException {
        Field field = BaseUserPO.class.getDeclaredField("id");
        MyID myID = field.getAnnotation(MyID.class);
        System.out.println(myID);
        AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(MyApiModelProperty.class, myID);
        MyApiModelProperty myApiModelProperty = AnnotationUtils.synthesizeAnnotation(annotationAttributes, MyApiModelProperty.class, myID.annotationType());
        System.out.println(myApiModelProperty);
    }

    public UserController() {
        System.out.println("启动控制层");
    }

    @Autowired
    private SystemMongoService systemMongoService;

    @RequestMapping("/add")
    public JSONObject add(BaseUserPO baseUserPO) throws NoSuchFieldException {
        String userName = baseUserPO.getUserName();
        if (StringUtil.isEmpty(userName)) {
            return createError("用户名不能为空！");
        }

        long userCount = systemMongoService.findCount(BaseUserPO.class, "userName", userName);
        if (userCount != 0) {
            return createError("用户已存在！");
        }
        //密码加密
        String password = handPassword(baseUserPO.getUserPassword(), userName);
        baseUserPO.setUserPassword(password);
        System.out.println("密码已加密");
        int code = systemMongoService.save(baseUserPO);
        System.out.println("用户已保存");
        return createResultInfo(code);
    }


    private String handPassword(String password, String publicKey) {
        password = SecretUtil.encryption(password, publicKey);
        password = Md5Util.md5Hex(password);
        return password;
    }

}