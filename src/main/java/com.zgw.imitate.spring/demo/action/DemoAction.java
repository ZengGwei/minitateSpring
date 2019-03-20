package com.zgw.imitate.spring.demo.action;


import com.zgw.imitate.spring.demo.service.IDemoService;
import com.zgw.imitate.spring.framework.annotation.ZAutowried;
import com.zgw.imitate.spring.framework.annotation.ZController;
import com.zgw.imitate.spring.framework.annotation.ZRequestMapping;
import com.zgw.imitate.spring.framework.annotation.ZRequestParam;
import com.zgw.imitate.spring.framework.webmvc.ZModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ZController
@ZRequestMapping("/demo")
public class DemoAction {
    @ZAutowried
    private IDemoService demoService;

    @ZRequestMapping("/query.json")
	public ZModelAndView query(HttpServletRequest req, HttpServletResponse resp,
							   @ZRequestParam("name") String name){
		String result = demoService.get(name);
		System.out.println(result);
//		try {
//			resp.getWriter().write(result);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return new ZModelAndView(null,null);
	}

	@ZRequestMapping("/edit.json")
	public ZModelAndView edit(HttpServletRequest req,HttpServletResponse resp,Integer id){
		return new ZModelAndView();
	}

}
