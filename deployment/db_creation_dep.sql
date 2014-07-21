SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `LicenseBoxDB` ;
CREATE SCHEMA IF NOT EXISTS `LicenseBoxDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `LicenseBoxDB` ;

-- -----------------------------------------------------
-- Table `LicenseBoxDB`.`team`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LicenseBoxDB`.`team` ;

CREATE  TABLE IF NOT EXISTS `LicenseBoxDB`.`team` (
  `team_id` INT(11) NOT NULL ,
  `team_name` VARCHAR(45) NULL ,
  `team_manager` VARCHAR(12) NOT NULL ,
  PRIMARY KEY (`team_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `LicenseBoxDB`.`app_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LicenseBoxDB`.`app_user` ;

CREATE  TABLE IF NOT EXISTS `LicenseBoxDB`.`app_user` (
  `username` VARCHAR(12) NOT NULL ,
  `first_name` VARCHAR(45) NULL ,
  `last_name` VARCHAR(45) NULL ,
  `email` VARCHAR(100) NULL ,
  `password` VARCHAR(64) NOT NULL ,
  `team_id` INT(11) NULL ,
  PRIMARY KEY (`username`) ,
  INDEX `fk_team_id_idx` (`team_id` ASC) ,
  CONSTRAINT `fk_team_id`
    FOREIGN KEY (`team_id` )
    REFERENCES `LicenseBoxDB`.`team` (`team_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '	';


-- -----------------------------------------------------
-- Table `LicenseBoxDB`.`app_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LicenseBoxDB`.`app_role` ;

CREATE  TABLE IF NOT EXISTS `LicenseBoxDB`.`app_role` (
  `role_name` VARCHAR(15) NOT NULL ,
  `role_desc` VARCHAR(200) NULL ,
  PRIMARY KEY (`role_name`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `LicenseBoxDB`.`user_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LicenseBoxDB`.`user_role` ;

CREATE  TABLE IF NOT EXISTS `LicenseBoxDB`.`user_role` (
  `username` VARCHAR(12) NOT NULL ,
  `role_name` VARCHAR(15) NOT NULL ,
  INDEX `fk_User_Title_User1_idx` (`username` ASC) ,
  PRIMARY KEY (`username`, `role_name`) ,
  INDEX `fk_user_group_role1_idx` (`role_name` ASC) ,
  CONSTRAINT `fk_user_group_role1`
    FOREIGN KEY (`role_name` )
    REFERENCES `LicenseBoxDB`.`app_role` (`role_name` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_name1`
    FOREIGN KEY (`username` )
    REFERENCES `LicenseBoxDB`.`app_user` (`username` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `LicenseBoxDB`.`program`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LicenseBoxDB`.`program` ;

CREATE  TABLE IF NOT EXISTS `LicenseBoxDB`.`program` (
  `program_id` INT NOT NULL ,
  `program_name` VARCHAR(45) NULL ,
  `site_link` VARCHAR(100) NULL ,
  `version` VARCHAR(45) NULL ,
  PRIMARY KEY (`program_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `LicenseBoxDB`.`license`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LicenseBoxDB`.`license` ;

CREATE  TABLE IF NOT EXISTS `LicenseBoxDB`.`license` (
  `license_id` VARCHAR(15) NOT NULL ,
  `program_id` INT NOT NULL ,
  `username` VARCHAR(12) NULL ,
  `price` DECIMAL(10,2) NOT NULL ,
  `purchase_date` DATE NOT NULL ,
  `upgrade_date` DATE NULL ,
  `attachement` MEDIUMBLOB NULL ,
  INDEX `fk_program_id_idx` (`program_id` ASC) ,
  PRIMARY KEY (`license_id`) ,
  INDEX `fk_license_app_user1_idx` (`username` ASC) ,
  CONSTRAINT `fk_program_id`
    FOREIGN KEY (`program_id` )
    REFERENCES `LicenseBoxDB`.`program` (`program_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_license_app_user1`
    FOREIGN KEY (`username` )
    REFERENCES `LicenseBoxDB`.`app_user` (`username` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `LicenseBoxDB`.`license_history`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LicenseBoxDB`.`license_history` ;

CREATE  TABLE IF NOT EXISTS `LicenseBoxDB`.`license_history` (
  `license_id` VARCHAR(15) NOT NULL ,
  `username` VARCHAR(12) NOT NULL ,
  `start_date` DATE NOT NULL ,
  `end_date` DATE NULL ,
  PRIMARY KEY (`license_id`, `start_date`, `username`) ,
  INDEX `fl_license_id_idx` (`license_id` ASC) ,
  INDEX `fk_username_idx` (`username` ASC) ,
  CONSTRAINT `fk_license_id`
    FOREIGN KEY (`license_id` )
    REFERENCES `LicenseBoxDB`.`license` (`license_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_username`
    FOREIGN KEY (`username` )
    REFERENCES `LicenseBoxDB`.`app_user` (`username` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `LicenseBoxDB`.`user_id`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LicenseBoxDB`.`user_id` ;

-- CREATE  TABLE IF NOT EXISTS `LicenseBoxDB`.`user_id` (
--  `UID` INT NOT NULL ,
--  `username` VARCHAR(12) NOT NULL ,
--  PRIMARY KEY (`UID`) ,
--  INDEX `fk_username_idx` (`username` ASC) ,
--  CONSTRAINT `fk_username2`
--    FOREIGN KEY (`username` )
--    REFERENCES `LicenseBoxDB`.`app_user` (`username` )
--    ON DELETE NO ACTION
--    ON UPDATE NO ACTION)
-- ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `LicenseBoxDB`.`license_flow`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LicenseBoxDB`.`license_flow` ;

CREATE  TABLE IF NOT EXISTS `LicenseBoxDB`.`license_flow` (
  `request_id` INT NOT NULL AUTO_INCREMENT ,
  `username` VARCHAR(12) NOT NULL ,
  `program_id` INT NOT NULL ,
  `teamlead_approved` TINYINT(1) NOT NULL ,
  `licman_approved` TINYINT(1) NOT NULL ,
  `request_date` DATE NOT NULL ,
  PRIMARY KEY (`request_id`) ,
  INDEX `fk_lic_flow_username_idx` (`username` ASC) ,
  INDEX `fk_lic_flow_programid_idx` (`program_id` ASC) ,
  CONSTRAINT `fk_lic_flow_username`
    FOREIGN KEY (`username` )
    REFERENCES `LicenseBoxDB`.`app_user` (`username` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_lic_flow_programid`
    FOREIGN KEY (`program_id` )
    REFERENCES `LicenseBoxDB`.`program` (`program_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `LicenseBoxDB`.`purchase_flow`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `LicenseBoxDB`.`purchase_flow` ;

CREATE  TABLE IF NOT EXISTS `LicenseBoxDB`.`purchase_flow` (
  `purchase_id` INT NOT NULL AUTO_INCREMENT ,
  `program_id` INT(11) NOT NULL ,
  `username` VARCHAR(12) NOT NULL ,
  `manager_approved` TINYINT(1) NULL ,
  `purchase_date` VARCHAR(45) NULL ,
  `purchase_closed` TINYINT(1) NULL ,
  PRIMARY KEY (`purchase_id`) ,
  INDEX `fk_pur_program_id_idx` (`program_id` ASC) ,
  INDEX `fk_pur_usrname_idx` (`username` ASC) ,
  CONSTRAINT `fk_pur_program_id`
    FOREIGN KEY (`program_id` )
    REFERENCES `LicenseBoxDB`.`program` (`program_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pur_usrname`
    FOREIGN KEY (`username` )
    REFERENCES `LicenseBoxDB`.`app_user` (`username` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `LicenseBoxDB` ;

-- -----------------------------------------------------
-- Placeholder table for view `LicenseBoxDB`.`v_user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `LicenseBoxDB`.`v_user_role` (`username` INT, `password` INT, `role_name` INT);

-- -----------------------------------------------------
-- View `LicenseBoxDB`.`v_user_role`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `LicenseBoxDB`.`v_user_role` ;
DROP TABLE IF EXISTS `LicenseBoxDB`.`v_user_role`;
USE `LicenseBoxDB`;
CREATE  OR REPLACE VIEW `LicenseBoxDB`.`v_user_role` AS
SELECT u.username, u.password, g.role_name
FROM `user_role` ug
INNER JOIN `app_user` u ON u.username = ug.username
INNER JOIN `app_role` g ON g.role_name = ug.role_name;
;

SET SQL_MODE = '';
GRANT USAGE ON *.* TO app_usr;
 DROP USER app_usr;
SET SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';
CREATE USER 'app_usr' IDENTIFIED BY 'LicenseBox';

GRANT SELECT, INSERT, TRIGGER ON TABLE `LicenseBoxDB`.* TO 'app_usr';
GRANT SELECT, INSERT, TRIGGER, UPDATE, DELETE ON TABLE `LicenseBoxDB`.* TO 'app_usr';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
