package com.zgw.imitate.spring.formework.context.support;

import com.zgw.imitate.spring.formework.beans.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *  用于对配置文件进行查找、读取、解析
 * Created by gw.Zeng on 2019/3/10
 */
public class BeanDefinitionReader {

    private Properties config = new Properties();

    private List<String> registerBeanClass = new ArrayList<String>();

    private  final  String SCAN_PACKAGE ="scanPackage";

    public BeanDefinitionReader(String ... locations) {
        //在Spring中是通过Reader去查找和定位
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:",""));//忽略循环

        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(null != is){is.close();}
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        this.doScanner(config.getProperty(SCAN_PACKAGE));
    }

    public List<String> loadBeanDefinitions(){
        return this.registerBeanClass;
    }

    //每注册一个className,就返回一个BeanDefinition对象()
    public BeanDefinition registerBean(String className){
        if (this.registerBeanClass.contains(className)){
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setBeanClassName(className);
            beanDefinition.setFactoryBeanName(lowerFirstCase(className.substring(className.lastIndexOf(".")+1)));
            return beanDefinition;
        }
        return null;
    }

    //递归扫描所配置文件下的包，找到包下所有类放到list中
    private void doScanner(String packageName){
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.","/"));

        File classDir = new File(url.getFile());

        for(File file :classDir.listFiles()){
            if (file.isDirectory()){
                doScanner(packageName+"."+file.getName());
            }else{
                registerBeanClass.add(packageName+"."+file.getName().replace(".class",""));
            }
        }

    }

    public Properties getConfig(){
        return this.config;
    }

    private String lowerFirstCase(String str){
        char [] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

}
