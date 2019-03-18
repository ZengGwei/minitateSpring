package com.zgw.imitate.spring.demo.action;


import com.zgw.imitate.spring.demo.service.IDemoService;
import com.zgw.imitate.spring.framework.annotation.Autowried;
import com.zgw.imitate.spring.framework.annotation.Controller;
import com.zgw.imitate.spring.framework.annotation.RequestMapping;
import com.zgw.imitate.spring.framework.annotation.RequestParam;
import com.zgw.imitate.spring.framework.webmvc.ZModelAndView;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MyAction {

//		@Autowried
//		IDemoService demoService;

		@RequestMapping("/first.html")
		public ZModelAndView query(@RequestParam("user") String user){

			Map<String,Object> model = new HashMap<String, Object>();
			model.put("user",user);
			return new ZModelAndView("first.html",model);
		}

		private ZModelAndView out(HttpServletResponse resp,String str){
			try {
				resp.getWriter().write(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

}
