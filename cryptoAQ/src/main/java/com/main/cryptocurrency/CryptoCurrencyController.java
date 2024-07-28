package com.main.cryptocurrency;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CryptoCurrencyController {

	private CryptoCurrencyManagerService cryptoCurrencyManagerService;

	public CryptoCurrencyController(CryptoCurrencyManagerService cryptoCurrencyManagerService) {
		super();
		this.cryptoCurrencyManagerService = cryptoCurrencyManagerService;
	}

	@GetMapping("api/latestprice")
	public Optional<BestCyptoPriceSnapshotEntity> getWalletBalance(@RequestParam String currencyPair) {
		return cryptoCurrencyManagerService.retrieveBestCryptoPriceSnapshot(currencyPair);
	}

}
