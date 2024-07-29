package com.main.testuser;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.main.common.Constants;
import com.main.cryptocurrency.BestCyptoPriceSnapshotEntity;
import com.main.cryptocurrency.IBestCryptoPriceSnapshotDAO;
import com.main.user.IUserDAO;
import com.main.user.IUserTransactionEntityDAO;
import com.main.user.IUserWalletDAO;
import com.main.user.UserEntity;
import com.main.user.UserServiceManager;
import com.main.user.UserTradeRequestVO;
import com.main.user.UserTransactionEntity;
import com.main.user.UserWalletEntity;

@SpringBootTest
class TestUserManagementService {

	@Autowired
	private IUserDAO userDAO;
	@Autowired
	private IUserWalletDAO userWalletDAO;
	@Autowired
	private UserServiceManager userServiceManager;

	@Autowired
	private IBestCryptoPriceSnapshotDAO bestCryptoPriceSnapshotDAO;
	@Autowired
	private IUserTransactionEntityDAO userTransactionEntityDAO;

	private final static Long TESTUSER_ID = 200L;

	@Test
	void testSellUSDTBuyETH() {

		UserEntity testUser = new UserEntity(TESTUSER_ID, "testUser", "hashedpassword",
				Timestamp.valueOf(LocalDateTime.now()));
		userDAO.save(testUser);

		UserWalletEntity usdtWallet = new UserWalletEntity(TESTUSER_ID, "USDT", BigDecimal.valueOf(1000.00));
		usdtWallet.setCreOn(Timestamp.valueOf(LocalDateTime.now()));
		userWalletDAO.save(usdtWallet);

		BestCyptoPriceSnapshotEntity priceSnapshot = new BestCyptoPriceSnapshotEntity("ETHUSDT");
		priceSnapshot.setBidPrice(BigDecimal.valueOf(3000.00));
		priceSnapshot.setBidPriceSource(Constants.BINANCE);
		priceSnapshot.setAskPrice(BigDecimal.valueOf(3100.00));
		priceSnapshot.setAskPriceSource(Constants.BINANCE);
		priceSnapshot.setCreOn(Timestamp.valueOf(LocalDateTime.now()));
		bestCryptoPriceSnapshotDAO.save(priceSnapshot);

		UserTradeRequestVO tradeRequest = new UserTradeRequestVO();
		tradeRequest.setUserId(TESTUSER_ID);
		tradeRequest.setBuyingCurrency(Constants.ETHEREUM_SYMBOL);
		tradeRequest.setSellingCurrency(Constants.USDTETHER_SYMBOL);
		tradeRequest.setBuyingQty(BigDecimal.valueOf(0.1));
		tradeRequest.setSellingQty(BigDecimal.valueOf(300));
		userServiceManager.requestCurrencyTradeExchange(tradeRequest);

		// Verify wallet updates
		UserWalletEntity updatedUsdtWallet = userWalletDAO.findByUseridAndCurrency(TESTUSER_ID, "USDT");
		UserWalletEntity ethWallet = userWalletDAO.findByUseridAndCurrency(TESTUSER_ID, "ETH");

		assertThat(updatedUsdtWallet.getQtyBalance()).isEqualByComparingTo(BigDecimal.valueOf(700.00));
		assertThat(ethWallet.getQtyBalance()).isEqualByComparingTo(BigDecimal.valueOf(0.1));

		List<UserTransactionEntity> transactionList = userTransactionEntityDAO
				.findByUseridOrderByCreOnDesc(TESTUSER_ID);
		assertThat(transactionList).isNotNull().size().isEqualTo(1);
	}
}
