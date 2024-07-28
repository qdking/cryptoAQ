package com.main.cryptocurrency;

import org.springframework.stereotype.Component;

@Component
public class CryptoCurrencyManagerService {

	private ICryptoCurrencyDAO cryptoCurrencyDAO;

	public CryptoCurrencyManagerService(ICryptoCurrencyDAO cryptoCurrencyDAO) {
		super();
		this.cryptoCurrencyDAO = cryptoCurrencyDAO;
	}

}
