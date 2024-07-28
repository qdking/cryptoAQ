package com.main.cryptocurrency;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public CryptoCurrencyManagerService(IBestCryptoPriceSnapshotDAO cryptoCurrencyDAO, RestTemplate restTemplate) {
		super();
		this.cryptoCurrencyDAO = cryptoCurrencyDAO;
		this.restTemplate = restTemplate;
	}

	@Scheduled(fixedRate = 10000)
	public void aggregateBestPrices() {
		List<Map<String, String>> binancePrices = retrieveBinancePrices();
		Map<String, Map<String, String>> huobiPrices = retrieveHuobiPrices();

		compareAndSaveBestPrices(binancePrices, huobiPrices, Constants.ETHUSTD_PAIR);
		compareAndSaveBestPrices(binancePrices, huobiPrices, Constants.BTCUSTD_PAIR);

	}

	private List<Map<String, String>> retrieveBinancePrices() {
		ResponseEntity<List<Map<String, String>>> response = restTemplate.exchange(Constants.BINANCE_URL,
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, String>>>() {
				});
		return response.getBody();
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
			BigDecimal huobiBidPrice = new BigDecimal(huobiPrice.get(Constants.HUOBI_JSON_BID_KEY));
			BigDecimal huobiAskPrice = new BigDecimal(huobiPrice.get(Constants.HUOBI_JSON_ASK_KEY));
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