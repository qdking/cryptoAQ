package com.main.user;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController {

	private UserServiceManager userServiceManager;

	public UserController(UserServiceManager userServiceManager) {
		super();
		this.userServiceManager = userServiceManager;
	}

	@GetMapping("api/wallet")
	public List<UserWalletEntity> getWalletBalance(@RequestParam Long userid) {
		return userServiceManager.getWalletBalance(userid);
	}

	@GetMapping("api/transactions")
	public List<UserTransactionEntity> getTransactionHistory(@RequestParam Long userid) {
		return userServiceManager.getTransactionHistory(userid);
	}
}
