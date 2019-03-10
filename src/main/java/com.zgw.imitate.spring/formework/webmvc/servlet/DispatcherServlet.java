package com.zgw.imitate.spring.formework.webmvc.servlet;

import com.zgw.imitate.spring.formework.context.ZApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/10
 */
public class DispatcherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        ZApplicationContext context = new ZApplicationContext();

    }
}
