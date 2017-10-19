'use strict';

app	.controller('JobController',['JobService','$location','$scope' , '$rootScope',
						function(JobService,  $location, $rootScope,$scope ) {
							console.log("JobController...")
							var self = this;

							this.job = {
								id : '',
								title : '',
								description : '',
								dateTime : '',
								qualification : '',
								status : '',
								errorCode : '',
								errorMessage : ''
							};
							this.jobs = [];

							this.alljobs=[];
							
							this.jobApplications=[];
							
							
							this.applyForJob = applyForJob

							function applyForJob(jobId) {
								alert("applyForJob");
								console.log("applyForJob");
								var currentUser = $rootScope.currentUser
								console.log("currentUser.id:" + currentUser.id)
								//if(currentUser) -> not null, not empty and defined
								
								if (typeof currentUser.id == 'undefined') 
									{
									   alert("Please login to apply for the job")
	                                     console.log("User not logged in.  Can not apply for job")
	                                     /*$location
											.path('/login');*/
									   return
									
									}
								console.log("->userID :" + currentUser.id
										+ "  applying for job:" + jobId)
										
										
								JobService
										.applyForJob(jobId)
										.then(
												function(data) {
													self.job = data;
													alert(self.job.errorMessage)
												},
												function(errResponse) {
													console
															.error('Error while applying for job request');
												});

							}
							
							

							self.getMyAppliedJobs = function() {
								console.log('calling the method getMyAppliedJobs');
								
								JobService.getMyAppliedJobs()
								.then(
									function(d) {
								    	self.jobApplications = d;
								    	//alert(d);
								    	console.log("Fetch all applied jobs sucessfully")
								    	/*$location.path("/view_applied_jobs")*/
								    }, 
								    
								    function(errResponse) {
									console.error('Error while fetching Jobs');
								});
							};
							
							self.getMyAppliedJobs(); 

							self.rejectJobApplication = function(userID) {
						    var jobID =$rootScope.selectedJob.id;
								JobService.rejectJobApplication(userID,jobID												)
										.then(
												function(d) {
													self.job = d;
													alert("You have successfully rejected the job application of the " +
															"user : " +userID)
												},
												function(errResponse) {
													console
															.error('Error while rejecting Job application.');
												});
							};

							self.callForInterview = function(userID) {
								var jobID =$rootScope.selectedJob.id;	
								JobService
										.callForInterview(userID,
												jobID)
										.then(
												function(d) {
													self.job = d;
													alert("Application status changed as call for interview")
												},
												function(errResponse) {
													console
															.error('Error while changing the status "call for interview" ');
												});
							};
							self.selectUser = function(userID) {
								var jobID =$rootScope.selectedJob.id;		
								JobService.selectUser(userID, jobID)
										.then(
												function(d) {
													self.job = d;
													alert("Application status sta as selected")
												},
												function(errResponse) {
													console
															.error('Error while changing the status "select user" ');
												});
							};

							//definition of getAllJobs function
							self.getAllJobs = function() {
								console.log('calling the method getAllJobs');
								JobService.getAllJobs()
										.then(
												function(d) {
													self.alljobs = d;
													console.log("Fetch all jobs sucessfully")
												},
												function(errResponse) {
													console.error('Error while fetching All opend jobs');
												});
							};

							self.getAllJobs(); // calling getAllJobs function

							self.submit = function() {
								{
									console.log('submit a new job', self.job);
									self.postAJob(self.job);
								}
								self.reset();
							};

							self.postAJob = function(job) {
								console.log('submit a new job', self.job);
								var currentUser = $rootScope.currentUser
								
								if (typeof currentUser.id == 'undefined') 
								{
								   alert("Please login to post for job")
                                     console.log("User not logged in.  Can not apply for job")
                                     /*$location
										.path('/login');*/
								   return
								
								}
								
								
								JobService.postAJob(job).then(function(d) {
								alert("You successfully posted the job")
								}, function(errResponse) {
									console.error('Error while posting job.');
								});
							};

							self.getJobDetails = getJobDetails

							function getJobDetails(jobID) {
								console.log('get Job details of the id', jobID);
								JobService
										.getJobDetails(jobID)
										.then(
												function(d) {
													self.job = d;
													
													$location
															.path('/view_job_details');
												},
												function(errResponse) {
													console
															.error('Error while fetching blog details');
												});
							}
							;

							self.reset = function() {
								console.log('resetting the Job');
								self.job = {
									id : '',
									title : '',
									description : '',
									dateTime : '',
									qualification : '',
									status : '',
									errorCode : '',
									errorMessage : ''
								};
								$scope.myForm.$setPristine(); // reset Form
							};

						} ]);