package com.puretalk.db.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {
	// 정확한 JSON 값이 들어와야 한다. (json key = 필드명 일치해야한다)
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="seq")
	// save후 리턴타입 결정부분
	private int seq;
	
	@Column(name="id")
	private String id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="usercellphone")
	private String usercellphone;
	
	@Column(name="password")
	private String password;
	
	@Column(name="gcmid")
	private String gcmId;
	
	public User() {}
	public User(String id, String password, String usercellPhone) {
		super();
		this.id = id;
		this.password = password;
		this.usercellphone = usercellPhone;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsercellphone() {
		return usercellphone;
	}
	public void setUsercellphone(String usercellphone) {
		this.usercellphone = usercellphone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGcmId() {
		return gcmId;
	}
	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}

}
