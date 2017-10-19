package com.niit.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name="MY_FRIEND")
@Component
public class Friend extends BaseDomain{

	@Id
	private int id;

	@Column(name="user_id")
	private int userID;

	@Column(name="friend_id")
	private int friendID;
	
	//New, accepted, rejected
	private String status;
	
	@Column(name="is_online")
	private char  isOnline;
	
	@Column(name = "LAST_SEEN_TIME")
	private Date lastSeenTime;
	
	
	public char getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(char isOnline) {
		this.isOnline = isOnline;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getFriendID() {
		return friendID;
	}
	public void setFriendID(int friendID) {
		this.friendID = friendID;
	}
	public Date getLastSeenTime() {
		return lastSeenTime;
	}
	public void setLastSeenTime(Date lastSeenTime) {
		this.lastSeenTime = lastSeenTime;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	


}
