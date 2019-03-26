package com.zgw.imitate.spring.framework.context;

import com.zgw.imitate.spring.framework.annotation.ZAutowried;
import com.zgw.imitate.spring.framework.annotation.ZController;
import com.zgw.imitate.spring.framework.annotation.ZService;
import com.zgw.imitate.spring.framework.aop.AopConfig;
import com.zgw.imitate.spring.framework.beans.BeanDefinition;
import com.zgw.imitate.spring.framework.beans.BeanPostProcessor;
import com.zgw.imitate.spring.framework.beans.BeanWrapper;
import com.zgw.imitate.spring.framework.context.support.BeanDefinitionReader;
import com.zgw.imitate.spring.framework.core.BeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/10
 */
public class ZApplicationContext extends DefaultListableBeanFactory implements BeanFactory {

    private String[] configLocation;

    private BeanDefinitionReader reader;



    //用来保证注册式单列
    private Map<String,Object> beanCacheMap = new HashMap<String, Object>();

    //用来存储被代理过的对象
    private Map<String,BeanWrapper> beanWrapperMap = new ConcurrentHashMap<String, BeanWrapper>();

    public ZApplicationContext(String ... locations) {
        this.configLocation = locations;
        this.refresh();
    }

    public  void refresh(){

        //定位
        this.reader =new BeanDefinitionReader(configLocation);

        //加载
        List<String> beanDefinitions = this.reader.loadBeanDefinitions();

        //注册
        doRegister(beanDefinitions);

        //依赖注入（lazy-init=false）执行依赖注入调用getBean().
        doAutowried();

    }

    //自动执行自动化依赖注入
    private  void doAutowried(){//todo 应用递归依赖
        for(Map.Entry<String,BeanDefinition> beanDefinitionEntry:this.beanDefinitionMap.entrySet()){
            String beanName = beanDefinitionEntry.getKey();
            if(!beanDefinitionEntry.getValue().isLazyInit()){
                Object bean = getBean(beanName);
                if (bean == null){return;}
                populateBean("",bean.getClass());
            }
        }
//        for(Map.Entry<String,BeanWrapper> beanWrapperEntry:this.beanWrapperMap.entrySet()){
//            populateBean(beanWrapperEntry.getKey(),beanWrapperEntry.getValue().getWrappedInstance());//TODO 注入代理对象？？
//        }
    }

    public void populateBean(String beanName,Object instance){//给属性对象赋值
        Class<?> clazz = instance.getClass();

        if(!(clazz.isAnnotationPresent(ZController.class)|| clazz.isAnnotationPresent(ZService.class))){  return; }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:fields){
            if (!field.isAnnotationPresent(ZAutowried.class)){continue;}
            ZAutowried ZAutowried = field.getAnnotation(ZAutowried.class);
            String autowriedBeanName = ZAutowried.value().trim();
            if("".equals(autowriedBeanName)){
                autowriedBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try {
//                System.out.println("=======================" +instance +"," + autowriedBeanName + "," + this.beanWrapperMap.get(autowriedBeanName));

                field.set(instance,this.beanWrapperMap.get(autowriedBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    //将beaDefinition注册到beanDefinitionmap
    private void doRegister(List<String> beanDefinitions) {
      try {
          for (String className : beanDefinitions) {
              Class<?> beanClass = Class.forName(className);

              //beanName有三种情况：1.默认 2.自定义 3.接口注入

              if (beanClass.isInterface()){continue;}
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
        String calssName = beanDefinition.getBeanClassName();
        try{
            //生成通知事件
            BeanPostProcessor beanPostProcessor = new BeanPostProcessor();

            Object instanceBean = instanceBean(beanDefinition);
            if (null == instanceBean){return null;}

            //在实例初始化前 调用一次
            beanPostProcessor.postProcessBeforeInitialization(instanceBean,beanName);

            BeanWrapper beanWrapper = new BeanWrapper(instanceBean);

            beanWrapper.setAopConfig(instanceAopConfig(beanDefinition));

            beanWrapper.setBeanPostProcessor(beanPostProcessor);
            this.beanWrapperMap.put(beanName,beanWrapper);

            //在实例初始化之后调用一次
            beanPostProcessor.postProcessAfterInitialization(instanceBean,beanName);
            return  this.beanWrapperMap.get(beanName).getWrappedInstance();//这样返回一个包装过的Bean,增大可操作空间
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private AopConfig instanceAopConfig(BeanDefinition beanDefinition) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        AopConfig aopConfig = new AopConfig();

        String pointCut = reader.getConfig().getProperty("pointCut");
        String[] aspectBefore = reader.getConfig().getProperty("aspectBefore").split("\\s");
        String[] aspectAfter = reader.getConfig().getProperty("aspectAfter").split("\\s");

        String className =beanDefinition.getBeanClassName();
        Class<?> aClass = Class.forName(className);

        Pattern pattern = Pattern.compile(pointCut);
        Class<?> aspectClass = Class.forName(aspectBefore[0]);

        for (Method method:aClass.getMethods()){
            //public .* com\.zgw\.imitate\.spring\.demo\.service\..*ZService\..*\(.*\)
            //public java.lang.String com.zgw.imitate.spring.demo.service.impl.ModifyService.add(java.lang.String,java.lang.String)
            Matcher matcher = pattern.matcher(method.toString());
            if (matcher.matches()){
                //把能满足规则的类 添加到aop配置中
                aopConfig.put(method,aspectClass.newInstance(),new Method[]{aspectClass.getMethod(aspectAfter[1]),
                        aspectClass.getMethod(aspectBefore[1])});
            }


        }


        return aopConfig;
    }

    //传一个beanDefinition 返回一个bean实例
    private  Object instanceBean(BeanDefinition beanDefinition){
        Object instance = null;
        String beanClassName = beanDefinition.getBeanClassName();
        try {//TODO 考虑线程安全？？？
            synchronized (this){
                if(this.beanCacheMap.containsKey(beanClassName)){
                    instance =this.beanCacheMap.get(beanClassName);
                }else {
                    Class<?> clazz = Class.forName(beanClassName);
                    instance = clazz.newInstance();



                    this.beanCacheMap.put(beanClassName,instance);
                }
                return instance;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }



    public String[] getBeanDefinitionNames() {
//        return getBeanFactory().getBeanDefinitionNames();//返回什么？？
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount() {

//        return getBeanFactory().getBeanDefinitionCount();
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }


}
