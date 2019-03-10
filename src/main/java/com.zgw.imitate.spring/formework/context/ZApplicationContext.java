package com.zgw.imitate.spring.formework.context;

import com.zgw.imitate.spring.formework.beans.BeanDefinition;
import com.zgw.imitate.spring.formework.context.support.BeanDefinitionReader;
import com.zgw.imitate.spring.formework.core.BeanFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/10
 */
public class ZApplicationContext implements BeanFactory {

    private String[] configLoaction;

    private BeanDefinitionReader reader;

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    public ZApplicationContext(String ... locations) {
        this.configLoaction = locations;
        this.refresh();
    }

    public  void refresh(){

        //定位
        this.reader =new BeanDefinitionReader(configLoaction);

        //加载
        List<String> beanDefinitions = this.reader.loadBeanDefinitions();

        //注册
        doRegister(beanDefinitions);

        //依赖注入（lazy-init=false）执行依赖注入调用getBean().


    }

    //将beaDefinition注册到beanDefinitionmap
    private void doRegister(List<String> beanDefinitions) {
      try {
          for (String className : beanDefinitions) {
              Class<?> beanClass = Class.forName(className);

              //beanName有三种情况：1.默认 2.自定义 3.接口注入

              if (beanClass.isAnnotation()){continue;}
              BeanDefinition beanDefinition = reader.registerBean(className);
              if (beanDefinition != null){
                  this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
              }

              Class<?>[] interfaces = beanClass.getInterfaces();//获取接口
              for (Class<?> clazz :interfaces){
                  //如果多个实现类，只能覆盖/出错。可以自定义名字
                  this.beanDefinitionMap.put(clazz.getName(),beanDefinition);
              }

              //以上 容器初始化完成

          }
      }catch (Exception e){
          e.printStackTrace();
      }

    }


    //通过读取BeanDefinition中的信息，通过反射机制创建一个实例并返回
    //Spring 会用一个BeanWrapper来进行一次包装 返回对象            装饰器模式 1.保留原来的OOP关系 2.可扩展和增强(AOP)
    @Override//依赖注入从这个方法开始
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        
        return null;
    }
}
