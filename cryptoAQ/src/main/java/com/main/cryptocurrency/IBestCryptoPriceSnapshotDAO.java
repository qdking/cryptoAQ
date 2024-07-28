package com.main.cryptocurrency;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IBestCryptoPriceSnapshotDAO extends JpaRepository<BestCyptoPriceSnapshotEntity, Long> {

	@Query("SELECT b FROM BestCyptoPriceSnapshotEntity b WHERE b.currencyPair = :currencyPair ORDER BY b.creOn DESC")
	List<BestCyptoPriceSnapshotEntity> findByCurrencyPairOrderByCreOnDesc(@Param("currencyPair") String currencyPair);
}
