package com.main.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class UserServiceManager {

	private IUserWalletDAO userWalletDAO;

	public UserServiceManager() {
		super();
	}

	@Autowired
	public UserServiceManager(IUserWalletDAO userWalletDAO) {
		super();
		this.userWalletDAO = userWalletDAO;
	}

	public List<UserWalletEntity> getWalletBalance(Long userid) {

		List<Long> idList = new ArrayList<>();

		idList.add(userid);
		return this.userWalletDAO.findAllById(idList);
	}
}
