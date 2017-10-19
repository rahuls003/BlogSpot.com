'use strict';
 
app.factory('FriendService', ['$http', '$q','$rootScope', function($http, $q,$rootScope){
	
	console.log("FriendService...")
	
	var BASE_URL='http://localhost:9002/ZChatBackEnd'
    
	return {
         
		getMyFriends: function() {
                    return $http.get(BASE_URL+'/myFriends')
                            .then(
                                    function(response){
                                    	if(response.data.errorCode==='404')
                                    		{
                                    		  alert(response.data.errorMessage)
                                    		}
                                        return response.data;
                                    }, 
                                   null
                            );
            },
             
            sendFriendRequest: function(friendID){
                    return $http.get(BASE_URL+'/addFriend/'+friendID)
                            .then(
                                    function(response){
                                    	if(response.data.errorCode==404)
                                    	{
                                    		alert(response.data.errorMessage)
                                    	}
                                        return response.data;
                                    }, 
                                    function(errResponse){
                                        console.error('Error while creating friend');
                                        return $q.reject(errResponse);
                                    }
                            );
            },
            
            getMyFriendRequests: function(){
                return $http.get(BASE_URL+'/getMyFriendRequests/')
                        .then(
                                function(response){
                                    return response.data;
                                }, 
                                function(errResponse){
                                    console.error('Error while creating friend');
                                    return $q.reject(errResponse);
                                }
                        );
        },
        
        acceptFriendRequest: function(friendID){
        	console.log("Starting of the method acceptFriendRequest")
            return $http.get(BASE_URL+'/accepttFriend/'+friendID)
                    .then(
                            function(response){
                                return response.data;
                            }, 
                            function(errResponse){
                                console.error('Error while creating acceptFriendRequest');
                                return $q.reject(errResponse);
                            }
                    );
    },
         
    rejectFriendRequest: function(friendID){
    	console.log("Starting of the method rejectFriendRequest")
        return $http.get(BASE_URL+'/rejectFriend/'+friendID)
                .then(
                        function(response){
                            return response.data;
                        }, 
                        function(errResponse){
                            console.error('Error while rejectFriendRequest');
                            return $q.reject(errResponse);
                        }
                );
},
     
unFriend: function(friendID){
	console.log("Starting of the method unFriend")
    return $http.get(BASE_URL+'/unFriend/'+friendID)
            .then(
                    function(response){
                        return response.data;
                    }, 
                    function(errResponse){
                        console.error('Error while unFriend ');
                        return $q.reject(errResponse);
                    }
            );
},
 
             
        //Not required because we are not deleting the record
            deleteFriend: function(id){
                    return $http.delete(BASE_URL+'/friend/'+id)
                            .then(
                                    function(response){
                                        return response.data;
                                    }, 
                                    function(errResponse){
                                        console.error('Error while deleting friend');
                                        return $q.reject(errResponse);
                                    }
                            );
            }
            
           
         
    };
 
}]);