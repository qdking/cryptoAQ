package com.main.user;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
		return userServiceManager.retrieveWalletBalance(userid);
	}

	@GetMapping("api/transactions")
	public List<UserTransactionEntity> getTransactionHistory(@RequestParam Long userid) {
		return userServiceManager.retrieveTransactionHistory(userid);
	}

	@PostMapping("/api/trade")
	public ResponseEntity<String> requestCurrencyTradeExchange(@RequestParam Long userId, String buyingCurrency,
			BigDecimal buyingQty, String sellingCurrency, BigDecimal sellingQty) {
		try {
			UserTradeRequestVO vo = new UserTradeRequestVO();
			vo.setUserId(userId);
			vo.setBuyingCurrency(buyingCurrency);
			vo.setBuyingQty(buyingQty);
			vo.setSellingCurrency(sellingCurrency);
			vo.setSellingQty(sellingQty);
			userServiceManager.requestCurrencyTradeExchange(vo);
			return ResponseEntity.ok("Trade executed successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
