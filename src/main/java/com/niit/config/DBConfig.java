package com.niit.config;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.niit.dao.FriendDAOImpl;
import com.niit.dao.JobDAOImpl;
import com.niit.dao.UserDAOImpl;
import com.niit.model.Friend;
import com.niit.model.Job;
import com.niit.model.JobApplication;
import com.niit.model.User;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.niit")
public class DBConfig 
{
	
	@Bean(name="sessionFactory")
	public SessionFactory getSessionFactory()
	{
		//Hibernate related properties
		Properties hibernateprop=new Properties();
		hibernateprop.setProperty("hibernate.hbm2ddl.auto", "update");
		hibernateprop.put("hibernate.show_sql", "true"); //optional
		hibernateprop.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
		
		//Adding the Database related Properties
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:XE");
		dataSource.setUsername("india");
		dataSource.setPassword("india");
		
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
		sessionBuilder.addProperties(hibernateprop);
		sessionBuilder.scanPackages("com.niit");
		/*sessionBuilder.addAnnotatedClass(Blog.class);*/
		SessionFactory sessionfactory=sessionBuilder.buildSessionFactory();
		
		System.out.println("Session Factory Object Created");
		
		return sessionfactory;
		
	}
	
	@Bean(name="userDAOImpl")
	public UserDAOImpl getUserDAOImpl(SessionFactory sessionFactory)
	{
		return new UserDAOImpl(sessionFactory);
	}
	
	@Bean(name="user")
	public User getUser(SessionFactory sessionFactory)
	{
		return new User();
	}
	
	@Bean(name="jobDAOImpl")
	public JobDAOImpl getJobDAO(SessionFactory sessionFactory)
	{
		return new JobDAOImpl(sessionFactory);
	}
	
	@Bean(name="job")
	public Job getJob(SessionFactory sessionFactory)
	{
		return new Job();
	}
	
	@Bean(name="jobApplication")
	public JobApplication getJobApplied(SessionFactory sessionFactory)
	{
		return new JobApplication();
	}

	@Bean(name="friendDAOImpl")
	public FriendDAOImpl getFriendDAO(SessionFactory sessionFactory)
	{
		return new FriendDAOImpl(sessionFactory);
	}
	
	@Bean(name="friend")
	public Friend getFriend(SessionFactory sessionFactory)
	{
		return new Friend();
	}
	
	@Bean(name="transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory)
	{
		System.out.println("Transaction Manager");
		HibernateTransactionManager transactionManager=new HibernateTransactionManager(sessionFactory);
		return transactionManager;
	}

}