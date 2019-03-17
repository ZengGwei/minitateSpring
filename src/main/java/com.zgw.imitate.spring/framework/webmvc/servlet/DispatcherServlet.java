package com.zgw.imitate.spring.framework.webmvc.servlet;

import com.zgw.imitate.spring.demo.action.DemoAction;
import com.zgw.imitate.spring.framework.annotation.Controller;
import com.zgw.imitate.spring.framework.annotation.RequestMapping;
import com.zgw.imitate.spring.framework.annotation.RequestParam;
import com.zgw.imitate.spring.framework.context.ZApplicationContext;
import com.zgw.imitate.spring.framework.webmvc.ZHandlerAdapter;
import com.zgw.imitate.spring.framework.webmvc.ZHandlerMapping;
import com.zgw.imitate.spring.framework.webmvc.ZModelAndView;
import com.zgw.imitate.spring.framework.webmvc.ZViewResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/10
 */

public class DispatcherServlet extends HttpServlet {

    private final String LOCATION = "contextConfigLocation";

    //    private Map<String, Method> handlerMapping = new HashMap<String, Method>();
    //拿不到方法名？handlerMapping  存入一个封装的对象
//    private Map<String, ZHandlerMapping> handlerMapping = new HashMap<String, ZHandlerMapping>();
    //所以handlerMapping最核心的设计，一个封装的对象 list
    private List<ZHandlerMapping> handlerMapping = new ArrayList<ZHandlerMapping>();


    private Map<ZHandlerMapping, ZHandlerAdapter> handlerAdapters = new HashMap<ZHandlerMapping, ZHandlerAdapter>();

    private List<ZViewResolver> viewResolversList = new ArrayList<ZViewResolver>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        String initParameter = config.getInitParameter(LOCATION);
        //相当于把IOC容器初始化
        ZApplicationContext context = new ZApplicationContext(initParameter);

        //该方法由实现ApplicationContextAware定义onRefresh()方法调用
        initStrategies(context);//springMvc


    }

    private void initStrategies(ZApplicationContext context) {
        //springMVC九大组件---九种策略，每种策略可以自定义干预，单结果一致
        initMultipartResolver(context);//文件上传解析
        initLocaleResolver(context);//本地化解析
        initThemeResolver(context);//主题解析

        //用来保存Controller中配置的requestMapping和Method的对应关系
        initHandlerMappings(context);

        //用来动态匹配Method的参数，包括类型转换，动态赋值
        initHandlerAdapters(context);//动态匹配参数
        initHandlerExceptionResolvers(context);//异常处理
        initRequestToViewNameTranslator(context);//直接解析请求到视图名

        //自己解析一套模板语言
        initViewResolvers(context);//通过viewResolver解析逻辑视图到具体视图实现
        initFlashMapManager(context);//flash映射管理器

    }

    private void initFlashMapManager(ZApplicationContext context) {

    }

    private void initRequestToViewNameTranslator(ZApplicationContext context) {

    }

    private void initHandlerExceptionResolvers(ZApplicationContext context) {

    }

    private void initThemeResolver(ZApplicationContext context) {

    }

    private void initLocaleResolver(ZApplicationContext context) {

    }

    private void initMultipartResolver(ZApplicationContext context) {
    }


    //将Controller中配置的requestMapping和Method的进行一一对应
    private void initHandlerMappings(ZApplicationContext context) {
        //这里是个Map<String,Method>--->springMVC用个封装的list不用map
        //

        //从容器取到所有的实例
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object instance = context.getBean(beanName);
            Class<?> aClass = instance.getClass();
            //
            if (!aClass.isAnnotationPresent(Controller.class)) {
                continue;
            }
            String beanUrl = "";
            if (aClass.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping re = aClass.getAnnotation(RequestMapping.class);
                String baseUrl = re.value();
            }
            //扫描所有publlic方法
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }

                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String regex = "/" + beanUrl + requestMapping.value().replaceAll("\\*",".*").replaceAll("/+", "");

                Pattern pattern = Pattern.compile(regex);

                this.handlerMapping.add(new ZHandlerMapping(pattern, instance, method));

                System.out.println("Mapping:" + regex + "," + method);

            }

        }
    }

    private void initHandlerAdapters(ZApplicationContext context) {
        //在初始化阶段，参数名字和类型 按一定顺序保存下来，  反射调用的时候 ，传的形参是一个数组
        //可以通过记录这些参数的位置index,挨个从数组填值，这样就和参数的顺序无关。
        for (ZHandlerMapping handlerMapping : this.handlerMapping) {
            //每一个方法有一个参数列表，这里保存形参列表
            Map<String, Integer> paramMapping = new HashMap<String, Integer>();
            Annotation[][] parameterAnnotations = handlerMapping.getMethod().getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (Annotation a : parameterAnnotations[i]) {
                    if (a instanceof RequestParam) {
                        String paramName = ((RequestParam) a).value();
                        if (!"".equals(paramName.trim())) {
                            paramMapping.put(paramName, i);
                        }
                    }
                }
            }

            //处理非命名参数 没有加注解requsetParam，是通过.ams,获取参数名。这里省略---》只处理request和response
            Class<?>[] parameterTypes = handlerMapping.getMethod().getParameterTypes();
            for (int j = 0; j < parameterTypes.length; j++) {
                Class<?> type = parameterTypes[j];
                if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                    paramMapping.put(type.getName(), j);
                }

            }
            this.handlerAdapters.put(handlerMapping, new ZHandlerAdapter(paramMapping));
        }

    }

    private void initViewResolvers(ZApplicationContext context) {
        //在页面路径 xxx/first.html //解决页面名字和模板文件关联问题。
        String templateRoot = context.getConfig().getProperty("templateRoot");//文件目录

        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);

        for (File template : templateRootDir.listFiles()) {
            this.viewResolversList.add(new ZViewResolver(template.getName(), template));
        }

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String url = req.getRequestURI();
//        String contextPath =req.getContextPath();
//        url=url.replace(contextPath,"").replaceAll("/+","/");
//        ZHandlerMapping handlerMapping = this.handlerMapping.get(url);
//        try {
//            ZModelAndView mv=(ZModelAndView) handlerMapping.getMethod().invoke(handlerMapping.getController(),null);
//
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }

