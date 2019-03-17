package com.zgw.imitate.spring.framework.webmvc;

import com.sun.istack.internal.Nullable;

import java.util.Map;

/**
 * 〈〉*
 * Created by gw.Zeng on 2019/3/17
 */
public class ZModelAndView {
   private  String viewNmae;
   private Map<String,Object> model;

    public ZModelAndView(String viewNmae, Map<String, Object> model) {
        this.viewNmae = viewNmae;
        this.model = model;
    }

    public ZModelAndView() { }


    private Object getModelMap() {
        return null;
    }

    public String getViewNmae() {
        return viewNmae;
    }

    public void setViewNmae(String viewNmae) {
        this.viewNmae = viewNmae;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
