package com.main.user;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_USER_MAIN")
public class UserEntity {

	@Id

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Long userid;
	private String username;
	private String hashedPassword;
	private Timestamp creOn;

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UserEntity() {
		super();
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public Timestamp getCreOn() {
		return creOn;
	}

	public void setCreOn(Timestamp creOn) {
		this.creOn = creOn;
	}

	public UserEntity(Long userid, String username, String hashedPassword, Timestamp creOn) {
		super();
		this.userid = userid;
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.creOn = creOn;
	}

}
