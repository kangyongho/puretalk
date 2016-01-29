package com.puretalk.db.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="message")
public class Message {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="seq")
	private int seq;
	
	@Column(name="message")
	private String message;
	
	@Column(name="usercellphone")
	private String usercellphone;
	
	@Column(name="friendcellphone")
	private String friendcellphone;
	
	@Column(name="toname")
	private String toname;
	
	@Column(name="fromname")
	private String fromname;
	
	@Column(name="flag")
	private String flag;
	
	public Message() {}

	public Message(String message, String usercellphone, String friendcellphone, String flag) {
		super();
		this.message = message;
		this.usercellphone = usercellphone;
		this.friendcellphone = friendcellphone;
		this.flag = flag;
	}

	public Message(String message, String usercellphone, String friendcellphone, String toname, String fromname,
			String flag) {
		super();
		this.message = message;
		this.usercellphone = usercellphone;
		this.friendcellphone = friendcellphone;
		this.toname = toname;
		this.fromname = fromname;
		this.flag = flag;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUsercellphone() {
		return usercellphone;
	}

	public void setUsercellphone(String usercellphone) {
		this.usercellphone = usercellphone;
	}

	public String getFriendcellphone() {
		return friendcellphone;
	}

	public void setFriendcellphone(String friendcellphone) {
		this.friendcellphone = friendcellphone;
	}

	public String getToname() {
		return toname;
	}

	public void setToname(String toname) {
		this.toname = toname;
	}

	public String getFromname() {
		return fromname;
	}

	public void setFromname(String fromname) {
		this.fromname = fromname;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
