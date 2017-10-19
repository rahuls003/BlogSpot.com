package com.niit.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.FriendDAOImpl;
import com.niit.dao.UserDAOImpl;
import com.niit.model.User;

@RestController
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserDAOImpl userDAOImpl;
	
	@Autowired
	User user;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	FriendDAOImpl friendDAOImpl;
	
	
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers() {

		logger.debug("->->->->calling method listAllUsers");
		List<User> users = userDAOImpl.list();
		if (users.isEmpty()) {
			user.setErrorCode("404");
			user.setErrorMessage("No users are available");
			users.add(user);
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/user/", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody User user) {
		logger.debug("->->->->calling method createUser");
		if (userDAOImpl.get(user.getId()) == null) {
			logger.debug("->->->->User is going to create with id:" + user.getId());
			user.setIsOnline('N');
			user.setStatus('N');
			user.setLastSeenTime(new Date(System.currentTimeMillis()));
			  if (userDAOImpl.save(user) ==true)
			  {
				  user.setErrorCode("200");
					user.setErrorMessage("Thank you  for registration. You have successfully registered as " + user.getRole());
			  }
			  else
			  {
				  user.setErrorCode("404");
					user.setErrorMessage("Could not complete the operatin please contact Admin");
		
				  
			  }
			
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		logger.debug("->->->->User already exist with id " + user.getId());
		user.setErrorCode("404");
		user.setErrorMessage("User already exist with id : " + user.getId());
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update/user/", method = RequestMethod.PUT)
	//@PutMapping("/user/")
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		logger.debug("->->->->calling method updateUser");
		if (userDAOImpl.get(user.getId()) == null) {
			logger.debug("->->->->User does not exist with id " + user.getId());
			user = new User(); // ?
			user.setErrorCode("404");
			user.setErrorMessage("User does not exist with id " + user.getId());
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}

		userDAOImpl.update(user);
		logger.debug("->->->->User updated successfully");
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}


	// http://localhost:8081/CollaborationBackEnd/user/abbas
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(@PathVariable("id") int id) {
		logger.debug("->->calling method getUser");
		logger.debug("->->id->->" + id);
		User user = userDAOImpl.get(id);
		if (user == null) {
			logger.debug("->->->-> User does not exist wiht id" + id);
			user = new User(); //To avoid NLP - NullPointerException
			user.setErrorCode("404");
			user.setErrorMessage("User does not exist");
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		logger.debug("->->->-> User exist wiht id" + id);
		logger.debug(user.getName());
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/accept/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> accept(@PathVariable("id") int id) {
		logger.debug("Starting of the method accept");

		user = updateStatus(id, 'A', "");
		logger.debug("Ending of the method accept");
		return new ResponseEntity<User>(user, HttpStatus.OK);

	}

	@RequestMapping(value = "/reject/{id}/{reason}", method = RequestMethod.GET)
	public ResponseEntity<User> reject(@PathVariable("id") int id, @PathVariable("reason") String reason) {
		logger.debug("Starting of the method reject");

		user = updateStatus(id, 'R', reason);
		logger.debug("Ending of the method reject");
		return new ResponseEntity<User>(user, HttpStatus.OK);

	}

	private User updateStatus(int id, char status, String reason) {
		logger.debug("Starting of the method updateStatus");

		logger.debug("status: " + status);
		user = userDAOImpl.get(id);

		if (user == null) {
			user = new User();
			user.setErrorCode("404");
			user.setErrorMessage("Could not update the status to " + status);
		} else {

			user.setStatus(status);
			user.setReason(reason);
			
			userDAOImpl.update(user);
			
			user.setErrorCode("200");
			user.setErrorMessage("Updated the status successfully");
		}
		logger.debug("Ending of the method updateStatus");
		return user;

	}

	@RequestMapping(value = "/myProfile", method = RequestMethod.GET)
	public ResponseEntity<User> myProfile() {
		logger.debug("->->calling method myProfile");
		int loggedInUserID = (Integer) session.getAttribute("loggedInUserID");
		User user = userDAOImpl.get(loggedInUserID);
		if (user == null) {
			logger.debug("->->->-> User does not exist wiht id" + loggedInUserID);
			user = new User(); // It does not mean that we are inserting new row
			user.setErrorCode("404");
			user.setErrorMessage("User does not exist");
			return new ResponseEntity<User>(user, HttpStatus.NOT_FOUND);
		}
		logger.debug("->->->-> User exist with id" + loggedInUserID);
		logger.debug(user.getName());
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	 
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	/*@PostMapping("/login")*/
	public ResponseEntity<User> login(@RequestBody User user) {
		logger.debug("->->->->calling method authenticate");
		user = userDAOImpl.authenticate(user.getName(), user.getPassword());
		if (user == null) {
			user = new User(); // Do wee need to create new user?
			user.setErrorCode("404");
			user.setErrorMessage("Invalid Credentials.  Please enter valid credentials");
			logger.debug("->->->->In Valid Credentials");

		} 
		else
		{
			user.setErrorCode("200");
			user.setErrorMessage("You have successfully logged in.");
			user.setIsOnline('Y');
			logger.debug("->->->->Valid Credentials");
			session.setAttribute("loggedInUser", user);
			session.setAttribute("loggedInUserID", user.getId());
			session.setAttribute("loggedInUserRole", user.getRole());
			
			System.out.println("------------------------------------------------------------");
			System.out.println("USER ID OF USER IS :" + session.getAttribute("loggedInUserID"));
			System.out.println("USER ID OF USER IS :" + session.getAttribute("loggedInUserRole"));
			System.out.println("------------------------------------------------------------");
			
			logger.debug("You are loggin with the role : " +session.getAttribute("loggedInUserRole"));

			friendDAOImpl.setOnline(user.getId());
			userDAOImpl.setOnline(user.getId());
		}

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/logout", method = RequestMethod.GET)
	public ResponseEntity<User> logout(HttpSession session) {
		logger.debug("->->->->calling method logout");
		int loggedInUserID =  (Integer) session.getAttribute("loggedInUserID");
		
		 user = userDAOImpl.get(loggedInUserID);
		 
		 user.setLastSeenTime(new Date(System.currentTimeMillis()));
		 userDAOImpl.update(user);
		 
		
		 friendDAOImpl.setOffLine(loggedInUserID);
		 userDAOImpl.setOffLine(loggedInUserID);

		session.invalidate();

		user.setErrorCode("200");
		user.setErrorMessage("You have successfully logged");
		return new ResponseEntity<User>(user, HttpStatus.OK);
	};
	
	@RequestMapping(value = "/listAllUsersNotFriends", method = RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsersNotFriends() {

		logger.debug("->->->->calling method listAllUsers");
		
		int loggedInUserID = (Integer) session.getAttribute("loggedInUserID");
		
		logger.debug("Loggined in user id is : " + loggedInUserID);
		
		
		List<User> users = userDAOImpl.notMyFriendList(loggedInUserID);

		// errorCode :200 :404
		// errorMessage :Success :Not found

		if (users.isEmpty()) {
			user.setErrorCode("404");
			user.setErrorMessage("No users are available");
			users.add(user);
		}

		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

}
