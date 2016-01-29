package com.puretalk.db.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="friendlist")
public class FriendList {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="seq")
	private int seq;
	
	@Column(name="usercellphone")
	private String usercellphone;
	
	@Column(name="friendname")
	private String friendname;
	
	@Column(name="friendcellphone")
	private String friendcellphone;
	
	@Column(name="flag")
	private int flag;
	
	@Column(name="gcmid")
	private String gcmid;
	
	public FriendList() {}

	public FriendList(String usercellphone, String friendname, String friendcellphone, int flag) {
		super();
		this.usercellphone = usercellphone;
		this.friendname = friendname;
		this.friendcellphone = friendcellphone;
		this.flag = flag;
	}

	public FriendList(String usercellphone, String friendname, String friendcellphone, int flag, String gcmid) {
		super();
		this.usercellphone = usercellphone;
		this.friendname = friendname;
		this.friendcellphone = friendcellphone;
		this.flag = flag;
		this.gcmid = gcmid;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getUsercellphone() {
		return usercellphone;
	}

	public void setUsercellphone(String usercellphone) {
		this.usercellphone = usercellphone;
	}

	public String getFriendname() {
		return friendname;
	}

	public void setFriendname(String friendname) {
		this.friendname = friendname;
	}

	public String getFriendcellphone() {
		return friendcellphone;
	}

	public void setFriendcellphone(String friendcellphone) {
		this.friendcellphone = friendcellphone;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getGcmid() {
		return gcmid;
	}

	public void setGcmid(String gcmid) {
		this.gcmid = gcmid;
	}
	

}
