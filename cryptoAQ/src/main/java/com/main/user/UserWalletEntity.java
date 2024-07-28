package com.main.user;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_USER_WALLET")
//@IdClass(UserWalletEntityId.class)
public class UserWalletEntity {

	@Id
	/*
	 * @SequenceGenerator(name = "wallet_sequence", sequenceName =
	 * "wallet_sequence", allocationSize = 1)
	 * 
	 * @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	 * "wallet_sequence")
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "WALLET_ID")
	private Long walletId;

	@Column(name = "USER_ID")
	private Long userid;

	@Column(name = "CURRENCY")
	private String currency;

	@Column(name = "QTY_BALANCE")
	private BigDecimal qtyBalance;

	@Column(name = "CRE_ON")
	private Timestamp creOn;

	@Column(name = "UPD_ON")
	private Timestamp updOn;

	public Long getWalletId() {
		return walletId;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}

	public UserWalletEntity() {
		super();
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getQtyBalance() {
		return qtyBalance;
	}

	public void setQtyBalance(BigDecimal qtyBalance) {
		this.qtyBalance = qtyBalance;
	}

	public Timestamp getCreOn() {
		return creOn;
	}

	public void setCreOn(Timestamp creOn) {
		this.creOn = creOn;
	}

	public Timestamp getUpdOn() {
		return updOn;
	}

	public void setUpdOn(Timestamp updOn) {
		this.updOn = updOn;
	}

	public UserWalletEntity(Long userid, String currency, BigDecimal qtyBalance, Timestamp creOn, Timestamp updOn) {
		super();
		this.userid = userid;
		this.currency = currency;
		this.qtyBalance = qtyBalance;
		this.creOn = creOn;
		this.updOn = updOn;
	}

}
