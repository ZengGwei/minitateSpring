package com.zgw.imitate.spring.demo.action;


import com.zgw.imitate.spring.demo.service.IDemoService;
import com.zgw.imitate.spring.framework.annotation.Autowried;
import com.zgw.imitate.spring.framework.annotation.Controller;
import com.zgw.imitate.spring.framework.annotation.RequestMapping;

@Controller
public class MyAction {

		@Autowried
		IDemoService demoService;

		@RequestMapping("/index.html")
		public void query(){

		}

}
