package com.main.cryptocurrency;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.main.common.Constants;

@Component
@Service
public class CryptoCurrencyManagerService {

	private IBestCryptoPriceSnapshotDAO cryptoCurrencyDAO;

	private RestTemplate restTemplate;
	private Logger logger;

	@Autowired
	public CryptoCurrencyManagerService(IBestCryptoPriceSnapshotDAO cryptoCurrencyDAO, RestTemplate restTemplate) {
		super();
		this.cryptoCurrencyDAO = cryptoCurrencyDAO;
		this.restTemplate = restTemplate;
	}

	public Optional<BestCyptoPriceSnapshotEntity> retrieveBestCryptoPriceSnapshot(String currencyPair) {
		List<BestCyptoPriceSnapshotEntity> snapshotList = this.cryptoCurrencyDAO
				.findByCurrencyPairOrderByCreOnDesc(currencyPair);
		return snapshotList.isEmpty() ? Optional.empty() : Optional.of(snapshotList.get(0));
	}

	@Scheduled(fixedRate = 10000)
	public void aggregateBestPrices() {
		List<Map<String, String>> binancePrices = retrieveBinancePrices();
		Map<String, Map<String, String>> huobiPrices = retrieveHuobiPrices();

		compareAndSaveBestPrices(binancePrices, huobiPrices, Constants.ETHUSTD_PAIR);
		compareAndSaveBestPrices(binancePrices, huobiPrices, Constants.BTCUSTD_PAIR);

	}

	private List<Map<String, String>> retrieveBinancePrices() {
		try {
			ResponseEntity<List<Map<String, String>>> response = restTemplate.exchange(Constants.BINANCE_URL,
					HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, String>>>() {
					});
			return response.getBody();
		} catch (Exception e) {
			logger.info("Error fetching prices From Binance: " + e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	private Map<String, Map<String, String>> retrieveHuobiPrices() {
		ResponseEntity<Map> response = restTemplate.getForEntity(Constants.HUOBI_URL, Map.class);
		Map<String, Map<String, String>> huobiPrices = new HashMap<>();

		if (response.getBody() != null) {
			List<Map<String, String>> data = (List<Map<String, String>>) response.getBody().get("data");
			for (Map<String, String> entry : data) {
				huobiPrices.put(entry.get(Constants.SYMBOL).toUpperCase(), entry);
			}
		}
		return huobiPrices;
	}

	private void compareAndSaveBestPrices(List<Map<String, String>> binancePrices,
			Map<String, Map<String, String>> huobiPrices, String currencySymbol) {
		BigDecimal bestBidPrice = BigDecimal.ZERO;
		BigDecimal bestAskPrice = BigDecimal.valueOf(Double.MAX_VALUE);
		String bestBidSource = Constants.BLANK;
		String bestAskSource = Constants.BLANK;

		for (Map<String, String> price : binancePrices) {
			if (price.get(Constants.SYMBOL).equals(currencySymbol)) {
				BigDecimal binanceBidPrice = new BigDecimal(price.get(Constants.BINANCE_JSON_BID_KEY));
				BigDecimal binanceAskPrice = new BigDecimal(price.get(Constants.BINANCE_JSON_ASK_KEY));
				if (binanceBidPrice.compareTo(bestBidPrice) > 0) {
					bestBidPrice = binanceBidPrice;
					bestBidSource = Constants.BINANCE;
				}
				if (binanceAskPrice.compareTo(bestAskPrice) < 0) {
					bestAskPrice = binanceAskPrice;
					bestAskSource = Constants.BINANCE;
				}
				break;
			}
		}

		if (huobiPrices.containsKey(currencySymbol)) {
			Map<String, String> huobiPrice = huobiPrices.get(currencySymbol);

			Object huobiBidValue = huobiPrice.get(Constants.HUOBI_JSON_BID_KEY);
			Object huobiAskValue = huobiPrice.get(Constants.HUOBI_JSON_ASK_KEY);
			BigDecimal huobiBidPrice;
			BigDecimal huobiAskPrice;

			if (huobiBidValue instanceof String s) {
				huobiBidPrice = new BigDecimal(s);
			} else if (huobiBidValue instanceof Double dbl) {
				huobiBidPrice = BigDecimal.valueOf(dbl);
			} else {
				throw new IllegalArgumentException(
						"Unexpected type for HUOBI bid price: " + huobiBidValue.getClass().getName());
			}

			if (huobiAskValue instanceof String s) {
				huobiAskPrice = new BigDecimal(s);
			} else if (huobiAskValue instanceof Double dbl) {
				huobiAskPrice = BigDecimal.valueOf(dbl);
			} else {
				throw new IllegalArgumentException(
						"Unexpected type for HUOBI ask price: " + huobiAskValue.getClass().getName());
			}

			if (huobiBidPrice.compareTo(bestBidPrice) > 0) {
				bestBidPrice = huobiBidPrice;
				bestBidSource = Constants.HUOBI;
			}
			if (huobiAskPrice.compareTo(bestAskPrice) < 0) {
				bestAskPrice = huobiAskPrice;
				bestAskSource = Constants.HUOBI;
			}
		}

		BestCyptoPriceSnapshotEntity bestBidCryptoPrice = new BestCyptoPriceSnapshotEntity();
		bestBidCryptoPrice.setCurrencyPair(currencySymbol);
		bestBidCryptoPrice.setAskPrice(bestAskPrice);
		bestBidCryptoPrice.setBidPrice(bestBidPrice);
		bestBidCryptoPrice.setAskPriceSource(bestAskSource);
		bestBidCryptoPrice.setBidPriceSource(bestBidSource);
		bestBidCryptoPrice.setCreOn(Timestamp.valueOf(LocalDateTime.now()));
		cryptoCurrencyDAO.save(bestBidCryptoPrice);
	}

}