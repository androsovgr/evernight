-- MySQL dump 10.13  Distrib 5.7.18, for Win64 (x86_64)
--
-- Host: localhost    Database: evernight
-- ------------------------------------------------------
-- Server version	5.7.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ent_classifier`
--

DROP TABLE IF EXISTS `ent_classifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ent_classifier` (
  `CLAS_PK` bigint(20) NOT NULL COMMENT 'Первичный ключ',
  `CLAS_LABEL` varchar(45) NOT NULL COMMENT 'Название классификатора',
  `CLAS_CLAS_PK` bigint(20) DEFAULT NULL COMMENT 'Ссылка на родительский классификатор',
  PRIMARY KEY (`CLAS_PK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ent_classifier`
--

LOCK TABLES `ent_classifier` WRITE;
/*!40000 ALTER TABLE `ent_classifier` DISABLE KEYS */;
INSERT INTO `ent_classifier` VALUES (1,'Доступно к заказу',NULL),(2,'Чаи',1),(3,'Бутылочные напитки',1),(4,'Кальяны',1);
/*!40000 ALTER TABLE `ent_classifier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ent_item`
--

DROP TABLE IF EXISTS `ent_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ent_item` (
  `ITEM_PK` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Первичный ключ',
  `ITEM_LABEL` varchar(45) NOT NULL COMMENT 'Название',
  `ITEM_CLAS_PK` bigint(20) NOT NULL COMMENT 'Ссылка на классификатор',
  `ITEM_TYPE` int(1) NOT NULL COMMENT '0 - основное блюдо. 1 - опция к основному блюду',
  `ITEM_ACTIVE` int(1) NOT NULL COMMENT '0 - доступно к заказу. 1 - выведено из употребления',
  `ITEM_PRICE` decimal(10,0) NOT NULL COMMENT 'Стоимость за единицу',
  PRIMARY KEY (`ITEM_PK`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ent_item`
--

