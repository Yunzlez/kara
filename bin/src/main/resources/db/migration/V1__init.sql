-- -----------------------------------------------------
-- Schema kara
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `kara` DEFAULT CHARACTER SET latin1 ;
USE `kara` ;

-- -----------------------------------------------------
-- Table `kara`.`bin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kara`.`bin` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `creation_date` DATE NULL DEFAULT NULL,
  `last_request` DATE NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `permanent` BIT(1) NOT NULL,
  `request_count` INT(11) NOT NULL,
  `reply_id` BIGINT(20) NULL DEFAULT NULL,
  `request_metric_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_jcbr94tda6vd4awhlwkqet9eb` (`name` ASC) ,
  INDEX `FKoei190vbxjw4bpy5fjb6pyr5n` (`reply_id` ASC) ,
  INDEX `FKsuhjsedoxrvx2m0ogjaby2rs1` (`request_metric_id` ASC) )
  ENGINE = MyISAM
  DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `kara`.`binary_request`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kara`.`binary_request` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `binary_request` LONGBLOB NULL DEFAULT NULL,
  `bin_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKf5fg0n19ohs4sl32x7qftifow` (`bin_id` ASC) )
  ENGINE = MyISAM
  DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `kara`.`reply`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kara`.`reply` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `body` TEXT NULL DEFAULT NULL,
  `code` INT(11) NULL DEFAULT NULL,
  `custom` BIT(1) NOT NULL,
  `mime_type` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
  ENGINE = MyISAM
  DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `kara`.`reply_cookies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kara`.`reply_cookies` (
  `reply_id` BIGINT(20) NOT NULL,
  `cookies` VARCHAR(255) NULL DEFAULT NULL,
  `cookies_key` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`reply_id`, `cookies_key`))
  ENGINE = MyISAM
  DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `kara`.`reply_headers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kara`.`reply_headers` (
  `reply_id` BIGINT(20) NOT NULL,
  `headers` VARCHAR(255) NULL DEFAULT NULL,
  `headers_key` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`reply_id`, `headers_key`))
  ENGINE = MyISAM
  DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `kara`.`request`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kara`.`request` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `body` LONGTEXT NULL DEFAULT NULL,
  `method` INT(11) NULL DEFAULT NULL,
  `mqtt` BIT(1) NOT NULL,
  `protocol` VARCHAR(255) NULL DEFAULT NULL,
  `request_time` DATETIME NULL DEFAULT NULL,
  `bin_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK9acuxqw13ffbi1bqrs8axshga` (`bin_id` ASC) )
  ENGINE = MyISAM
  DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `kara`.`request_headers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kara`.`request_headers` (
  `request_id` BIGINT(20) NOT NULL,
  `headers` TEXT NULL DEFAULT NULL,
  `headers_key` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`request_id`, `headers_key`))
  ENGINE = MyISAM
  DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `kara`.`request_metric`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kara`.`request_metric` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `bin_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK1m4gb0ajjg5vqsw68yrgkro2n` (`bin_id` ASC) )
  ENGINE = MyISAM
  DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `kara`.`request_metric_counts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kara`.`request_metric_counts` (
  `request_metric_id` BIGINT(20) NOT NULL,
  `counts` INT(11) NULL DEFAULT NULL,
  `counts_key` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`request_metric_id`, `counts_key`))
  ENGINE = MyISAM
  DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `kara`.`request_query_params`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `kara`.`request_query_params` (
  `request_id` BIGINT(20) NOT NULL,
  `query_params` VARCHAR(255) NULL DEFAULT NULL,
  `query_params_key` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`request_id`, `query_params_key`))
  ENGINE = MyISAM
  DEFAULT CHARACTER SET = latin1;