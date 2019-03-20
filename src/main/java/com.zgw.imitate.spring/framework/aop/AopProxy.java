package com.zgw.imitate.spring.framework.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/18
 */
public class AopProxy implements InvocationHandler {//代理对象   默认jdk动态代理

    private  AopConfig config;

    //有容器实现再注入
    public void setConfig(AopConfig config){
        this.config =config;
    }

    private Object target;

    //把原生的对象传竟来
    public Object getProxy(Object instance){
        this.target =instance;
        Class<?> aClass = instance.getClass();

        return Proxy.newProxyInstance(aClass.getClassLoader(),aClass.getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //在原始 方法调用前 执行增强的代码
        if(config.contains(method)){
            AopConfig.Aspect aspect = config.get(method);
            aspect.getPoints()[0].invoke(aspect.getAspect());
        }
        //反射调用原始方法
        Object object = method.invoke(this.target, args);

        //在原始 方法调用后 执行增强的代码
        if(config.contains(method)){
            AopConfig.Aspect aspect = config.get(method);
            aspect.getPoints()[1].invoke(aspect.getAspect());
        }
        return object;
    }


}
