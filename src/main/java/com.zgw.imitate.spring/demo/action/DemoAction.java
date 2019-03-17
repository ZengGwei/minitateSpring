package com.zgw.imitate.spring.demo.action;


import com.zgw.imitate.spring.demo.service.IDemoService;
import com.zgw.imitate.spring.framework.annotation.Autowried;
import com.zgw.imitate.spring.framework.annotation.Controller;
import com.zgw.imitate.spring.framework.annotation.RequestMapping;
import com.zgw.imitate.spring.framework.annotation.RequestParam;
import com.zgw.imitate.spring.framework.webmvc.ZModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/demo")
public class DemoAction {
    @Autowried
    private IDemoService demoService;

    @RequestMapping("/query.json")
	public ZModelAndView query(HttpServletRequest req, HttpServletResponse resp,
							   @RequestParam("name") String name){
		String result = demoService.get(name);
		System.out.println(result);
//		try {
//			resp.getWriter().write(result);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return new ZModelAndView(null,null);
	}

	@RequestMapping("/edit.json")
	public ZModelAndView edit(HttpServletRequest req,HttpServletResponse resp,Integer id){
		return new ZModelAndView();
	}

}
