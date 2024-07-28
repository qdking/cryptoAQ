package com.main.cryptocurrency;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_CRYPTO_BEST_PRICE_SNAPSHOT")
public class BestCyptoPriceSnapshotEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "CURRENCY_PAIR", nullable = false)
	private String currencyPair;

	@Column(name = "BID_PRICE", nullable = false)
	private BigDecimal bidPrice;

	@Column(name = "ASK_PRICE", nullable = false)
	private BigDecimal askPrice;

	@Column(name = "ASK_SOURCE", nullable = false)
	private String askPriceSource;

	@Column(name = "BID_SOURCE", nullable = false)
	private String bidPriceSource;

	@Column(name = "CRE_ON", nullable = false)
	private Timestamp creOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCurrencyPair() {
		return currencyPair;
	}

	public void setCurrencyPair(String currency) {
		this.currencyPair = currency;
	}

	public BigDecimal getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(BigDecimal bidPrice) {
		this.bidPrice = bidPrice;
	}

	public BigDecimal getAskPrice() {
		return askPrice;
	}

	public void setAskPrice(BigDecimal askPrice) {
		this.askPrice = askPrice;
	}

	public String getAskPriceSource() {
		return askPriceSource;
	}

	public void setAskPriceSource(String askPriceSource) {
		this.askPriceSource = askPriceSource;
	}

	public String getBidPriceSource() {
		return bidPriceSource;
	}

	public void setBidPriceSource(String bidPriceSoruce) {
		this.bidPriceSource = bidPriceSoruce;
	}

	public Timestamp getCreOn() {
		return creOn;
	}

	public void setCreOn(Timestamp creOn) {
		this.creOn = creOn;
	}

	public BestCyptoPriceSnapshotEntity() {
		super();
	}

	public BestCyptoPriceSnapshotEntity(Long id, String currencyPair, BigDecimal bidPrice, BigDecimal askPrice,
			Timestamp creOn) {
		super();
		this.id = id;
		this.currencyPair = currencyPair;
		this.bidPrice = bidPrice;
		this.askPrice = askPrice;
		this.creOn = creOn;
	}

}
