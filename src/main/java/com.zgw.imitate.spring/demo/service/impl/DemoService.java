package com.zgw.imitate.spring.demo.service.impl;


import com.zgw.imitate.spring.demo.service.IDemoService;
import com.zgw.imitate.spring.framework.annotation.Service;

@Service
public class DemoService implements IDemoService {

	public String get(String name) {
		return "My name is " + name;
	}

}
