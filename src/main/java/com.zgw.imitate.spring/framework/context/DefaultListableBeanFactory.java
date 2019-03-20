package com.zgw.imitate.spring.framework.context;

import com.zgw.imitate.spring.framework.beans.BeanDefinition;
import com.zgw.imitate.spring.framework.context.AbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/18
 */
public class DefaultListableBeanFactory extends AbstractApplicationContext {

    //保存bean配置信息
    protected Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    @Override
    protected void onRefresh() {
        super.onRefresh();
    }

    @Override
    protected void refreshBeanFactory() {
        super.refreshBeanFactory();
    }
}
