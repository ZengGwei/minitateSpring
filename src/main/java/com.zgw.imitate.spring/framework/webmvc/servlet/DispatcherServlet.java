package com.zgw.imitate.spring.framework.webmvc.servlet;

import com.zgw.imitate.spring.demo.action.DemoAction;
import com.zgw.imitate.spring.framework.context.ZApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Documented;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/10
 */

public class DispatcherServlet extends HttpServlet {

    private final String  LOCATION ="contextConfigLocation";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        String initParameter = config.getInitParameter(LOCATION);

        ZApplicationContext context = new ZApplicationContext(initParameter);



    }
}
