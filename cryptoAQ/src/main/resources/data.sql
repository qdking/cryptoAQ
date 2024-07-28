-- Insert sample User Data for Test purposes (assumed the user is already registered, logged in, and authenticated)
insert into TB_USER_MAIN (
	USER_ID, USERNAME, HASHED_PASSWORD, CRE_ON, UPD_ON
) values (
	1, '6592963649', '5f4dcc3b5aa765d61d8327deb882cf99', CURRENT_TIMESTAMP, null
);


insert into TB_USER_WALLET (
	USER_ID, CURRENCY, QTY_BALANCE, CRE_ON, UPD_ON
) values (
	1, 'USDT', '50000', CURRENT_TIMESTAMP, null
);

