package com.main.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserTransactionEntityDAO extends JpaRepository<UserTransactionEntity, Long> {

}
