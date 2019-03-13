package com.zgw.imitate.spring.framework.beans;

import com.zgw.imitate.spring.framework.core.FactryBean;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/10
 */
//用作事件监听
public class BeanPostProcessor extends FactryBean {

    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }


    public Object postProcessAfterInitialization(Object bean, String beanName)  {
        return bean;
    }
}
