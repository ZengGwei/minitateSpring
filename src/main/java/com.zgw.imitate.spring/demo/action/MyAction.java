package com.zgw.imitate.spring.demo.action;


import com.zgw.imitate.spring.framework.annotation.ZController;
import com.zgw.imitate.spring.framework.annotation.ZRequestMapping;
import com.zgw.imitate.spring.framework.annotation.ZRequestParam;
import com.zgw.imitate.spring.framework.webmvc.ZModelAndView;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ZController
public class MyAction {

//		@ZAutowried
//		IDemoService demoService;

		@ZRequestMapping("/first.html")
		public ZModelAndView query(@ZRequestParam("user") String user){

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