//        Method method = handlerMapping.get(url);
        //对象，方法名要方法名才能用，对象从IOC容器中取
//        method.invoke();//方法名？？？？
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Exception,details:\r\n" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "")
                    .replaceAll("\\s", "\r\n") + "");
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, InvocationTargetException, IllegalAccessException {
        //根据用户请求url
        ZHandlerMapping handlerMapping = getHandler(req);
        if(handlerMapping==null){
            resp.getWriter().write("404 Not Found\r\n@zspringmvc");
        }
        ZHandlerAdapter handlerAdapter = getHandlerAdapter(handlerMapping);

        ZModelAndView modelAndView = handlerAdapter.handle(req, resp, handlerMapping);

        processDispatchResult(resp, modelAndView);


    }

    private void processDispatchResult(HttpServletResponse resp, ZModelAndView modelAndView) throws IOException {
        //调用viewResolver 的resolver 方法
        if (null ==modelAndView){ return;  }
        if (this.viewResolversList.isEmpty()){return;}

        for (ZViewResolver viewResolver :this.viewResolversList){
            if (!modelAndView.getViewNmae().equals(viewResolver.getViewName())){ continue; }
            String out = viewResolver.viewResolver(modelAndView);
            if (null != out){
                resp.getWriter().write(out);
                break;
            }


        }


    }

    private ZHandlerAdapter getHandlerAdapter(ZHandlerMapping handlerMapping) {
        if(this.handlerAdapters.isEmpty()){return null;}
        return  handlerAdapters.get(handlerMapping);
    }

    private ZHandlerMapping getHandler(HttpServletRequest req) {
        if(this.handlerMapping.isEmpty()){return  null;}
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url.replace(contextPath,"").replaceAll("/+","/");
        for(ZHandlerMapping handler:this.handlerMapping){
            Matcher matcher = handler.getUrlPattern().matcher(url);
            if (!matcher.matches()){continue;}
            return handler;
        }
        return null;
    }
}
