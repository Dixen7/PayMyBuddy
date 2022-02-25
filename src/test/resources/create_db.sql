USE pay_my_buddy_test;

-- -----------------------------------------------------
-- Table `pay_my_buddy_test`.`bank_account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_my_buddy_test.bank_account (
    bank_account_id 	BIGINT NOT NULL 	AUTO_INCREMENT,
    account_number 	    VARCHAR(11) NULL 	DEFAULT NULL,
    bic 				VARCHAR(11) NULL 	DEFAULT NULL,
    holder 			    VARCHAR(20) NULL 	DEFAULT NULL,
    iban 				VARCHAR(34) NULL 	DEFAULT NULL,
    PRIMARY KEY (bank_account_id));


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`user_buddy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_my_buddy_test.user (
    user_id         BIGINT                  NOT NULL AUTO_INCREMENT,
    active          BIT(1)                  NOT NULL,
    email 			VARCHAR(254)            NOT NULL,
    first_name 		VARCHAR(25)             NULL DEFAULT NULL,
    last_name 		VARCHAR(25)             NULL DEFAULT NULL,
    password 		VARCHAR(255)            NOT NULL,
    bank_account_id	BIGINT                  NULL DEFAULT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (bank_account_id)
    REFERENCES pay_my_buddy_test.bank_account (bank_account_id));


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_my_buddy_test.account (
    account_id  	BIGINT NOT NULL 	AUTO_INCREMENT,
    balance 		DECIMAL(19,2) NULL 	DEFAULT NULL,
    user_id 		BIGINT NULL		 	DEFAULT NULL,
    PRIMARY KEY (account_id),
    FOREIGN KEY (user_id)
    REFERENCES pay_my_buddy_test.user (user_id));


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`connexion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_my_buddy_test.connexion (
    user_id 			BIGINT NOT NULL,
    user_associate_id   BIGINT NOT NULL,
    PRIMARY KEY (user_id, user_associate_id),
    FOREIGN KEY (user_associate_id)
    REFERENCES pay_my_buddy_test.user (user_id),
    FOREIGN KEY (user_id)
    REFERENCES pay_my_buddy_test.user (user_id));


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_my_buddy_test.role (
    role_id 	BIGINT NOT NULL 	AUTO_INCREMENT,
    name 		VARCHAR(255) NULL 	DEFAULT NULL,
    PRIMARY KEY (role_id));


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_my_buddy_test.transaction (
    transaction_id 			    BIGINT NOT NULL 	AUTO_INCREMENT,
    amount 					    DECIMAL(19,2) NULL 	DEFAULT NULL,
    date_transaction 			DATE NULL 			DEFAULT NULL,
    description 				VARCHAR(100) NULL 	DEFAULT NULL,
    fee 						DECIMAL(19,2) NULL 	DEFAULT NULL,
    type 						VARCHAR(255) NULL 	DEFAULT NULL,
    account_beneficiary_id 	    BIGINT NULL 		DEFAULT NULL,
    account_sender_id 		    BIGINT NULL 		DEFAULT NULL,
    PRIMARY KEY (transaction_id),
    FOREIGN KEY (account_beneficiary_id)
    REFERENCES pay_my_buddy_test.account (user_id),
    FOREIGN KEY (account_sender_id)
    REFERENCES pay_my_buddy_test.account (user_id));


-- -----------------------------------------------------
-- Table `pay_my_buddy`.`users_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_my_buddy_test.users_roles (
    user_id 	BIGINT 	NOT NULL,
    role_id 	BIGINT 	NOT NULL,
    PRIMARY KEY(user_id, role_id));

ALTER TABLE users_roles
ADD CONSTRAINT fk_user_id
FOREIGN KEY (user_id)
REFERENCES user (user_id)
ON DELETE CASCADE
ON UPDATE CASCADE,
ADD CONSTRAINT fk_role_id
FOREIGN KEY (role_id)
REFERENCES role (role_id)
ON DELETE CASCADE
ON UPDATE CASCADE;
