package com.zgw.imitate.spring.framework.aop;

import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/18
 */
//只是对application中的expression的封装
    //目标代理对象的一个方法要增强，由自己去实现的业务逻辑去增强。
//配置文件的目的 含有哪些类的哪些方法 需要增强哪些内容   所以对配置文件 内容封装
public class AopConfig {

    //以目标对象要增强的method 作为key，需要增强的内容作为value
    private Map<Method,Aspect>  points = new HashMap<Method,Aspect >();

    public void put(Method method,Object aspect,Method[] points){
        this.points.put(method,new Aspect(aspect,points));
    }

    public Aspect get(Method method){
        return this.points.get(method);
    }

    public  boolean contains(Method method){
        return this.points.containsKey(method);
    }

    //对增强的代码的封装
    public class  Aspect{
        private Object aspect;//赋值 切面 如logAspect对象
        private Method[] points;//赋值 logAspect中的before,after方法

        public Aspect(Object aspect, Method[] points) {
            this.aspect = aspect;
            this.points = points;
        }

        public Object getAspect() {
            return aspect;
        }

        public Method[] getPoints() {
            return points;
        }
    }

}
