package com.zgw.imitate.spring.framework.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/17
 */
public class ZHandlerMapping {
    private Object controller;

    private Method method;

//    private String url;//不能用字符串
    private Pattern urlPattern;//路径的正则

    public ZHandlerMapping( Pattern url,Object controller, Method method) {
        this.controller = controller;
        this.method = method;
        this.urlPattern = url;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(Pattern urlPattern) {
        this.urlPattern = urlPattern;
    }
}
