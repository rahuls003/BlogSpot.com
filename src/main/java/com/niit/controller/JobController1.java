package com.niit.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.JobDAOImpl;
import com.niit.dao.UserDAOImpl;
import com.niit.model.Job;
import com.niit.model.JobApplication;
import com.niit.model.User;;

@RestController
public class JobController1 {

	private static final Logger logger = LoggerFactory.getLogger(JobController1.class);

	@Autowired
	Job job;
	
	@Autowired
	UserDAOImpl userDAOImpl;

	@Autowired
	JobApplication jobApplication;

	@Autowired
	JobDAOImpl jobDAOImpl;

	@Autowired
	HttpSession session;

	 @CrossOrigin(origins = "http://localhost:9002")
	//@CrossOrigin(orinigs="http://snapdeal.com")
	 
	@RequestMapping(value = "/getAllJobs/", method = RequestMethod.GET) // $http.get(base_url+"/getAllJobs/)
	public ResponseEntity<List<Job>> getAllOpendJobs() {
		logger.debug("Starting of the method getAllOpendJobs");
		List<Job> jobs = jobDAOImpl.getAllOpendJobs();
		return new ResponseEntity<List<Job>>(jobs, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/updateJob", method = RequestMethod.PUT)
	//@PutMapping("/updateJob")
	public ResponseEntity<Job> updateJob(@RequestBody Job job) {
		logger.debug("Starting of the method updateJob");
	
		if (jobDAOImpl.updateJob(job) == false) {
			job.setErrorCode("404");
			job.setErrorMessage("Not able to updated a job");
			logger.debug("Not able to updated a job");
		} else {
			job.setErrorCode("200");
			job.setErrorMessage("Successfully updated the job");
			logger.debug("Successfully poted the updateJob");
		}

		return new ResponseEntity<Job>(job, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getJobDetails/{jobId}", method = RequestMethod.GET)

	public ResponseEntity<Job> getJobDetails(@PathVariable("jobId") Long jobID) {
		logger.debug("Starting of the method getJobDetails of job id : " +jobID );
		Job job = jobDAOImpl.getJobDetails(jobID);

		if (job == null) {
			job = new Job();
			job.setErrorCode("404");
			job.setErrorMessage("Job not available for this id : " + jobID);
		}

		return new ResponseEntity<Job>(job, HttpStatus.OK);
	}

	@RequestMapping(value = "/postAJob", method = RequestMethod.POST)
	//@PostMapping("/postAJob")
	public ResponseEntity<Job> postAJob(@RequestBody Job job) {
		logger.debug("Starting of the method postAJob");
		job.setStatus('V'); // 1. V-Vacant 2. F-Filled 3. P-Pending 4.

		if (jobDAOImpl.save(job) == false) {
			job.setErrorCode("404");
			job.setErrorMessage("Not able to post a job");
			logger.debug("Not able to post a job");
		} else {
			job.setErrorCode("200");
			job.setErrorMessage("Successfully poted the job");
			logger.debug("Successfully poted the job");
		}

		return new ResponseEntity<Job>(job, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/applyForJob/{jobId}", method = RequestMethod.POST)
	public ResponseEntity<JobApplication> applyForJob(@PathVariable("jobId") Long jobId) {
		logger.debug("Starting of the method applyForJob");
		int loggedInUserID = (Integer) session.getAttribute("loggedInUserID");
		User user = userDAOImpl.get(loggedInUserID);
		
		if (user == null || user.isEmpty()) {
			jobApplication.setErrorCode("404");
			jobApplication.setErrorMessage("You have loggin to apply for a job");
		} else {

			if (isUserAppliedForTheJob(loggedInUserID, jobId) == false) {
				jobApplication.setJobId(jobId);
				jobApplication.setUserId(loggedInUserID);
				jobApplication.setStatus('N'); // N-Newly Applied; C->Call For
												// Interview, S->Selected
				jobApplication.setRemarks("It's a good job");
				jobApplication.setDateApplied(new Date(System.currentTimeMillis()));

				logger.debug("Applied Date : " + jobApplication.getDateApplied());

				if (jobDAOImpl.save(jobApplication)) {
					jobApplication.setErrorCode("200");
					jobApplication.setErrorMessage("You have successfully applied for the job :" + jobId +
							" Hr will getback to you soon.");
					logger.debug("Not able to apply for the job");
				}
			} else // If the user already applied for the job
			{
				jobApplication.setErrorCode("404");
				jobApplication.setErrorMessage("You already applied for the job number :" + jobId);
				logger.debug("Not able to apply for the job");
			}

		}
		return new ResponseEntity<JobApplication>(jobApplication, HttpStatus.OK);
			}
	
	private boolean isUserAppliedForTheJob(int userId, Long jobId) {
		if (jobDAOImpl.getJobApplication(userId, jobId) == null) {
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = "/getMyAppliedJobs/", method = RequestMethod.GET)
	public ResponseEntity<List<JobApplication>> getMyAppliedJobs() {
		
		logger.debug("Starting of the method getMyAppliedJobs");
		
		int loggedInUserID =(Integer) session.getAttribute("loggedInUserID");
		
		System.out.println("hi");
		User user = userDAOImpl.get(loggedInUserID);
		System.out.println("bye");
		
		List<JobApplication> jobs =new ArrayList<JobApplication>();
		
		if (user == null || user.isEmpty()) 
		{
			jobApplication.setErrorCode("404");
			jobApplication.setErrorMessage("You have to login to see your applied jobs");
			//jobs.add(job);

		} 
		else 
		{
				jobs = jobDAOImpl.getMyAppliedJobs(loggedInUserID);
				for (JobApplication jobApplication : jobs) {
					System.out.println(jobApplication.getJobId());
		}
				
		}
		return new ResponseEntity<List<JobApplication>>(jobs, HttpStatus.OK);
	}
	
}










