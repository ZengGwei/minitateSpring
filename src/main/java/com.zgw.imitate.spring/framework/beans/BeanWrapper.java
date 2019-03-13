package com.zgw.imitate.spring.framework.beans;

import com.zgw.imitate.spring.framework.core.FactryBean;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/10
 */
public class BeanWrapper extends FactryBean {
    //加入 观察者 模式 //支持事件响应，加监听

    private  BeanPostProcessor beanPostProcessor;

    private  Object wrapperInstance;
    //原始的通过反射new出来，包装 保存
    private  Object originalInstance;

    public BeanWrapper(Object instance){
        this.wrapperInstance = instance;
        this.originalInstance = instance;
    }

    public Object getWrappedInstance(){
        return this.wrapperInstance;
    }

    //返回代理以后的class
    Class<?> getWrappedClass(){
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
}
