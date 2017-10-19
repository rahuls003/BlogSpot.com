package com.niit.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.FriendDAOImpl;
import com.niit.dao.UserDAOImpl;
import com.niit.model.Friend;
import com.niit.model.User;

@RestController
public class FriendController {

	private static final Logger logger = LoggerFactory.getLogger(FriendController.class);

	@Autowired
	FriendDAOImpl friendDAOImpl;

	@Autowired
	User user;
	
	@Autowired
	Friend friend;
	
	@Autowired
	HttpSession httpSession;
	
	@Autowired
	UserDAOImpl userDAOImpl;

	@RequestMapping(value = "/myFriends", method = RequestMethod.GET)
	public ResponseEntity<List<Friend>> getMyFriends() {
		logger.debug("->->->->calling method getMyFriends");
		int loggedInUserID =  (Integer) httpSession.getAttribute("loggedInUserID");
		 user = userDAOImpl.get(loggedInUserID);
		 
		List<Friend> myFriends = new ArrayList<Friend>();
		if(user == null)
		{
			friend.setErrorCode("404");
			friend.setErrorMessage("Please login to know your friends");
			myFriends.add(friend);
			return new ResponseEntity<List<Friend>>(myFriends, HttpStatus.OK);
			
		}
       
		logger.debug("getting friends of : " + loggedInUserID);
		 myFriends = friendDAOImpl.getMyFriends(loggedInUserID);

		if (myFriends.isEmpty()) {
			logger.debug("Friends does not exsit for the user : " + loggedInUserID);
			friend.setErrorCode("404");
			friend.setErrorMessage("You does not have any friends");
			myFriends.add(friend);
		}
		logger.debug("Send the friend list ");
		return new ResponseEntity<List<Friend>>(myFriends, HttpStatus.OK);
	}

	@RequestMapping(value = "/addFriend/{friendID}", method = RequestMethod.GET)
	public ResponseEntity<Friend> sendFriendRequest(@PathVariable("friendID") int friendID) {
		logger.debug("->->->->calling method sendFriendRequest");
		int loggedInUserID = (Integer) httpSession.getAttribute("loggedInUserID");
		logger.debug(loggedInUserID + " is sending request to " + friendID);
		friend.setUserID(loggedInUserID);
		friend.setFriendID(friendID);
		friend.setStatus("N"); // N - New, R->Rejected, A->Accepted
		friend.setIsOnline('N');
		// Is the user already sent the request previous?
		
		//check whether the friend exist in user table or not
		if(isUserExist(friendID)==false)
		{
			friend.setErrorCode("404");
			friend.setErrorMessage("No user exist with the id :" + friendID);
			return new ResponseEntity<Friend>(friend, HttpStatus.OK);
		}
		
	
		if (isRequestAlreadySent(friendID)) {
			friend.setErrorCode("404");
			friend.setErrorMessage("You already sent the friend request to " + friendID);

		} else {
			friendDAOImpl.save(friend);

			friend.setErrorCode("200");
			friend.setErrorMessage("Friend request successfull.." + friendID);
		}

		return new ResponseEntity<Friend>(friend, HttpStatus.OK);

	}
	
	
	
	private boolean isUserExist(int id)
	{
		if(userDAOImpl.get(id)==null)
			return false;
		else
			return true;
	}
	
	
	private boolean isRequestAlreadySent(int friendID)
	{
		int loggedInUserID = (Integer) httpSession.getAttribute("loggedInUserID");
		
		if(friendDAOImpl.get(loggedInUserID,friendID)==null)
			return false;
		else
			return true;
	}

	@RequestMapping(value = "/unFriend/{friendID}", method = RequestMethod.PUT)
	public ResponseEntity<Friend> unFriend(@PathVariable("friendID") int friendID) {
		logger.debug("->->->->calling method unFriend");
		friend =updateRequest(friendID, "U");
		return new ResponseEntity<Friend>(friend, HttpStatus.OK);

	}

	@RequestMapping(value = "/rejectFriend/{friendID}", method = RequestMethod.PUT)
	public ResponseEntity<Friend> rejectFriendFriendRequest(@PathVariable("friendID") int friendID) {
		logger.debug("->->->->calling method rejectFriendFriendRequest");

		friend =updateRequest(friendID, "R");
		return new ResponseEntity<Friend>(friend, HttpStatus.OK);

	}

	@RequestMapping(value = "/accepttFriend/{friendID}", method = RequestMethod.PUT)
	public ResponseEntity<Friend> acceptFriendFriendRequest(@PathVariable("friendID") int friendID) {
		logger.debug("->->->->calling method acceptFriendFriendRequest");
        
		friend = updateRequest(friendID, "A");
		return new ResponseEntity<Friend>(friend, HttpStatus.OK);

	}

	private Friend updateRequest(int friendID, String status) {
		logger.debug("Starting of the method updateRequest");
		int loggedInUserID = (Integer) httpSession.getAttribute("loggedInUserID");
		logger.debug("loggedInUserID : " + loggedInUserID);
		
		if(isRequestAlreadySent(friendID)==false)
		{
			friend.setErrorCode("404");
			friend.setErrorMessage("The request does not exist.  So you can not update to "+status);
		}
		
		if (status.equals("A") || status.equals("R"))
			friend = friendDAOImpl.get(loggedInUserID, friendID);
		else
			friend = friendDAOImpl.get(loggedInUserID, friendID);
		friend.setStatus(status); // N - New, R->Rejected, A->Accepted

		friendDAOImpl.update(friend);

		friend.setErrorCode("200");
		friend.setErrorMessage(
				"Request from   " + friend.getUserID() + " To " + friend.getFriendID() + " has updated to :" + status);
		logger.debug("Ending of the method updateRequest");
		return friend;

	}

	@RequestMapping(value = "/getMyFriendRequests/", method = RequestMethod.GET)
	public ResponseEntity<List<Friend>> getMyFriendRequests() {
		logger.debug("->->->->calling method getMyFriendRequests");
		int loggedInUserID = (Integer) httpSession.getAttribute("loggedInUserID");
		
		System.out.println(loggedInUserID);
		
		List<Friend> myFriendRequests = friendDAOImpl.getRequestsSendByMe(loggedInUserID);
		
		if(myFriendRequests.isEmpty())
		{
			friend.setErrorCode("404");
			friend.setErrorMessage("You did not received any new requests");
			myFriendRequests.add(friend);
			return new ResponseEntity<List<Friend>>(myFriendRequests, HttpStatus.OK);
			
		}
		return new ResponseEntity<List<Friend>>(myFriendRequests, HttpStatus.OK);
	}
	
	
	@RequestMapping("/getRequestsSendByMe")
	public ResponseEntity<List<Friend>>  getRequestsSendByMe()
	{
		logger.debug("->->->->calling method getRequestsSendByMe");
		
		int loggedInUserID = (Integer) httpSession.getAttribute("loggedInUserID");
		List<Friend> requestSendByMe = friendDAOImpl.getRequestsSendByMe(loggedInUserID);
		
		logger.debug("No. of request send by you : " + requestSendByMe.size());
		if(requestSendByMe.isEmpty() || requestSendByMe.size()==0)
		{
			friend.setErrorCode("404");
			friend.setErrorMessage("You did not send request to any body");
			requestSendByMe.add(friend);
			return new ResponseEntity<List<Friend>>(requestSendByMe, HttpStatus.OK);
			
		}
		logger.debug("->->->->Ending method getRequestsSendByMe");
		
		return new ResponseEntity<List<Friend>>(requestSendByMe, HttpStatus.OK);
		
	}
		

}