LOCK TABLES `ent_item` WRITE;
/*!40000 ALTER TABLE `ent_item` DISABLE KEYS */;
INSERT INTO `ent_item` VALUES (1,'Каркаде',2,0,0,200),(2,'Dark Side',4,0,0,600),(3,'Черный шоколад',2,0,0,200);
/*!40000 ALTER TABLE `ent_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ent_order`
--

DROP TABLE IF EXISTS `ent_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ent_order` (
  `ORD_PK` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Первичный ключ',
  `ORD_TBL_PK` bigint(20) NOT NULL COMMENT 'Ссылка на стол',
  `ORD_WAITER_USR_PK` bigint(20) NOT NULL COMMENT 'Ссылка на официанта',
  `ORD_OPENED` datetime NOT NULL COMMENT 'Дата и время, в которые стол был открыт',
  `ORD_CLOSED` datetime DEFAULT NULL COMMENT 'Дата и время, в которые стол был закрыт',
  `ORD_COMMENT` varchar(2000) DEFAULT NULL COMMENT 'Комментарий к заказу',
  PRIMARY KEY (`ORD_PK`),
  KEY `ENFK_ORD_USR_idx` (`ORD_WAITER_USR_PK`),
  KEY `ENFK_ORD_TBL_idx` (`ORD_TBL_PK`),
  CONSTRAINT `ENFK_ORD_TBL` FOREIGN KEY (`ORD_TBL_PK`) REFERENCES `ent_table` (`TBL_PK`),
  CONSTRAINT `ENFK_ORD_USR` FOREIGN KEY (`ORD_WAITER_USR_PK`) REFERENCES `ent_user` (`USR_PK`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ent_order`
--

LOCK TABLES `ent_order` WRITE;
/*!40000 ALTER TABLE `ent_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `ent_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ent_order_item`
--

DROP TABLE IF EXISTS `ent_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ent_order_item` (
  `ORIT_PK` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Первичный ключ',
  `ORIT_TIME` datetime DEFAULT NULL COMMENT 'Время, в которое было заказано блюдо',
  `ORIT_ORD_PK` bigint(20) NOT NULL COMMENT 'Ссылка на заказ',
  `ORIT_ITEM_PK` bigint(20) NOT NULL COMMENT 'Ссылка на заказанный продукт',
  PRIMARY KEY (`ORIT_PK`),
  KEY `ENFK_ORIT_ORD_idx` (`ORIT_ORD_PK`),
  KEY `ENFK_ORIT_ITEM_idx` (`ORIT_ITEM_PK`),
  CONSTRAINT `ENFK_ORIT_ITEM` FOREIGN KEY (`ORIT_ITEM_PK`) REFERENCES `ent_item` (`ITEM_PK`),
  CONSTRAINT `ENFK_ORIT_ORD` FOREIGN KEY (`ORIT_ORD_PK`) REFERENCES `ent_order` (`ORD_PK`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ent_order_item`
--

LOCK TABLES `ent_order_item` WRITE;
/*!40000 ALTER TABLE `ent_order_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `ent_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ent_order_item_options`
--

DROP TABLE IF EXISTS `ent_order_item_options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ent_order_item_options` (
  `ORIO_PK` bigint(20) NOT NULL,
  `ORIO_ORIT_PK` bigint(20) NOT NULL,
  `ORIO_ITEM_PK` bigint(20) NOT NULL,
  PRIMARY KEY (`ORIO_PK`),
  KEY `ENFK_ORIO_ITEM_idx` (`ORIO_ITEM_PK`),
  KEY `ENFK_ORIO_ORIT_idx` (`ORIO_ORIT_PK`),
  CONSTRAINT `ENFK_ORIO_ITEM` FOREIGN KEY (`ORIO_ITEM_PK`) REFERENCES `ent_item` (`ITEM_PK`),
  CONSTRAINT `ENFK_ORIO_ORIT` FOREIGN KEY (`ORIO_ORIT_PK`) REFERENCES `ent_order_item` (`ORIT_PK`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ent_order_item_options`
--

LOCK TABLES `ent_order_item_options` WRITE;
/*!40000 ALTER TABLE `ent_order_item_options` DISABLE KEYS */;
/*!40000 ALTER TABLE `ent_order_item_options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ent_table`
--

DROP TABLE IF EXISTS `ent_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ent_table` (
  `TBL_PK` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Первичный ключ',
  `TBL_LABEL` varchar(45) NOT NULL COMMENT 'Название столика',
  PRIMARY KEY (`TBL_PK`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ent_table`
--

LOCK TABLES `ent_table` WRITE;
/*!40000 ALTER TABLE `ent_table` DISABLE KEYS */;
INSERT INTO `ent_table` VALUES (1,'1'),(2,'2'),(3,'3'),(4,'4'),(5,'5'),(6,'6'),(7,'7'),(8,'8'),(9,'9');
/*!40000 ALTER TABLE `ent_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ent_user`
--

DROP TABLE IF EXISTS `ent_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ent_user` (
  `USR_PK` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Первичный ключ',
  `USR_EMAIL` varchar(45) NOT NULL COMMENT 'Электронная почта (логин)',
  `USR_PASSWORD_HASH` varchar(45) NOT NULL COMMENT 'Хэш пароля пользователя',
  `USR_SURNAME` varchar(45) NOT NULL COMMENT 'Фамилия',
  `USR_NAME` varchar(45) NOT NULL COMMENT 'Имя',
  `USR_MIDDLENAME` varchar(45) DEFAULT NULL COMMENT 'Отчество',
  `USR_ROLE` int(11) NOT NULL COMMENT '0 - официант. 1 - менеджер. 2 - администратор',
  `USR_LOCKED` int(11) NOT NULL COMMENT '0 - разблокирован. 1 - заблокирован',
  PRIMARY KEY (`USR_PK`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ent_user`
--

LOCK TABLES `ent_user` WRITE;
/*!40000 ALTER TABLE `ent_user` DISABLE KEYS */;
INSERT INTO `ent_user` VALUES (-1,'admin','pgF8pejz4nBLqCI9rbN3pA==','Транспортный','Администритор','',2,0);
/*!40000 ALTER TABLE `ent_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-01 17:21:32
