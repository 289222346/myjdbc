package com.myjdbc.web.core.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.myjdbc.core.util.SecretUtil;
import com.myjdbc.core.util.StringUtil;
import com.myjdbc.mymongodb.service.SystemMongoService;
import com.myjdbc.web.core.common.controller.BaseController;
import com.myjdbc.web.core.user.model.BaseUserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制台-用户模块
 *
 * @author 陈文
 * @date 2020/05/27  22:38
 */
@CrossOrigin(origins={"http://127.0.0.1:8020","http://192.168.43.208:8020"})
@RestController()
@RequestMapping(value = "/console/user")
public class UserController extends BaseController {

    @Autowired
    private SystemMongoService systemMongoService;

    /**
     * 新增用户
     *
     * @param baseUser 用户实体
     */
    @RequestMapping("/add")
    public JSONObject add(BaseUserPO baseUser) {
        //前置校验
        JSONObject jsonObject = addPreCheck(baseUser);
        if (jsonObject != null) {
            return jsonObject;
        }
        int code = systemMongoService.save(baseUser);
        return createResultInfo(code);
    }

    /**
     * 前置校验(添加用户)
     *
     * @param baseUser 用户实体
     */
    private JSONObject addPreCheck(BaseUserPO baseUser) {
        String userName = baseUser.getUserName();
        if (StringUtil.isEmpty(userName)) {
            return createError("用户名不能为空！");
        }

        long userCount = systemMongoService.findCount(BaseUserPO.class, "userName", userName);
        if (userCount != 0) {
            return createError("用户已存在！");
        }

        String password = baseUser.getUserPassword();
        if (StringUtil.isEmpty(password)) {
            return createError("密码不能为空！");
        }
        //对密码加密
        String newPassword = SecretUtil.handPassword(password, baseUser.getUserName());
        baseUser.setUserPassword(newPassword);
        return null;
    }

}