package com.puretalk.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.puretalk.db.domain.Message;


@Repository
public class MessageDAO {
	@Autowired
	SessionFactory sessionFactory;

	Integer messageID;
	
	/* Method to CREATE an message in the database  */
	public Integer addMessage(String message, String userCellPhone, String friendCellPhone, String flag) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		messageID = null;
		try {
			tx = session.beginTransaction();
			Message messageContext = new Message(message, userCellPhone, friendCellPhone, flag);
			messageID = (Integer) session.save(messageContext);
			
			Query user = session.createQuery("update ChattingList set flag=1 "
					+ "where usercellphone=:usercellphone and friendcellphone=:friendcellphone and flag=0");
			
			user.setString("usercellphone", userCellPhone);
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
		return messageID;
	}
	
	/* Method to READ all the message */
	public ArrayList<Map<String, Object>> listMessage(String userCellphone, String friendCellphone) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery(
					"from Message m "
					+ "where "
					+ "m.usercellphone=:usercellphone and m.friendcellphone=:friendcellphone "
					+ "or "
					+ "m.usercellphone=:friendcellphone and m.friendcellphone=:usercellphone");
			query.setString("usercellphone", userCellphone);
			query.setString("friendcellphone", friendCellphone);

			@SuppressWarnings("unchecked")
			List<Message> messageObject = query.list();
			System.out.println("listmessage" + messageObject.size());
			
			for (int i=0; i < messageObject.size(); i++) {
				
				Map<String, Object> messageMap = new HashMap<String, Object>();

				Message messageDomain = messageObject.get(i);
				
				int seq = messageDomain.getSeq();
				String message = messageDomain.getMessage();
				String usercellphone = messageDomain.getUsercellphone();
				String friendcellphone = messageDomain.getFriendcellphone();
				String toname = messageDomain.getToname();
				String fromname = messageDomain.getFromname();
				String flag = messageDomain.getFlag();
				
				
				messageMap.put("seq", seq);
				messageMap.put("message", message);
				messageMap.put("usercellphone", usercellphone);
				messageMap.put("friendcellphone", friendcellphone);
				messageMap.put("toname", toname);
				messageMap.put("fromname", fromname);
				messageMap.put("flag", flag);
				
				arrayList.add(messageMap);
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
	
}
