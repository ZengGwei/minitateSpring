package com.zgw.imitate.spring.demo.action;

import com.zgw.imitate.spring.demo.service.IUserLoginService;
import com.zgw.imitate.spring.framework.annotation.ZAutowried;
import com.zgw.imitate.spring.framework.annotation.ZController;
import com.zgw.imitate.spring.framework.annotation.ZRequestMapping;
import com.zgw.imitate.spring.framework.annotation.ZRequestParam;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/20
 */
@ZController
public class UserLoginController {

    @ZAutowried
    private IUserLoginService userLoginService;

    @ZRequestMapping("login")
    public String login(@ZRequestParam("name") String name, @ZRequestParam("password") String password){
        return userLoginService.login(name,password);
    }


}
