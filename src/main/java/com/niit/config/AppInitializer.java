package com.niit.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		System.out.println("Starting of the Method getRootConfigClasses");
		return new Class<?>[]{DBConfig.class,WebConfig1.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		System.out.println("Starting of the Method getServletConfigClasses");
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		System.out.println("Starting of the Method getServletMappings");
		return new String[]{ "/"};
	}

}
