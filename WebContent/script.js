

var app=angular.module('RoutingApp', ['ngRoute','ngCookies'
                                ]);

app.config( ['$routeProvider', function($routeProvider) {
  
	$routeProvider
	
	.when('/getfriendrequest', {
	    templateUrl: 'friend/view_friend.html',
	    controller : 'FriendController'
	   })
	
	
	.when('/search_friend', {
	    templateUrl: 'friend/search_friend.html',
	    controller : 'FriendController'
	   })
	
  
   .when('/postjob', {
    templateUrl: 'Job/post_job.html',
    controller : 'JobController'
   })
   
   .when('/listjob', {
    templateUrl: 'Job/search_job.html',
    controller : 'JobController'
   })
   
   .when('/view_job_details', {
    templateUrl: 'Job/view_job_details.html',
    controller : 'JobController'
   })
   
   .when('/view_applied_jobs', {
    templateUrl: 'Job/view_applied_jobs.html',
    controller : 'JobController'
   })
   
   .when('/all_users', {
    templateUrl: 'Login/All_Users.html',
    controller : 'UserController'
   })
   
   .when('/login', {
    templateUrl: 'Login/login.html',
    controller : 'UserController'
   })
   
   .when('/register', {
    templateUrl: 'Login/register.html',
    controller : 'UserController'
   })
   
   .when('/myInformation', {
    templateUrl: 'Login/my_information.html',
    controller : 'UserController'
   })
   
   .when('/editUser', {
    templateUrl: 'Login/my_profile.html',
    controller : 'UserController'
   })
   
   .otherwise({
    redirectTo: '/'
   });
 }]);

app.run( function ($rootScope, $location,$cookieStore, $http) {
	
	$rootScope.$on('$locationChangeStart', function (event, next, current) {
		 console.log("$locationChangeStart")
		 //http://localhost:8080/Collaboration/addjob
	        // redirect to login page if not logged in and trying to access a restricted page
	     
		 var userPages = ['/myProfile','/getfriendrequest']
		 var adminPages = ["/post_job","/all_users"]
		 
		 //http://localhost:8080/Collaboration/myProfile
		 //$location.path()  - will give  /myProfile
		 var currentPage = $location.path()
		 
		 //will return 0 if current page is there in list
		 //else return -1
		 var isUserPage = $.inArray(currentPage, userPages)
		 var isAdminPage = $.inArray(currentPage, adminPages)
		 
		 var isLoggedIn = $rootScope.currentUser.id;
	        
	     console.log("isLoggedIn:" +isLoggedIn)
	     console.log("isUserPage:" +isUserPage)
	     console.log("isAdminPage:" +isAdminPage)
	        
	        if(!isLoggedIn)
	        	{
	        	
	        	 if (isUserPage===0 || isAdminPage ===0) {
		        	  console.log("Navigating to login page:")
		        	  alert("You need to loggin to do this operation")

						            $location.path('/');
		                }
	        	}
	        
			 else //logged in
	        	{
	        	
				 var role = $rootScope.currentUser.role;
				 
				 if(isAdminPage===0 && role!='admin' )
					 {
					 
					  alert("You can not do this operation as you are logged as : " + role )
					   $location.path('/');
					 
					 }
				     
	        	
	        	}
	        
	 }
	       );
	
	
// keep user logged in after page refresh
$rootScope.currentUser = $cookieStore.get('currentUser') || {};
if ($rootScope.currentUser) 
{
    $http.defaults.headers.common['Authorization'] = 'Basic' + $rootScope.currentUser; 
}

});



/*var app=angular.module('myApp',['ngRoute']);


app.config(function($routeProvider)
{
		  $routeProvider	
	      
	      .when('/',{
	    	  templateUrl:'index.html'
	      })
	      
	      .when('/Login',{
	    	  templateUrl:'Login.html'
	      })
	      
	      .when('/Register',{
	    	  templateUrl:'Register.html'
	      })
		
	      .otherwise({redirectTo: '/'});
});*/