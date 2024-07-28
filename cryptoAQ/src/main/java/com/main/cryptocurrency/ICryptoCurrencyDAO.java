package com.main.cryptocurrency;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICryptoCurrencyDAO extends JpaRepository<CyptoCurrencyEntity, Long> {

}
