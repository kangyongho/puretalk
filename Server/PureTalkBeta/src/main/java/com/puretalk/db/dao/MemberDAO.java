package com.puretalk.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.puretalk.db.domain.ChattingList;
import com.puretalk.db.domain.FriendList;
import com.puretalk.db.domain.User;

@Repository
public class MemberDAO {
	@Autowired
	SessionFactory sessionFactory;
	
	public Boolean checkId(String loginId) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Boolean checkIdResult = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("select count(*) from User where id=:id");
			query.setString("id", loginId);
			Long count = (Long) query.uniqueResult();
			if (count > 0) {
				// 중복 Id 존재
				checkIdResult = false;
			} else {
				checkIdResult = true;
			}
			tx.commit();
		} catch(HibernateException e) {
			if (tx != null) {
				tx.rollback();
				e.printStackTrace();
			}
		} finally {session.close();}
		return checkIdResult;
	}
	
	public Integer join(String id, String password, String usercellphone) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Integer memberNumber = 0;
		try {
			tx = session.beginTransaction();
			User user = new User(id, password, usercellphone);
			memberNumber = (Integer) session.save(user);
			tx.commit();
		} catch(HibernateException e) {
			if (tx != null) {
				tx.rollback();
				e.printStackTrace();
			}
		} finally {
			session.close();
		}
		return memberNumber;
	}
	
	public ArrayList<Map<String, String>> userList() {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
		try {
			tx = session.beginTransaction();
			@SuppressWarnings("unchecked")
			List<User> userObject = session.createQuery("from User").list();
			
			for (int i=0; i < userObject.size(); i++) {
				
				Map<String, String> userListMap = new HashMap<String, String>();

				User userList = userObject.get(i);
				String name = userList.getName();
				String usercellphone = userList.getUsercellphone();
				
				userListMap.put("name", name);
				userListMap.put("usercellphone", usercellphone);
				
				arrayList.add(userListMap);
			}
			tx.commit();
		} catch(HibernateException e) {
			if (tx != null) {
				tx.rollback();
				e.printStackTrace();
			}
		} finally {session.close();}
		return arrayList;
	}
	
	public ArrayList<Map<String, Object>> getFriendList(String userCellphone) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		try {
			tx = session.beginTransaction();
			
			Query query = session.createQuery("from FriendList f where f.usercellphone=:usercellphone and f.flag=1");
			query.setString("usercellphone", userCellphone);
			@SuppressWarnings("unchecked")
			List<FriendList> friendObject = query.list();
			
			
			for (int i=0; i < friendObject.size(); i++) {
				
				Map<String, Object> friendListMap = new HashMap<String, Object>();

				FriendList friendList = friendObject.get(i);
				
				int seq = friendList.getSeq();
				String usercellphone = friendList.getUsercellphone();
				String friendname = friendList.getFriendname();
				String friendcellphone = friendList.getFriendcellphone();
				String gcmid = friendList.getGcmid();
				
				friendListMap.put("seq", seq);
				friendListMap.put("usercellphone", usercellphone);
				friendListMap.put("friendname", friendname);
				friendListMap.put("friendcellphone", friendcellphone);
				friendListMap.put("gcmid", gcmid);
				
				arrayList.add(friendListMap);
			}
			tx.commit();
		} catch(HibernateException e) {
			if (tx != null) {
				tx.rollback();
				e.printStackTrace();
			}
		} finally {session.close();}
		return arrayList;
	}
	
	public ArrayList<Map<String, Object>> getChattingList(String userCellphone) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		try {
			tx = session.beginTransaction();
			
			Query query = session.createQuery("from ChattingList c where c.usercellphone=:usercellphone and c.flag=1");
			query.setString("usercellphone", userCellphone);
			@SuppressWarnings("unchecked")
			List<ChattingList> chattingObject = query.list();
			
			for (int i=0; i < chattingObject.size(); i++) {
				
				Map<String, Object> chattingListMap = new HashMap<String, Object>();

				ChattingList chattingList = chattingObject.get(i);
				
				int seq = chattingList.getSeq();
				String usercellphone = chattingList.getUsercellphone();
				String friendname = chattingList.getFriendname();
				String friendcellphone = chattingList.getFriendcellphone();
				String gcmid = chattingList.getGcmid();
				
				chattingListMap.put("seq", seq);
				chattingListMap.put("usercellphone", usercellphone);
				chattingListMap.put("friendname", friendname);
				chattingListMap.put("friendcellphone", friendcellphone);
				chattingListMap.put("gcmid", gcmid);
				
				arrayList.add(chattingListMap);
			}
			tx.commit();
		} catch(HibernateException e) {
			if (tx != null) {
				tx.rollback();
				e.printStackTrace();
			}
		} finally {session.close();}
		return arrayList;
	}
	
	
	public void updateFriendList(String userCellphone, String friendCellPhone) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			Query user = session.createQuery("update FriendList set flag=2 "
					+ "where usercellphone=:usercellphone and friendcellphone=:friendcellphone");
			user.setString("usercellphone", userCellphone);
			user.setString("friendcellphone", friendCellPhone);
			user.executeUpdate();
			
			tx.commit();
		} catch(HibernateException e) {
			if (tx != null) {
				tx.rollback();
				e.printStackTrace();
			}
		} finally {
			session.close();
		}
	}
	
	public void updateChattingList(String userCellphone, String friendCellPhone) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			Query user = session.createQuery("update ChattingList set flag=3 "
					+ "where usercellphone=:usercellphone and friendcellphone=:friendcellphone");
			user.setString("usercellphone", userCellphone);
			user.setString("friendcellphone", friendCellPhone);
			user.executeUpdate();
			
			tx.commit();
		} catch(HibernateException e) {
			if (tx != null) {
				tx.rollback();
				e.printStackTrace();
			}
		} finally {
			session.close();
		}
	}
	
	public void setFirendList(String userCellphone, String friendName, String friendCellPhone, int flag) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			FriendList friendList = new FriendList(userCellphone, friendName, friendCellPhone, flag);
			session.save(friendList);
			tx.commit();
		} catch(HibernateException e) {
			if (tx != null) {
				tx.rollback();
				e.printStackTrace();
			}
		} finally {
			session.close();
		}
	}
	
	public void setChattingList(String userCellphone, String friendName, String friendCellPhone, int flag) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ChattingList chattingList = new ChattingList(userCellphone, friendName, friendCellPhone, flag);
			session.save(chattingList);
			tx.commit();
		} catch(HibernateException e) {
			if (tx != null) {
				tx.rollback();
				e.printStackTrace();
			}
		} finally {
			session.close();
		}
	}
	
	public Boolean loginResult(String loginId, String password) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Boolean loginResult = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("select count(*) from User where id=:id and password=:password");
			query.setString("id", loginId);
			query.setString("password", password);
			Long count = (Long) query.uniqueResult();
			if (1 == count) {
				// 중복 Id 존재
				loginResult = true;
			} else {
				loginResult = false;
			}
			tx.commit();
		} catch(HibernateException e) {
			if (tx != null) {
				tx.rollback();
				e.printStackTrace();
			}
		}
		return loginResult;
	}
	
	/* gcmid 업데이트 */ 
	public void updateGCM(String userCellPhone, String gcmId) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			Query user = session.createQuery("update User set gcmid=:gcmid where usercellphone=:usercellphone");
			user.setString("gcmid", gcmId);
			user.setString("usercellphone", userCellPhone);
			user.executeUpdate();
			
			Query friend = session.createQuery("update FriendList set gcmid=:gcmid where friendcellphone=:friendcellphone and flag=1");
			friend.setString("gcmid", gcmId);
			friend.setString("friendcellphone", userCellPhone);
			friend.executeUpdate();
			
			Query chatting = session.createQuery("update ChattingList set gcmid=:gcmid where friendcellphone=:friendcellphone and flag=1");
			chatting.setString("gcmid", gcmId);
			chatting.setString("friendcellphone", userCellPhone);
			chatting.executeUpdate();
			
			tx.commit();
		} catch(HibernateException e) {
			if (tx != null) {
				tx.rollback();
				e.printStackTrace();
			}
		} finally {
			session.close();
		}
	}
	
	// 메시지 보낼 때를 위해 chattinglist 조회시 gcmid도 함께 보내주기 
	public ArrayList<Map<String, Object>> getGcmId(String userCellPhone) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("from User where usercellphone=:usercellphone");
			query.setString("usercellphone", userCellPhone);
			String gcmid = (String) query.uniqueResult();
			
			Map<String, Object> gcmMap = new HashMap<String, Object>();
			gcmMap.put("gcmid", gcmid);
			
			arrayList.add(gcmMap);
			
			tx.commit();
		} catch(HibernateException e) {
			if (tx != null) {
				tx.rollback();
				e.printStackTrace();
			}
		} finally {session.close();}
		return arrayList;
	}
}












