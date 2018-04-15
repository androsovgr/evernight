CREATE TABLE `evernight`.`ent_consumption` (
  `cons_pk` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'Первичный ключ',
  `cons_info` VARCHAR(45) NOT NULL COMMENT 'Краткая информация о расходе',
  `cons_date` DATE NOT NULL COMMENT 'Дата расхода',
  `cons_money` DECIMAL(10,0) NOT NULL COMMENT 'величина расходов',
  `cons_comment` VARCHAR(1000) NULL COMMENT 'Комментарий, содержащий полную информацию о расходах',
  `cons_type` INT NOT NULL COMMENT 'Тип расходов. 0 - операционные расходы. 1 - улучшение',
  PRIMARY KEY (`cons_pk`));
