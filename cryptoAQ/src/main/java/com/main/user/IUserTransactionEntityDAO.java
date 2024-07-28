package com.main.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserTransactionEntityDAO extends JpaRepository<UserTransactionEntity, Long> {

	List<UserTransactionEntity> findByUserid(Long userId);
}
