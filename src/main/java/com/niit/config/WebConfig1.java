package com.niit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.niit")
public class WebConfig1 extends WebMvcConfigurerAdapter {
	
	
	@Bean
	public InternalResourceViewResolver getViewResolver(){
		System.out.println("Starting of the Method viewResolver");
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;		
	}
	
	@Bean(name = "multipartResolver")
    public CommonsMultipartResolver multiPartResolver(){
		
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        return resolver;
    }
	
}