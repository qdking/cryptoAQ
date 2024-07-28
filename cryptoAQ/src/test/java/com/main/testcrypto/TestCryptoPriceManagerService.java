package com.main.testcrypto;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.main.common.Constants;
import com.main.cryptocurrency.BestCyptoPriceSnapshotEntity;
import com.main.cryptocurrency.CryptoCurrencyManagerService;
import com.main.cryptocurrency.IBestCryptoPriceSnapshotDAO;

@SpringBootTest
@Transactional
class TestCryptoPriceManagerService {

	@Autowired
	private CryptoCurrencyManagerService cryptoCurrencyManagerService;
	@Autowired
	private IBestCryptoPriceSnapshotDAO bestCryptoPriceSnapshotDAO;

	@Test
	void testGetLatestPriceSnapshot() {

		BestCyptoPriceSnapshotEntity snapshot1 = new BestCyptoPriceSnapshotEntity();
		snapshot1.setCurrencyPair(Constants.ETHUSTD_PAIR);
		snapshot1.setBidPrice(BigDecimal.valueOf(1));
		snapshot1.setAskPrice(BigDecimal.valueOf(1));
		snapshot1.setBidPriceSource(Constants.BINANCE);
		snapshot1.setAskPriceSource(Constants.BINANCE);
		snapshot1.setCreOn(Timestamp.valueOf(LocalDateTime.now().minusMinutes(10)));
		bestCryptoPriceSnapshotDAO.save(snapshot1);

		BestCyptoPriceSnapshotEntity snapshot2 = new BestCyptoPriceSnapshotEntity();
		snapshot2.setCurrencyPair(Constants.ETHUSTD_PAIR);
		snapshot2.setBidPrice(BigDecimal.valueOf(1));
		snapshot2.setAskPrice(BigDecimal.valueOf(1));
		snapshot2.setBidPriceSource(Constants.BINANCE);
		snapshot2.setAskPriceSource(Constants.BINANCE);
		snapshot2.setCreOn(Timestamp.valueOf(LocalDateTime.now()));
		bestCryptoPriceSnapshotDAO.save(snapshot2);

		Optional<BestCyptoPriceSnapshotEntity> latestSnapshot = cryptoCurrencyManagerService
				.retrieveBestCryptoPriceSnapshot(Constants.ETHUSTD_PAIR);

		assertThat(latestSnapshot).isPresent();
		assertThat(latestSnapshot.get().getCreOn()).isEqualTo(snapshot2.getCreOn());
	}
}
