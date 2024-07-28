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

import com.main.user.IUserDAO;
import com.main.user.IUserTransactionEntityDAO;
import com.main.user.UserEntity;
import com.main.user.UserTransactionEntity;

@DataJpaTest
class TestUserTransactionDAO {

	@Autowired
	private IUserTransactionEntityDAO userTransactionEntityDAO;

	@Autowired
	private IUserDAO userDAO;

	private final static Long TESTUSERID = 2000L;
	private final static Long TESTUSERID2 = 3000L;

	@Test
	void testFindTransactionHistoryByUserid() {

		UserEntity testUser = new UserEntity(TESTUSERID, "testUser", "hashedpassword",
				Timestamp.valueOf(LocalDateTime.now()));
		UserEntity testUser2 = new UserEntity(TESTUSERID2, "testUser2", "hashedpassword2",
				Timestamp.valueOf(LocalDateTime.now()));
		userDAO.save(testUser);
		userDAO.save(testUser2);

		Optional<UserEntity> retrievedUser = userDAO.findById(TESTUSERID);
		assertThat(retrievedUser).isNotNull();
		assertThat(retrievedUser.get().getUsername()).isEqualTo("testUser");
		assertThat(retrievedUser.get().getHashedPassword()).isEqualTo("hashedpassword");

		Optional<UserEntity> retrievedUser2 = userDAO.findById(TESTUSERID2);
		assertThat(retrievedUser2).isNotNull();
		assertThat(retrievedUser2.get().getUsername()).isEqualTo("testUser2");
		assertThat(retrievedUser2.get().getHashedPassword()).isEqualTo("hashedpassword2");

		UserTransactionEntity transactionObject = new UserTransactionEntity(TESTUSERID);

		transactionObject.setCurrencyBought("ETH");
		transactionObject.setCurrencySold("BTC");
		transactionObject.setQtyBought(BigDecimal.valueOf(5));
		transactionObject.setQtySold(BigDecimal.valueOf(1.1));
		transactionObject.setPriceBought(BigDecimal.valueOf(9.9));
		transactionObject.setPriceSold(BigDecimal.valueOf(7.77));
		transactionObject.setCreOn(Timestamp.valueOf(LocalDateTime.now()));

		UserTransactionEntity transactionObject2 = new UserTransactionEntity(TESTUSERID2);

		transactionObject2.setCurrencyBought("ETH");
		transactionObject2.setCurrencySold("BTC");
		transactionObject2.setQtyBought(BigDecimal.valueOf(4.4));
		transactionObject2.setQtySold(BigDecimal.valueOf(3.333));
		transactionObject2.setPriceBought(BigDecimal.valueOf(8.88));
		transactionObject2.setPriceSold(BigDecimal.valueOf(1.1111));
		transactionObject2.setCreOn(Timestamp.valueOf(LocalDateTime.now()));

		userTransactionEntityDAO.save(transactionObject);
		userTransactionEntityDAO.save(transactionObject2);

		List<UserTransactionEntity> list = userTransactionEntityDAO.findByUserid(TESTUSERID);
		List<UserTransactionEntity> list2 = userTransactionEntityDAO.findByUserid(TESTUSERID2);

		assertThat(list).isNotNull().size().isEqualTo(1);
		assertThat(list.get(0).getCurrencyBought()).isEqualTo("ETH");
		assertThat(list.get(0).getCurrencySold()).isEqualTo("BTC");
		assertThat(list.get(0).getQtyBought()).isEqualTo(BigDecimal.valueOf(5));
		assertThat(list.get(0).getQtySold()).isEqualTo(BigDecimal.valueOf(1.1));
		assertThat(list.get(0).getPriceBought()).isEqualTo(BigDecimal.valueOf(9.9));
		assertThat(list.get(0).getPriceSold()).isEqualTo(BigDecimal.valueOf(7.77));

		assertThat(list2).isNotNull().size().isEqualTo(1);
		assertThat(list2.get(0).getCurrencyBought()).isEqualTo("ETH");
		assertThat(list2.get(0).getCurrencySold()).isEqualTo("BTC");
		assertThat(list2.get(0).getQtyBought()).isEqualTo(BigDecimal.valueOf(4.4));
		assertThat(list2.get(0).getQtySold()).isEqualTo(BigDecimal.valueOf(3.333));
		assertThat(list2.get(0).getPriceBought()).isEqualTo(BigDecimal.valueOf(8.88));
		assertThat(list2.get(0).getPriceSold()).isEqualTo(BigDecimal.valueOf(1.1111));
	}
}
