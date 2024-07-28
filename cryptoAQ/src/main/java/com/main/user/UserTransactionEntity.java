package com.main.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_USER_TRANSACTION")
public class UserTransactionEntity {

	@Id
	private Long userid;
}
