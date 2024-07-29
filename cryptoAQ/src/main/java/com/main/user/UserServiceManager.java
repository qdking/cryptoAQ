package com.main.user;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.main.common.Constants;
import com.main.cryptocurrency.BestCyptoPriceSnapshotEntity;
import com.main.cryptocurrency.CryptoCurrencyManagerService;

@Component
@Service
public class UserServiceManager {

	private IUserWalletDAO userWalletDAO;
	private IUserTransactionEntityDAO userTransactionEntityDAO;
	private CryptoCurrencyManagerService cryptoCurrencyManagerService;

	@Autowired
	public UserServiceManager(IUserWalletDAO userWalletDAO, IUserTransactionEntityDAO userTransactionEntityDAO,
			CryptoCurrencyManagerService cryptoCurrencyManagerService) {
		super();
		this.userWalletDAO = userWalletDAO;
		this.userTransactionEntityDAO = userTransactionEntityDAO;
		this.cryptoCurrencyManagerService = cryptoCurrencyManagerService;
	}

	public List<UserWalletEntity> retrieveWalletBalance(Long userid) {
		return this.userWalletDAO.findByUserid(userid);
	}

	public List<UserTransactionEntity> retrieveTransactionHistory(Long userid) {
		return this.userTransactionEntityDAO.findByUseridOrderByCreOnDesc(userid);
	}

	public void requestCurrencyTradeExchange(UserTradeRequestVO userTradeRequestVO) {

		validateRequestInfo(userTradeRequestVO);

		String currencyPair = currencyPairStringBuilder(userTradeRequestVO);
		Optional<BestCyptoPriceSnapshotEntity> latestPrice = cryptoCurrencyManagerService
				.retrieveBestCryptoPriceSnapshot(currencyPair);

		if (Objects.isNull(latestPrice) || latestPrice.isEmpty()) {
			throw new RuntimeException("No latest price found for the currency pair");
		}

		UserWalletEntity userSellingCurrencyWallet = userWalletDAO
				.findByUseridAndCurrency(userTradeRequestVO.getUserId(), userTradeRequestVO.getSellingCurrency());
		BigDecimal askPrice = latestPrice.get().getAskPrice();
		BigDecimal totalCost = askPrice.multiply(userTradeRequestVO.getBuyingQty());

		validateSufficientWalletBalance(userSellingCurrencyWallet, totalCost);

		executeTradeExchange(userTradeRequestVO, latestPrice.get().getBidPrice(), userSellingCurrencyWallet, totalCost);
	}

	private void executeTradeExchange(UserTradeRequestVO userTradeRequestVO, BigDecimal bidPrice,
			UserWalletEntity userSellingCurrencyWallet, BigDecimal totalCost) {

		userSellingCurrencyWallet
				.setQtyBalance(userSellingCurrencyWallet.getQtyBalance().subtract(userTradeRequestVO.getSellingQty()));
		userWalletDAO.save(userSellingCurrencyWallet);

		BigDecimal totalRevenue = bidPrice.multiply(userTradeRequestVO.getBuyingQty());

		UserWalletEntity userBuyingCurrencyWallet = userWalletDAO
				.findByUseridAndCurrency(userTradeRequestVO.getUserId(), userTradeRequestVO.getBuyingCurrency());

		if (userBuyingCurrencyWallet == null) {
			userBuyingCurrencyWallet = new UserWalletEntity();
			userBuyingCurrencyWallet.setCreOn(Timestamp.valueOf(LocalDateTime.now()));
			userBuyingCurrencyWallet.setCurrency(userTradeRequestVO.getBuyingCurrency());
			userBuyingCurrencyWallet.setQtyBalance(userTradeRequestVO.getBuyingQty());
			userBuyingCurrencyWallet.setUserid(userTradeRequestVO.getUserId());
		} else {
			userBuyingCurrencyWallet.setUpdOn(Timestamp.valueOf(LocalDateTime.now()));
			userBuyingCurrencyWallet
					.setQtyBalance(userBuyingCurrencyWallet.getQtyBalance().add(userTradeRequestVO.getBuyingQty()));
		}

		userWalletDAO.save(userBuyingCurrencyWallet);
		saveUserTransactionLog(userTradeRequestVO, totalCost, totalRevenue);
	}

	private void saveUserTransactionLog(UserTradeRequestVO userTradeRequestVO, BigDecimal totalCost,
			BigDecimal totalRevenue) {
		UserTransactionEntity transaction = new UserTransactionEntity(userTradeRequestVO.getUserId());
		transaction.setCreOn(Timestamp.valueOf(LocalDateTime.now()));
		transaction.setCurrencyBought(userTradeRequestVO.getBuyingCurrency());
		transaction.setCurrencySold(userTradeRequestVO.getSellingCurrency());
		transaction.setQtyBought(userTradeRequestVO.getBuyingQty());
		transaction.setQtySold(userTradeRequestVO.getSellingQty());
		transaction.setPriceBought(totalCost);
		transaction.setPriceSold(totalRevenue);
		userTransactionEntityDAO.save(transaction);
	}

	private void validateRequestInfo(UserTradeRequestVO userTradeRequestVO) {
		if (StringUtils.isBlank(userTradeRequestVO.getBuyingCurrency())
				|| StringUtils.isBlank(userTradeRequestVO.getSellingCurrency())
				|| userTradeRequestVO.getUserId() == null || userTradeRequestVO.getBuyingQty() == null
				|| userTradeRequestVO.getSellingQty() == null
				|| (userTradeRequestVO.getBuyingQty() != null
						&& userTradeRequestVO.getBuyingQty().compareTo(BigDecimal.ZERO) < 0)
				|| (userTradeRequestVO.getSellingQty() != null
						&& userTradeRequestVO.getSellingQty().compareTo(BigDecimal.ZERO) < 0)) {
			throw new RuntimeException("Invalid request values");
		}
	}

	private String currencyPairStringBuilder(UserTradeRequestVO userTradeRequestVO) {
		StringBuilder currencyPairStringBuilder = new StringBuilder();

		if (Constants.ETHEREUM_SYMBOL.equalsIgnoreCase(userTradeRequestVO.getBuyingCurrency())
				|| Constants.ETHEREUM_SYMBOL.equalsIgnoreCase(userTradeRequestVO.getSellingCurrency())) {
			currencyPairStringBuilder.append(Constants.ETHEREUM_SYMBOL);
		} else if (Constants.BITCOIN_SYMBOL.equalsIgnoreCase(userTradeRequestVO.getBuyingCurrency())
				|| Constants.BITCOIN_SYMBOL.equalsIgnoreCase(userTradeRequestVO.getSellingCurrency())) {
			currencyPairStringBuilder.append(Constants.BITCOIN_SYMBOL);
		}
		// Assuming only USDT paired with either BTC or ETH
		// For this assignment: Only support Ethereum - ETHUSDT and Bitcoin - BTCUSDT
		currencyPairStringBuilder.append(Constants.USDTETHER_SYMBOL);

		return currencyPairStringBuilder.toString();
	}

	private void validateSufficientWalletBalance(UserWalletEntity userSellingCurrencyWallet, BigDecimal totalCost) {
		if (userSellingCurrencyWallet == null || userSellingCurrencyWallet.getQtyBalance().compareTo(totalCost) < 0) {
			throw new RuntimeException("Insufficient balance");
		}
	}

	public UserServiceManager() {
		super();
	}
}
