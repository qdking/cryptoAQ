package com.main.user;

import java.math.BigDecimal;

public class UserTradeRequestVO {
	private Long userId;
	private String buyingCurrency;
	private BigDecimal buyingQty;
	private String sellingCurrency;
	private BigDecimal sellingQty;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getBuyingCurrency() {
		return buyingCurrency;
	}

	public void setBuyingCurrency(String buyingCurrency) {
		this.buyingCurrency = buyingCurrency;
	}

	public BigDecimal getBuyingQty() {
		return buyingQty;
	}

	public void setBuyingQty(BigDecimal buyingQty) {
		this.buyingQty = buyingQty;
	}

	public String getSellingCurrency() {
		return sellingCurrency;
	}

	public void setSellingCurrency(String sellingCurrency) {
		this.sellingCurrency = sellingCurrency;
	}

	public BigDecimal getSellingQty() {
		return sellingQty;
	}

	public void setSellingQty(BigDecimal sellingQty) {
		this.sellingQty = sellingQty;
	}

	public UserTradeRequestVO() {
		super();
	}
}
