package com.zgw.imitate.spring.framework.beans;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.zgw.imitate.spring.framework.aop.AopConfig;
import com.zgw.imitate.spring.framework.aop.AopProxy;
import com.zgw.imitate.spring.framework.core.FactryBean;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/10
 */
public class BeanWrapper extends FactryBean {
    private AopProxy aopProxy=new AopProxy();

    private  BeanPostProcessor beanPostProcessor;//加入 观察者 模式 //支持事件响应，加监听

    private  Object wrapperInstance;
    //原始的通过反射new出来，包装 保存
    private  Object originalInstance;

    public BeanWrapper(Object instance){

        this.wrapperInstance = aopProxy.getProxy(instance);//从这里开始，把动态的代码添加进来
        this.originalInstance = instance;
    }

    public Object getWrappedInstance(){
        return this.wrapperInstance;
    }

    //返回代理以后的class
    public Class<?> getWrappedClass(){
        return  this.wrapperInstance.getClass();
    }

    public BeanPostProcessor getBeanPostProcessor() {
        return beanPostProcessor;
    }

    public void setBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessor = beanPostProcessor;
    }

    public Object getOriginalInstance() {
        return originalInstance;
    }

    public void setOriginalInstance(Object originalInstance) {
        this.originalInstance = originalInstance;
    }


    public void setAopConfig(AopConfig aopConfig){
        aopProxy.setConfig(aopConfig);
    }
}
