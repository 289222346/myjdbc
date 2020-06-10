package com.myjdbc.web.core.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.myjdbc.core.util.SecretUtil;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.mymongodb.service.SystemMongoService;
import com.myjdbc.web.core.common.controller.BaseController;
import com.myjdbc.web.core.common.util.Md5Util;
import com.myjdbc.web.core.user.model.BaseUserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;

/**
 * 公共信息开放接口
 *
 * @author 陈文
 * @date 2020/05/27  22:38
 */
@RestController()
@RequestMapping(value = "/console/user")
public class UserController extends BaseController {

    @Autowired
    private SystemMongoService systemMongoService;

    @RequestMapping("/add")
    public JSONObject add(BaseUserPO baseUserPO) {
        System.out.println("进入了add");
        String userName = baseUserPO.getUserName();
        if (StringUtil.isEmpty(userName)) {
            return createError("用户名不能为空！");
        }

        for (int i = 0; i < 1000000; i++) {
            int s = 5;
            int c = s;
            s = i;
        }

        long userCount = systemMongoService.findCount(BaseUserPO.class, "userName", userName);
        if (userCount != 0) {
            return createError("用户已存在！");
        }

        //加密密码
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