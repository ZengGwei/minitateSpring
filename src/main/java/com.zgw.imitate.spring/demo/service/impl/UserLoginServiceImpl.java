package com.zgw.imitate.spring.demo.service.impl;

import com.zgw.imitate.spring.demo.service.IUserLoginService;
import com.zgw.imitate.spring.framework.annotation.ZService;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/20
 */
@ZService
public class UserLoginServiceImpl implements IUserLoginService {
    @Override
    public String login(String name, String password) {
        return name+"在 "+System.currentTimeMillis()+"时刻 登陆。";
    }
}
