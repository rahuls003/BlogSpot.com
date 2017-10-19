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

import com.niit.model.Friend;

@Repository("friendDAOImpl")
public class FriendDAOImpl {

	private static final Logger log = LoggerFactory.getLogger(FriendDAOImpl.class);

	@Autowired(required = true)
	private SessionFactory sessionFactory;

	public FriendDAOImpl(SessionFactory sessionFactory) {
		try {
			this.sessionFactory = sessionFactory;
		} catch (Exception e) {
			log.error(" Unable to connect to db");
			e.printStackTrace();
		}
	}

	private Integer getMaxId() {
		log.debug("->->Starting of the method getMaxId");

		String hql = "select max(id) from Friend";
		Query query = sessionFactory.openSession().createQuery(hql);
		Integer maxID;
		try {
			maxID = (Integer) query.uniqueResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 100;
		}
		log.debug("Max id :" + maxID);
		return maxID;

	}

	@Transactional
	public boolean save(Friend friend) {

		try {
			log.debug("*********************88Previous id " + getMaxId());
			/*friend.setId(100);*/
			friend.setId(getMaxId() + 1);
			log.debug("***********************generated id:" + getMaxId());
			sessionFactory.getCurrentSession().save(friend);
			return true;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	@Transactional
	public boolean update(Friend friend) {

		try {
			log.debug("Starting of the method update");
			log.debug("user ID : " + friend.getUserID() + " Friend id :" + friend.getFriendID());
			sessionFactory.getCurrentSession().update(friend);
			log.debug("Successfully updated");
			return true;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug("Not able to update the status");
			return false;
		}

	}

	@Transactional
	public void delete(int userID, int friendID) {
		Friend friend = new Friend();
		friend.setFriendID(friendID);
		friend.setUserID(userID);
		sessionFactory.openSession().delete(friend);
	}
	
	/**
	 * get the friends who are already accepcted
	 */

	public List<Friend> getMyFriends(int loggedInUserID) {
		/*
		 * select user_id from c_friend where user_id='Srinivas' and status ='A'
		 * UNION select friend_id from c_friend where user_id='Srinivas' and
		 * status ='A' MINUS select user_id from c_friend where
		 * user_id='Srinivas';
		 */
		String hql1 = "select friendID  from Friend where userID='" + loggedInUserID + "' and status = 'A' ";

				/*+ " union  " +*/

		String hql2= "select userID from Friend where friendID='" + loggedInUserID + "' and status = 'A'";

		log.debug("getMyFriends hql1 : " + hql1);
		log.debug("getMyFriends hql2 : " + hql2);
	
		List<Friend> list1 = sessionFactory.openSession().createQuery(hql1).list();
		List<Friend> list2 = sessionFactory.openSession().createQuery(hql2).list();
		
		
		
		list1.addAll(list2);

		return list1;

	}

	public List<Friend> getNewFriendRequests(int loggedInUserID) {
		String hql = "select userID from Friend where friendID=" + "'" + loggedInUserID + "' and status ='" + "N'";

		/*log.debug(hql);*/
		
		 return  sessionFactory.openSession().createQuery(hql).list();

	}
	
	
	public List<Friend> getRequestsSendByMe(int loggedInUserID) {
		String hql = "select friendID from Friend where userID=" + "'" + loggedInUserID + "' and status ='" + "N'";

		log.debug(hql);
		return  sessionFactory.openSession().createQuery(hql).list();

	}


public Friend get(int loggedInUserID, int friendID) {
		String hql = "from Friend where userID=" + "'" + loggedInUserID + "' and friendID= '" + friendID + "'";

		log.debug("hql: " + hql);
		Query query = sessionFactory.openSession().createQuery(hql);

		return (Friend) query.uniqueResult();

	}
/*	public Friend get(String userID)
	{
		String hql = "from Friend where userID=" + "'" + userID + "' or friendID= '" + userID + "'";

		log.debug("hql: " + hql);
		Query query = sessionFactory.openSession().createQuery(hql);

		return (Friend) query.uniqueResult();

	}
*/
	// We can use update method also.
	// isOnline need to set in controller and the ncal update

	/**
	 * Instead of writing the below two methods, you can use update method
	 */

	@Transactional
	public void setOnline(int i) {
		log.debug("Starting of the metnod setOnline");
		//String hql = " UPDATE Friend	SET isOnline = 'Y' where friendID='" + friendID + "'";
		
		String hql = " UPDATE Friend	SET isOnline = 'Y' where friendID= ?";
		
		
		
		
		log.debug("hql: " + hql);
		Query query = sessionFactory.openSession().createQuery(hql);
		query.setLong(0, i);
		
		query.executeUpdate();
		log.debug("Ending of the metnod setOnline");

	}

	@Transactional
	public void setOffLine(int loggedInUserID) {
		log.debug("Starting of the metnod setOffLine");
		String hql = " UPDATE Friend	SET isOnline = 'N' where friendID='" + loggedInUserID + "'";
		log.debug("hql: " + hql);
		Query query = sessionFactory.openSession().createQuery(hql);
		query.executeUpdate();
		log.debug("Ending of the metnod setOffLine");

	}

}
