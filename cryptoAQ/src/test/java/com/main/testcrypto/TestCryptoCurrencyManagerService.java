package com.main.testcrypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.main.common.Constants;
import com.main.cryptocurrency.BestCyptoPriceSnapshotEntity;
import com.main.cryptocurrency.CryptoCurrencyManagerService;
import com.main.cryptocurrency.IBestCryptoPriceSnapshotDAO;

@SpringBootTest
class TestCryptoCurrencyManagerService {

	@Autowired
	private CryptoCurrencyManagerService cryptoCurrencyManagerService;

	@MockBean
	private IBestCryptoPriceSnapshotDAO bestCryptoPriceSnapshotDAO;

	@MockBean
	private RestTemplate restTemplate;

	@Test
	void testFetchAndStorePrices() {

		List<Map<String, String>> binanceResponse = new ArrayList<>();
		Map<String, String> ethBinance = new HashMap<>();
		ethBinance.put(Constants.SYMBOL, "ETHUSDT");
		ethBinance.put("bidPrice", "2000.00");
		ethBinance.put("askPrice", "2005.00");
		binanceResponse.add(ethBinance);

		Map<String, String> btcBinance = new HashMap<>();
		btcBinance.put(Constants.SYMBOL, "BTCUSDT");
		btcBinance.put("bidPrice", "30000.00");
		btcBinance.put("askPrice", "30010.00");
		binanceResponse.add(btcBinance);

		Mockito.when(restTemplate.exchange(eq("https://api.binance.com/api/v3/ticker/bookTicker"), eq(HttpMethod.GET),
				any(), eq(new ParameterizedTypeReference<List<Map<String, String>>>() {
				}))).thenReturn(new ResponseEntity<>(binanceResponse, HttpStatus.OK));

		Map<String, Object> huobiResponse = new HashMap<>();
		List<Map<String, String>> huobiData = new ArrayList<>();
		Map<String, String> ethHuobi = new HashMap<>();
		ethHuobi.put(Constants.SYMBOL, "ethusdt");
		ethHuobi.put("bid", "2001.00");
		ethHuobi.put("ask", "2004.00");
		huobiData.add(ethHuobi);

		Map<String, String> btcHuobi = new HashMap<>();
		btcHuobi.put(Constants.SYMBOL, "btcusdt");
		btcHuobi.put("bid", "29950.00");
		btcHuobi.put("ask", "30005.00");
		huobiData.add(btcHuobi);

		huobiResponse.put("data", huobiData);

		Mockito.when(restTemplate.getForEntity(eq("https://api.huobi.pro/market/tickers"), eq(Map.class)))
				.thenReturn(new ResponseEntity<>(huobiResponse, HttpStatus.OK));

		// Execute the method
		cryptoCurrencyManagerService.aggregateBestPrices();

		// Verify the repository save calls
		ArgumentCaptor<BestCyptoPriceSnapshotEntity> priceCaptor = ArgumentCaptor
				.forClass(BestCyptoPriceSnapshotEntity.class);
		Mockito.verify(bestCryptoPriceSnapshotDAO, Mockito.times(2)).save(priceCaptor.capture());

		List<BestCyptoPriceSnapshotEntity> savedPrices = priceCaptor.getAllValues();
		assertEquals(2, savedPrices.size());

		BestCyptoPriceSnapshotEntity savedEthPrice = savedPrices.stream()
				.filter(price -> "ETHUSDT".equals(price.getCurrencyPair())).findFirst().orElse(null);
		assertNotNull(savedEthPrice);
		assertEquals(new BigDecimal("2001.00"), savedEthPrice.getBidPrice());
		assertEquals(new BigDecimal("2004.00"), savedEthPrice.getAskPrice());
		assertEquals(Constants.HUOBI, savedEthPrice.getBidPriceSource());
		assertEquals(Constants.HUOBI, savedEthPrice.getAskPriceSource());

		BestCyptoPriceSnapshotEntity savedBtcPrice = savedPrices.stream()
				.filter(price -> "BTCUSDT".equals(price.getCurrencyPair())).findFirst().orElse(null);
		assertNotNull(savedBtcPrice);
		assertEquals(new BigDecimal("30000.00"), savedBtcPrice.getBidPrice());
		assertEquals(new BigDecimal("30005.00"), savedBtcPrice.getAskPrice());
		assertEquals(Constants.BINANCE, savedBtcPrice.getBidPriceSource());
		assertEquals(Constants.HUOBI, savedBtcPrice.getAskPriceSource());
	}
}
