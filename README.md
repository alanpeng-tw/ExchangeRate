
### CREATE TABLE

	DROP TABLE IF EXISTS `Exchange_Rate`;
	CREATE TABLE `Exchange_Rate` (
		`currency_type` VARCHAR(10) NOT NULL,
		`currency_type_name` VARCHAR(50) NOT NULL,
		`rate` VARCHAR(30) NULL,
		`updated` VARCHAR(20) NULL,
		Primary Key(`currency_type`)
	);



### 測試

第1點
GET http://localhost:8080/api/exchangeRates/GBP


第2點
POST http://localhost:8080/api/exchangeRates
	
	{
		"currency_type" : "JPY",
		"currency_type_name" : "日幣",
		"rate" : "0.23"
	}

第3點
PUT http://localhost:8080/api/exchangeRates/USD

	{
		"currency_type_name": "美金",
		"rate": "45,720.7133"
	}
	
	
第4點(傳入 id)
DELETE http://localhost:8080/api/exchangeRates/1


第5點
GET http://localhost:8080/api/getCoindeskApi


第6點
GET http://localhost:8080/api/exchangeRates
