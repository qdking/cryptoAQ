package com.main.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserWalletDAO extends JpaRepository<UserWalletEntity, Long> {

	List<UserWalletEntity> findByUserid(Long userId);

	UserWalletEntity findByUseridAndCurrency(Long userId, String currency);
}
