package com.spring.mini.demo.action;
import com.spring.mini.demo.service.IDemoService;
import com.spring.mini.spring.annotion.Autowried;
import com.spring.mini.spring.annotion.Controller;
import com.spring.mini.spring.annotion.RequestMapping;
import com.spring.mini.spring.annotion.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/demo")
public class DemoAction {
    @Autowried
    private IDemoService demoService;

    @RequestMapping("/query.json")
	public void query(HttpServletRequest req,HttpServletResponse resp,
		   @RequestParam("name") String name){
		String result = demoService.get(name);
		System.out.println(result);
//		try {
//			resp.getWriter().write(result);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@RequestMapping("/edit.json")
	public void edit(HttpServletRequest req,HttpServletResponse resp,Integer id){

	}

}
