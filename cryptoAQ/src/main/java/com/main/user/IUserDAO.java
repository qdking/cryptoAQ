package com.main.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserDAO extends JpaRepository<UserEntity, Long> {

}
