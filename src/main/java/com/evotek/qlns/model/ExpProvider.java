package com.evotek.qlns.model;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ExpProvider {
	private Environment env;
	public static List<String> listExp;
//	static {
//		listExp =new ArrayList<String>();
//	}
	ExpProvider(Environment env){
		this.env=env;
		init();
	}
	private void init() {
		String pre = env.getProperty("exp.list");
		pre = pre.replace(" ", "");
		String[] arr = pre.split(",");
		listExp =Arrays.asList(arr);
//		System.out.println("expProvider:");
//		listExp.forEach(c-> System.out.println(c));
	}
}
