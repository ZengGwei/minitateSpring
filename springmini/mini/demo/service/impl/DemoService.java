package com.spring.mini.demo.service.impl;


import com.spring.mini.demo.service.IDemoService;
import com.spring.mini.spring.annotion.Service;

@Service
public class DemoService implements IDemoService {

	public String get(String name) {
		return "My name is " + name;
	}

}
