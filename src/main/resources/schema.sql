
DROP TABLE IF EXISTS `Exchange_Rate`;
CREATE TABLE `Exchange_Rate` (
    `currency_type` VARCHAR(10) NOT NULL,
    `currency_type_name` VARCHAR(50) NOT NULL,
    `rate` VARCHAR(30) NULL,
    `updated` VARCHAR(20) NULL,
    Primary Key(`currency_type`)
);