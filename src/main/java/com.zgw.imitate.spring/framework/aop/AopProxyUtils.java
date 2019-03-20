package com.zgw.imitate.spring.framework.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/19
 */
public class AopProxyUtils {

    public static  Object getTargetObject(Object proxy)throws  Exception{
        //先判断是不是在理对象
        if(!isAopProxy(proxy)){
            return proxy;
        }
        Object proxyTargetObject = getProxyTargetObject(proxy);//找到原生对象
        return  proxyTargetObject;
    }

    private static  boolean isAopProxy(Object object){
        return Proxy.isProxyClass(object.getClass());
    }

    private static  Object getProxyTargetObject(Object proxy) throws  Exception{
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);

        AopProxy aopProxy = (AopProxy)h.get(proxy);
        Field target = aopProxy.getClass().getDeclaredField("target");
        target.setAccessible(true);
        return target.get(aopProxy);
    }


}
