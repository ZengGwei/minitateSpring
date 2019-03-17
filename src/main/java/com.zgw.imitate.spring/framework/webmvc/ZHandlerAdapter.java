package com.zgw.imitate.spring.framework.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/17
 */
public class ZHandlerAdapter {

    private Map<String,Integer> paramMapping;

    public ZHandlerAdapter(Map<String, Integer> paramMapping) {
        this.paramMapping = paramMapping;
    }

    /**
     *
     * @param req
     * @param resp
     * @param handlerMapping 该参数封装了 controller method url 信息
     * @return
     */
    public ZModelAndView handle(HttpServletRequest req, HttpServletResponse resp, ZHandlerMapping handlerMapping) throws InvocationTargetException, IllegalAccessException {
        //根据用户的请求参数信息，跟method中的参数信息动态匹配
        //resp 只是为了赋值给方法参数就此而已。传递功能


        //当用户传ModelAndView 为空的时候 才会new一个默认
        //方法重载：形参的决定因素：参数的个数、参数的类型、参数的顺序、方法的名字
        Class<?>[] parameterTypes = handlerMapping.getMethod().getParameterTypes();

        //拿到自定义命名参数所在位置
        Map<String,String[]> parameterMap = req.getParameterMap();//用户传过来的参数
        //构造实参列表
        Object[] paramValues = new Object[parameterTypes.length];
        for (Map.Entry<String,String[]> param:parameterMap.entrySet()){
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]]","").replaceAll("\\s","");
            if(!this.paramMapping.containsKey(param.getKey())){
                continue;
            }
            int index =this.paramMapping.get(param.getKey());

            //因为页面传值是string,方法参数类型 不确定
            //要针对性类型转换

            caseStringValue(value,parameterTypes[index]);

        }
        if (this.paramMapping.containsKey(HttpServletRequest.class.getName())){
            int reqIndex = this.paramMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = req;
        }
        if (this.paramMapping.containsKey(HttpServletResponse.class.getName())){
            int respIndex = this.paramMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = resp;
        }



        //4.从handler 中取出controller。method,然后利用反射机制进行调用

        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);
        if (result ==null)
            return null;
        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == ZModelAndView.class;

        if(isModelAndView){
            return (ZModelAndView) result;
        }else
            return null;
    }
    //springMVC 有很多类型转换器 完成 这里简单一点
    private Object caseStringValue(String val,Class<?> clazz){
        if (clazz ==String.class){
            return val;
        }else if (clazz == Integer.class){
            return Integer.valueOf(val.trim());
        }else if (clazz == Double.class){
            return Double.valueOf(val.trim());
        }
        return null;
    }

}
