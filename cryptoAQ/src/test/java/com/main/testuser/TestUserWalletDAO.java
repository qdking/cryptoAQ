package com.main.testuser;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.main.common.Constants;
import com.main.user.IUserDAO;
import com.main.user.IUserWalletDAO;
import com.main.user.UserEntity;
import com.main.user.UserWalletEntity;

@DataJpaTest
class TestUserWalletDAO {

	@Autowired
	private IUserWalletDAO userWalletDAO;

	@Autowired
	private IUserDAO userDAO;

	private final static Long TESTUSERID = 2000L;

	@Test
	void testUserWalletDAO() {

		UserEntity testUser = new UserEntity(TESTUSERID, "testUser", "hashedpassword",
				Timestamp.valueOf(LocalDateTime.now()));
		userDAO.save(testUser);

		Optional<UserEntity> retrievedUser = userDAO.findById(TESTUSERID);
		assertThat(retrievedUser).isNotNull();
		assertThat(retrievedUser.get().getUsername()).isEqualTo("testUser");
		assertThat(retrievedUser.get().getHashedPassword()).isEqualTo("hashedpassword");

		UserWalletEntity testWalletUSDT = new UserWalletEntity(TESTUSERID, "USDT", BigDecimal.valueOf(300.15),
				Timestamp.valueOf(LocalDateTime.now()), null);

		UserWalletEntity testWalletBTC = new UserWalletEntity(TESTUSERID, "BTC", BigDecimal.valueOf(5000.0095),
				Timestamp.valueOf(LocalDateTime.now()), null);

		UserWalletEntity testWalletETH = new UserWalletEntity(TESTUSERID, "ETH", BigDecimal.valueOf(0),
				Timestamp.valueOf(LocalDateTime.now()), null);

		userWalletDAO.save(testWalletUSDT);
		userWalletDAO.save(testWalletBTC);
		userWalletDAO.save(testWalletETH);

		List<UserWalletEntity> walletRetrievedViaUserIdList = this.userWalletDAO.findByUserid(TESTUSERID);
		assertThat(walletRetrievedViaUserIdList).isNotNull().size().isEqualTo(3);

		List<UserWalletEntity> usdtList = walletRetrievedViaUserIdList.stream()
				.filter(e -> "USDT".equals(e.getCurrency())).toList();

		assertThat(usdtList).isNotNull().size().isEqualTo(1);
		assertThat(usdtList.get(0).getQtyBalance()).isEqualTo(BigDecimal.valueOf(300.15));

		List<UserWalletEntity> btcList = walletRetrievedViaUserIdList.stream()
				.filter(e -> "BTC".equals(e.getCurrency())).toList();
		assertThat(btcList).isNotNull().size().isEqualTo(1);
		assertThat(btcList.get(0).getQtyBalance()).isEqualTo(BigDecimal.valueOf(5000.0095));

		List<UserWalletEntity> ethList = walletRetrievedViaUserIdList.stream()
				.filter(e -> "ETH".equals(e.getCurrency())).toList();
		assertThat(ethList).isNotNull().size().isEqualTo(1);
		assertThat(ethList.get(0).getQtyBalance()).isEqualTo(BigDecimal.valueOf(0));

		UserWalletEntity ethFindByIdAndCurrency = this.userWalletDAO.findByUseridAndCurrency(TESTUSERID,
				Constants.ETHEREUM_SYMBOL);
		assertThat(ethFindByIdAndCurrency).isNotNull();
		assertThat(ethFindByIdAndCurrency.getQtyBalance()).isEqualTo(BigDecimal.valueOf(0));

	}
}
