USE `promotion` ;

-- -----------------------------------------------------
-- Table `promotion`.`rp`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `promotion`.`test` (
  `id` VARCHAR(255) NOT NULL,
  `rp_id` VARCHAR(255) NULL,
  `wallet_id` VARCHAR(255) NULL,
  `usr_id` VARCHAR(255) NULL,
  `holdings` INT NULL COMMENT 'total point earned',
  `status` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  KEY `rp_ptcpnt_x01` (`rp_id` ASC, `usr_id` ASC),
  UNIQUE KEY `rp_ptcpnt_x02` (`rp_id` ASC, `wallet_id` ASC))
ENGINE = InnoDB;

#SET SESSION tx_isolation='READ-COMMITTED';
#SET SESSION tx_isolation='REPEATABLE-READ';
SHOW VARIABLES LIKE 'tx_isolation';