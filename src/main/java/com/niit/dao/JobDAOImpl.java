package com.niit.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.model.Job;
import com.niit.model.JobApplication;

@Repository("jobDAOImpl")
public class JobDAOImpl{

	private static final Logger log = LoggerFactory.getLogger(JobDAOImpl.class);

	@Autowired(required = true)
	private SessionFactory sessionFactory;

	public JobDAOImpl(SessionFactory sessionFactory) {
		try {
			this.sessionFactory = sessionFactory;
		} catch (Exception e) {
			log.error(" Unable to connect to db");
			e.printStackTrace();
		}
	}

	@Transactional
	public List<Job> getAllOpendJobs() {
		log.debug("Starting of method getAllOpendJobs");
		String hql = "from Job where status ='"+"V'";
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		log.debug("Starting of method getAllOpendJobs");
		
		
	/*  return	sessionFactory.getCurrentSession().createCriteria(Job.class)
		.add(Restrictions.eqProperty("status", "V"))
		.list();*/
		
		return query.list();
		//return null;
	}

	@Transactional
	public Job getJobDetails(Long id) {
		return  (Job)sessionFactory.getCurrentSession().get(Job.class, id);
	
	}
	
	@Transactional
	public JobApplication getJobApplication(int userId, Long jobId) {
		
		log.debug("Starting of the method getJobApplication");
		String hql = "from JobApplication where userId ='"+ userId + "' and jobId='"+jobId + "'";
		log.debug("hql" + hql);
		return (JobApplication) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult();
		
	
	}
	
	@Transactional
	public JobApplication getJobApplication(Long jobId) {
		return (JobApplication) sessionFactory.getCurrentSession().get(JobApplication.class, jobId);
	
	}

	@Transactional
	public boolean updateJob(Job job) {
		try {
			sessionFactory.getCurrentSession().update(job);
			return true;
		} catch (HibernateException e) {
			
			e.printStackTrace();
			return false;
		}
		}
	
	@Transactional
	public boolean updateJob(JobApplication jobApplication) {
		try {
			sessionFactory.getCurrentSession().update(jobApplication);
			return true;
		} catch (HibernateException e) {
			
			e.printStackTrace();
			return false;
		}
		}

	
	@Transactional
	public boolean save(JobApplication jobApplied) {
		log.debug("->->Starting of the save job application");
		try {
			/*jobApplied.setId(100L);*/
			jobApplied.setId(getMaxId());
			sessionFactory.getCurrentSession().save(jobApplied);
			
		} catch (HibernateException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private Long getMaxId()
	{
		log.debug("->->Starting of the method getMaxId");
		
		Long maxID = 100L;
		try {
			String hql = "select max(id) from JobApplication";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			maxID =  (Long) query.uniqueResult();
		} catch (HibernateException e) {
			log.debug("It seems this is first record. setting initial id is 100 :");
			maxID = 100L;
			e.printStackTrace();
		}
		log.debug("Max id :"+maxID);
		return maxID+1;

	}
	
	
	@Transactional
	public boolean save(Job job) {
		log.debug("->->Starting of the save Job");
		try {
			sessionFactory.getCurrentSession().save(job);
		} catch (HibernateException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	@Transactional
	public List<JobApplication> getMyAppliedJobs(int userId) {
		log.debug("Starting of method getMyAppliedJobs");
		System.out.println("____________________________");
		System.out.println(userId);
		System.out.println("____________________________");
		String hql = "from JobApplication where userId ='"+ userId +"'";
		List<JobApplication> JApp =sessionFactory.openSession().createQuery(hql).list();
		log.debug("Ending of method getMyAppliedJobs");
		return JApp;
		
	}



}
