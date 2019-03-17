package com.zgw.imitate.spring.framework.webmvc;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/17
 */
public class ZViewResolver {
//目的：将一个静态文件，变为动态文件   根据用户传不同参数，返回不同结果
    //最终输出字符串

    private String viewName ;

    private File templateFile;

    public ZViewResolver(String viewName, File templateFile) {
        this.viewName = viewName;
        this.templateFile = templateFile;
    }

    public String viewResolver(ZModelAndView mv) throws IOException {
        StringBuilder sb =new StringBuilder();
        RandomAccessFile ra =new RandomAccessFile(this.templateFile,"r");
        String line =null;
        while (null !=(line=ra.readLine())){
            Matcher matcher = matcher(line);
            while (matcher.find()){
                for (int i = 1; i <=matcher.groupCount() ; i++) {

                    //要把￥{} 吧中间的字符串取出
                    String paramName = matcher.group(i);
                    Object paramValue = mv.getModel().get(paramName);
                    if (null == paramName){continue;}
                    line = line.replaceAll("￥\\{"+paramName+"\\}",paramValue.toString());
                }
            }
            sb.append(line);
        }
        return sb.toString();
    }

    private Matcher matcher(String str){
        Pattern pattern = Pattern.compile("￥\\{(.+?)\\}",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public File getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(File templateFile) {
        this.templateFile = templateFile;
    }
}
