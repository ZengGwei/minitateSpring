package com.spring.mini.spring.servlet;

import com.spring.mini.demo.action.DemoAction;
import com.spring.mini.spring.annotion.Autowried;
import com.spring.mini.spring.annotion.Controller;
import com.spring.mini.spring.annotion.Service;
import com.sun.javafx.collections.MappingChange;
import com.sun.jndi.toolkit.url.UrlUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


public class DispatchServlet extends HttpServlet {
    private Properties contextConfig = new Properties();
    //    Ioc容器(spring中是beanDefinitionMap)
    private Map<String, Object> beanMap = new ConcurrentHashMap<String, Object>();

    private List<String> classNames = new ArrayList<String>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Post请求....");
    }



    @Override
    public void init(ServletConfig config) throws ServletException {
        //开始初始化
        //定位
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //加载
        doScanner(contextConfig.getProperty("scanPackage"));
        //注册
        doRegistry();
        //依赖注入,在spring中调用getBean()触发注入
        doAutowired();

        DemoAction demoAction = (DemoAction) beanMap.get("demoAction");
        demoAction.query(null,null,"jik");
        //(springmvc)handlerMapping,配置的url和一个方法关联上
        initHandlerMapping();
    }

    private void initHandlerMapping() {
    }

    private void doAutowired() {
        if(beanMap.isEmpty()){return;}
        for(Map.Entry<String,Object> entry:beanMap.entrySet()){
            Field[] declaredFields = entry.getValue().getClass().getDeclaredFields();
            for (Field field:declaredFields){
                if(!field.isAnnotationPresent(Autowried.class)){
                    continue;
                }
                Autowried autowried = field.getAnnotation(Autowried.class);
                String beanName = autowried.value().trim();
                if("".equals(beanName)){
                    beanName = field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(),beanMap.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }

        }


    }

    private void doRegistry() {
        if(classNames.isEmpty()){
            return;
        }
        try{
            for(String className :classNames){
                Class<?> clazz = Class.forName(className);

                //在spring中用多个子方法来处理
                 if(clazz.isAnnotationPresent(Controller.class)){
                     String beanName = lowerFirstCase(clazz.getSimpleName());
                     Object instance = clazz.newInstance();
                     //在spring中是存放BeanDefinition
                    beanMap.put(beanName,instance);
                 }else if(clazz.isAnnotationPresent(Service.class)){
                     Service service = clazz.getAnnotation(Service.class);
                     //默认用类名首字母注入
                     //如果自己定义beanname,优先使用
                     //如果是接口,使用接口的类型

                     //在spring中分别调用不同的方法
                     String beanName = service.value();
                     if ("".equals(beanName.trim())){
                         beanName = lowerFirstCase(clazz.getSimpleName());
                     }

                     Object instance = clazz.newInstance();
                     beanMap.put(beanName,instance);

                     Class<?>[] interfaces = clazz.getInterfaces();
                     for(Class<?> i:interfaces){
                         beanMap.put(i.getName(),instance);
                     }
                 }else {
                     continue;
                 }
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource("/"+packageName.replaceAll("\\.","/"));
        File classDir = new File(url.getFile());

        for(File file:classDir.listFiles()){
            if(file.isDirectory()){
                doScanner(packageName+"."+file.getName());
            }else {
                classNames.add(packageName+"."+file.getName().replace(".class",""));
            }

        }

    }

    private void doLoadConfig(String location) {
        //在spring是通过一个Reader 去查找和定位。
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(location.replace("classpath:",""));
        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private String lowerFirstCase(String str){
        char[] chars = str.toCharArray();
        chars[0] +=32;
        return String.valueOf(chars);
    }
}
