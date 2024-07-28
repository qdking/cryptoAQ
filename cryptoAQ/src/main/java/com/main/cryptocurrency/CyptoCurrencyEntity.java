package com.main.cryptocurrency;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_CURRENCY_PRICE_SNAPSHOT")
public class CyptoCurrencyEntity {

	@Id
	private Long userid;
}
