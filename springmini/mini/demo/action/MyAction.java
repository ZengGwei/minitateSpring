package com.spring.mini.demo.action;


import com.spring.mini.demo.service.IDemoService;
import com.spring.mini.spring.annotion.Autowried;
import com.spring.mini.spring.annotion.Controller;
import com.spring.mini.spring.annotion.RequestMapping;

@Controller
public class MyAction {

		@Autowried
        IDemoService demoService;

		@RequestMapping("/index.html")
		public void query(){

		}

}
