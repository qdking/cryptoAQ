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
@Table(name = "TB_USER_TRANSACTION")
public class UserTransactionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRANSACTION_ID")
	private Long transactionId;

	@Column(name = "USER_ID")
	private Long userid;

	@Column(name = "CURRENCY_BOUGHT")
	private String currencyBought;

	@Column(name = "CURRENCY_SOLD")
	private String currencySold;

	@Column(name = "QTY_BOUGHT")
	private BigDecimal qtyBought;

	@Column(name = "QTY_SOLD")
	private BigDecimal qtySold;

	@Column(name = "PRICE_BOUGHT")
	private BigDecimal priceBought;

	@Column(name = "PRICE_SOLD")
	private BigDecimal priceSold;

	@Column(name = "CRE_ON")
	private Timestamp creOn;

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getCurrencyBought() {
		return currencyBought;
	}

	public void setCurrencyBought(String currencyBought) {
		this.currencyBought = currencyBought;
	}

	public String getCurrencySold() {
		return currencySold;
	}

	public void setCurrencySold(String currencySold) {
		this.currencySold = currencySold;
	}

	public BigDecimal getQtyBought() {
		return qtyBought;
	}

	public void setQtyBought(BigDecimal qtyBought) {
		this.qtyBought = qtyBought;
	}

	public BigDecimal getQtySold() {
		return qtySold;
	}

	public void setQtySold(BigDecimal qtySold) {
		this.qtySold = qtySold;
	}

	public BigDecimal getPriceBought() {
		return priceBought;
	}

	public void setPriceBought(BigDecimal priceBought) {
		this.priceBought = priceBought;
	}

	public BigDecimal getPriceSold() {
		return priceSold;
	}

	public void setPriceSold(BigDecimal priceSold) {
		this.priceSold = priceSold;
	}

	public Timestamp getCreOn() {
		return creOn;
	}

	public void setCreOn(Timestamp creOn) {
		this.creOn = creOn;
	}

	public UserTransactionEntity(Long userid) {
		super();
		this.userid = userid;
	}

	public UserTransactionEntity() {
		super();
	}

}