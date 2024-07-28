package com.main.user;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Component
@Service
public class UserServiceManager {

	private IUserWalletDAO userWalletDAO;
	private IUserTransactionEntityDAO userTransactionEntityDAO;

	@Autowired
	public UserServiceManager(IUserWalletDAO userWalletDAO, IUserTransactionEntityDAO userTransactionEntityDAO) {
		super();
		this.userWalletDAO = userWalletDAO;
		this.userTransactionEntityDAO = userTransactionEntityDAO;
	}

	public List<UserWalletEntity> retrieveWalletBalance(Long userid) {

		return this.userWalletDAO.findByUserid(userid);
	}

	public List<UserTransactionEntity> getTransactionHistory(Long userid) {
		List<UserTransactionEntity> unsortedList = this.userTransactionEntityDAO.findByUserid(userid);

		if (!CollectionUtils.isEmpty(unsortedList)) {
			return unsortedList.stream().sorted((t1, t2) -> t2.getCreOn().compareTo(t1.getCreOn())).toList();
		}
		return Collections.emptyList();
	}

	public List<UserWalletEntity> sell(Long userid) {
		return this.retrieveWalletBalance(userid);
	}

	public UserServiceManager() {
		super();
	}
}
