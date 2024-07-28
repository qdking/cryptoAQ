package com.main.cryptocurrency;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IBestCryptoPriceSnapshotDAO extends JpaRepository<BestCyptoPriceSnapshotEntity, Long> {

	List<BestCyptoPriceSnapshotEntity> findByCurrencyPair(String currencyPair);
}
