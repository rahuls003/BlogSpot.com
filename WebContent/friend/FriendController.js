'use strict';
 



app.controller('FriendController', ['UserService','$scope', 'FriendService','$location',
   '$rootScope',function(UserService,$scope, FriendService,$location,$routeParams,$rootScope) {
	console.log("FriendController...")
          var self = this;
          self.friend={id:'',userID:'',friendID:'',status:'', errorCode:'', errorMessage:''};
          self.friends=[];
          self.pendingrequest=[];
          
          self.user = {	id : '', name : '',	mobile : '',
  				address : '', isOnline:'',role : '',
  				errorCode:'', errorMessage : ''
  			};
          
  			self.users = [];
  			
  			
/*GET FRIEND REQUEST SEND BY USER*/
  		    self.getMyFriendRequests = function(){
  		              FriendService.getMyFriendRequests()
  		                      .then(
  		                             function(d)
  		                             {
  		                            	 self.pendingrequest = d;
  		                            	 
  		                            	 $location.path="/view_friend";
  		                            	 
  		                             },
  		                              function(errResponse){
  		                                   console.error('Error while updating Friend.');
  		                              } 
  		                  );
  		          };
  		     self.getMyFriendRequests();
  		 
          
/* SEND FRIEND REQUEST      */
  	  /*self.sendFriendRequest=sendFriendRequest*/
         
         function sendFriendRequest(friendID)
         {
        	 console.log("->sendFriendRequest :"+friendID)
		             FriendService.sendFriendRequest(friendID)
		                 .then(
		                              function(d) {
		                                   self.friend = d;
		                                   
		                                  alert("Friend Request : " + self.friend.errorMessage)
		                              },
		                               function(errResponse){
		                                   console.error('Error while sending friend request');
		                               }
		                      );
         }
          
             
          self.getMyFriends = function(){
        	  console.log("Getting my friends")
              FriendService.getMyFriends()
                  .then(
                               function(d) {
                                    self.friends = d;
                                    console.log("Got the friends list"+ self.friends)
                                     	/* $location.path('/view_friend');*/
                               },
                                function(errResponse){
                                    console.error('Error while fetching Friends');
                                }
                       );
          };
            
       
         self.updateFriendRequest = function(friend, id){
              FriendService.updateFriendRequest(friend, id)
                      .then(
                              self.fetchAllFriends, 
                              function(errResponse){
                                   console.error('Error while updating Friend.');
                              } 
                  );
          };
 
         self.acceptFriendRequest = function(id){
              FriendService.acceptFriendRequest(id)
                      .then(
                              self.fetchAllFriends, 
                              function(errResponse){
                                   console.error('Error while acceptFriendRequest');
                              } 
                  );
          };
          
          self.rejectFriendRequest = function(id){
              FriendService.rejectFriendRequest(id)
                      .then(
                              self.fetchAllFriends, 
                              function(errResponse){
                                   console.error('Error while rejectFriendRequest');
                              } 
                  );
          };
          
          self.unFriend = function(id){
              FriendService.unFriend(id)
                      .then(
                              self.fetchAllFriends, 
                              function(errResponse){
                                   console.error('Error while unFriend ');
                              } 
                  );
          };
          
          self.fetchAllUsers = function() {
				UserService.fetchAllUsers().then(function(d) {
					self.users = d;
				}, function(errResponse) {
					console.error('Error while fetching Users');
				});
			};
            
 
          self.fetchAllUsers();  //calling the method fetchAllUsers
          self.getMyFriends();   //calling the method getMyFriends
 
     
 
      }]);