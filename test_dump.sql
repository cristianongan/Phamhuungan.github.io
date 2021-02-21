-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: localhost    Database: datn
-- ------------------------------------------------------
-- Server version	8.0.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'men\'s'),(2,'women\'s'),(3,'kid\'s');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category2`
--

DROP TABLE IF EXISTS `category2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category2` (
  `id_1` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `for_ca_cat2` (`id_1`),
  CONSTRAINT `for_ca_cat2` FOREIGN KEY (`id_1`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category2`
--

LOCK TABLES `category2` WRITE;
/*!40000 ALTER TABLE `category2` DISABLE KEYS */;
INSERT INTO `category2` VALUES (1,1,'quần'),(1,2,'áo'),(2,3,'quần'),(2,4,'áo'),(3,5,'Body Suit');
/*!40000 ALTER TABLE `category2` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `info`
--

DROP TABLE IF EXISTS `info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `info` (
  `id` int NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  `firstname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `Lastname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `DOB` date DEFAULT NULL,
  `Address` varchar(50) DEFAULT NULL,
  `city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `additional_information` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `homephone` varchar(11) DEFAULT NULL,
  `mobilephone` varchar(10) DEFAULT NULL,
  KEY `id_info` (`id`),
  CONSTRAINT `id_info` FOREIGN KEY (`id`) REFERENCES `user` (`id`),
  CONSTRAINT `info_chk_1` CHECK ((`title` in (_utf8mb4'mr',_utf8mb4'mrs',_utf8mb4'miss')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `info`
--

LOCK TABLES `info` WRITE;
/*!40000 ALTER TABLE `info` DISABLE KEYS */;
INSERT INTO `info` VALUES (11,'Mr','pham','ngan','1999-01-11','a','n','a','0327944481','0327944481'),(12,'Mr','pham','ngan','1999-01-11','Giao Tất, Kim Sơn, Gia Lâm','Hà Nội','a','0327944481','0327944481'),(13,'Mr','pham','ngan','1999-01-11','a','n','a','0327944481','0327944481'),(14,'Mr','pham','ngan','1999-01-11','a','n','a','0327944481','0327944481'),(15,'Mr','pham','ngan','1999-01-11','a','n','a','0327944481','0327944481'),(16,'Mr','pham','ngan','1999-01-11','a','n','a','0327944481','0327944481'),(18,'Mr','pham','ngan','1999-01-13','a','n','a','0327944481','0327944481'),(19,'Mr','pham','ngan','1999-01-13','a','n','a','0327944481','0327944481'),(21,'Mrs','pham','ngan','1999-01-11','thÃ´n Giao Táº¥t a','Huyá»n Gia LÃ¢m','a','0327944481','0327944481'),(22,'Mr','pháº¡m','ngÃ¢n','1999-01-11','thÃ´n Giao Táº¥t a','Huyá»n Gia LÃ¢m','a','0327944481','0327944481'),(1,'Mr','Phạm Hữu','Ngân','1999-07-03','Giao Tất - Kim Sơn - Gia Lâm','Hà Nội','admin','0327944481','0327944481');
/*!40000 ALTER TABLE `info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `max_id_product`
--

DROP TABLE IF EXISTS `max_id_product`;
/*!50001 DROP VIEW IF EXISTS `max_id_product`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `max_id_product` AS SELECT 
 1 AS `max(id)`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `num_user`
--

DROP TABLE IF EXISTS `num_user`;
/*!50001 DROP VIEW IF EXISTS `num_user`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `num_user` AS SELECT 
 1 AS `num`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `order_ma`
--

DROP TABLE IF EXISTS `order_ma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_ma` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user` int DEFAULT NULL,
  `created` date DEFAULT NULL,
  `addr` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `status` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'Đóng gói',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_ma`
--

LOCK TABLES `order_ma` WRITE;
/*!40000 ALTER TABLE `order_ma` DISABLE KEYS */;
INSERT INTO `order_ma` VALUES (1,12,'2021-01-21','Kim son','Đang giao hàng'),(2,12,'2021-01-21','an','Đóng gói'),(3,12,'2021-01-21','an','Đóng gói'),(4,12,'2021-01-21','an','Đóng gói'),(5,12,'2021-01-21','an','Đóng gói'),(6,12,'2021-01-21','an','Đóng gói'),(7,12,'2021-01-21','an','Đóng gói'),(8,12,'2021-01-21','an','Đóng gói'),(9,12,'2021-01-21','an','Đóng gói'),(10,12,'2021-01-21','an','Đóng gói'),(12,12,'2021-01-21','an','Đóng gói'),(14,12,'2021-01-21','an','Đóng gói'),(15,12,'2021-01-22','Giao Tất, Kim Sơn, Gia LâmHà Nội','Đóng gói'),(16,12,'2021-01-22','Giao Tất, Kim Sơn, Gia LâmHà Nội','Đóng gói');
/*!40000 ALTER TABLE `order_ma` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_pd`
--

DROP TABLE IF EXISTS `order_pd`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_pd` (
  `stt` int NOT NULL AUTO_INCREMENT,
  `id` int NOT NULL,
  `nameofproduct` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `price` varchar(50) NOT NULL,
  `numofproduct` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`stt`),
  KEY `id_order_pd` (`id`),
  CONSTRAINT `id_order_pd` FOREIGN KEY (`id`) REFERENCES `order_ma` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_pd`
--

LOCK TABLES `order_pd` WRITE;
/*!40000 ALTER TABLE `order_pd` DISABLE KEYS */;
INSERT INTO `order_pd` VALUES (1,4,'áo kiểu tay lửng-color:white-size:S','499000',1),(2,5,'áo sơ mi casual tay dài-color:white-size:S','499000',1),(3,6,'áo kiểu tay lửng-color:white-size:S','499000',4),(4,6,'áo sơ mi casual tay dài-color:white-size:S','499000',1),(5,7,'áo kiểu tay lửng-color:white-size:S','499000',4),(6,8,'áo kiểu tay lửng-color:white-size:S','499000',4),(7,8,'áo sơ mi casual tay dài-color:white-size:S','499000',1),(8,9,'áo kiểu tay lửng-color:white-size:S','499000',4),(9,9,'áo sơ mi casual tay dài-color:white-size:S','499000',1),(10,10,'áo kiểu tay lửng-color:white-size:S','499000',3),(12,12,'áo sơ mi supima co dãn-color:white-size:S','499000',2),(14,14,'áo kiểu tay lửng-color:white-size:S','499000',3),(15,15,'áo sơ mi cổ bẻ kẻ caro-color:brown-size:S','499000',1),(16,15,'áo sơ mi supima co dãn-color:white-size:M','499000',1),(17,16,'áo sơ mi vải-color:white-blue-size:S','499000',3),(18,16,'Body Suit Nous Không tất-color:yewlow-size:9-12M','175000',1);
/*!40000 ALTER TABLE `order_pd` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `color` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `material` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `price` varchar(50) DEFAULT NULL,
  `urlimg` varchar(1000) DEFAULT NULL,
  `numofproduct` int NOT NULL DEFAULT '0',
  `category` int NOT NULL,
  `category2` int NOT NULL,
  `note` varchar(20000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `size` varchar(50) NOT NULL DEFAULT 'S,M,L,XL',
  PRIMARY KEY (`id`),
  KEY `for_ca_pro` (`category`),
  KEY `for_ca2_pro` (`category2`),
  CONSTRAINT `for_ca2_pro` FOREIGN KEY (`category2`) REFERENCES `category2` (`id`),
  CONSTRAINT `for_ca_pro` FOREIGN KEY (`category`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'áo len cổ lỗ','white','cotton acrylic','150000','imagesP/aolencolo.jpg',12,1,2,NULL,'S,M,L,XL'),(2,'áo len cổ lỗ','white','cotton acrylic','150000','aolencolo',12,1,2,NULL,'S,M,L,XL'),(3,'áo len cổ lỗ','white','cotton acrylic','150000','aolencolo',12,1,2,NULL,'S,M,L,XL'),(4,'áo len cổ lỗ','white','cotton acrylic','150000','aolencolo',12,1,2,NULL,'S,M,L,XL'),(5,'áo len cổ lỗ','white','cotton acrylic','150000','aolencolo',12,1,2,NULL,'S,M,L,XL'),(6,'áo len cổ lỗ','white','cotton acrylic','150000','aolencolo',12,1,2,NULL,'S,M,L,XL'),(7,'áo kiểu tay lửng','white','rayon','499000','imagesP/aokieutaylung.jpg',12,1,2,NULL,'S,M,L,XL'),(8,'áo len cổ lỗ','white','cotton acrylic','150000','imagesP/aolencolo.jpg',12,2,2,NULL,'S,M,L,XL'),(9,'áo ngắn tay','white','rayon','499000','imagesP/aongantay.jpg',12,2,2,NULL,'S,M,L,XL'),(10,'áo ngắn tay','white','rayon','499000','imagesP/aongantay.jpg',12,2,4,NULL,'S,M,L,XL'),(11,'áo kiểu tay lửng','white','rayon','499000','imagesP/aokieutaylung.jpg',12,2,4,NULL,'S,M,L,XL'),(12,'áo sơ mi casual tay dài','white','rayon','499000','imagesP/aocasualtaydai.jpg',12,2,4,'sản phẩm phù hợp cho những bạn nữ trẻ trung năng động. Chất liệu bền đẹp giá cả phải trăng, đây là sự lựa chọn hoàn hảo cho nơi công sở hay những buổi đi chơi ngắn','S,M,L,XL'),(13,'áo sơ mi cổ bẻ kẻ caro','dark blue','rayon','499000','imagesP/aosomicobekecaro.jpg',12,2,4,NULL,'S,M,L,XL'),(14,'áo sơ mi cổ bẻ kẻ caro','brown','rayon','499000','imagesP/aosomicobekecaronau.jpg',12,2,4,NULL,'S,M,L,XL'),(15,'áo sơ mi supima co dãn','white','rayon','499000','imagesP/aosomisupimacodan.jpg',12,2,4,NULL,'S,M,L,XL'),(16,'áo sơ mi vải','white-blue','rayon','499000','imagesP/aosomivai.jpg',12,2,4,NULL,'S,M,L,XL'),(17,'Body Suit Nous Không tất','yewlow','vải cao cấp','175000','imagesP/bodysuitNouskhongtat9M.jpg',12,3,5,'Bộ sưu tập thu đông BST- Thu Đông 2020 Nous  Chất liệu ấm áp nhưng thoáng khí, gây kích ứng da bé.\n\n- Nhẹ nhàng nhưng giữ ấm hiệu quả\n\n- Phong cách hiện đại, đa dạng kiểu dáng, màu sắc\n\n- Thiết kế đơn giản nhưng không kém phần tinh tế.\n\nXuất xứ: Việt Nam\n\nThương hiệu: Nous','0-3M,3-6M,6-9M,9-12M,12-18M,18-24M'),(18,'Body Suit Nous Không tất','blue','vải cao cấp','175000','imagesP/27806_e0064d3a3e07c05999161.jpg',50,3,5,'Bộ sưu tập thu đông BST- Thu Đông 2020 Nous  Chất liệu ấm áp nhưng thoáng khí, gây kích ứng da bé.\n\n- Nhẹ nhàng nhưng giữ ấm hiệu quả\n\n- Phong cách hiện đại, đa dạng kiểu dáng, màu sắc\n\n- Thiết kế đơn giản nhưng không kém phần tinh tế.\n\nXuất xứ: Việt Nam\n\nThương hiệu: Nous','0-3M,3-6M,6-9M,9-12M,12-18M,18-24M'),(20,'Phạm Hữu Ngân','đỏ','nhựa','10000','imagesP\\img',2,1,1,NULL,'x,l'),(21,'Phạm Hữu Ngân','đỏ','nhựa','10000','imagesP\\img',2,1,1,NULL,'x,l'),(22,'Phạm Hữu Ngân','đỏ','nhựa','10000','imagesP\\img',2,1,1,NULL,'x,l');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `row_num_cat`
--

DROP TABLE IF EXISTS `row_num_cat`;
/*!50001 DROP VIEW IF EXISTS `row_num_cat`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `row_num_cat` AS SELECT 
 1 AS `cat`,
 1 AS `num`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `row_num_cat2`
--

DROP TABLE IF EXISTS `row_num_cat2`;
/*!50001 DROP VIEW IF EXISTS `row_num_cat2`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `row_num_cat2` AS SELECT 
 1 AS `cat`,
 1 AS `cat2`,
 1 AS `num`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `row_num_order`
--

DROP TABLE IF EXISTS `row_num_order`;
/*!50001 DROP VIEW IF EXISTS `row_num_order`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `row_num_order` AS SELECT 
 1 AS `r`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','1'),(2,'phn','1'),(3,'phn111','1'),(4,'u','p'),(5,'phn2','12345'),(8,'phn21','12345'),(10,'phn211','12345'),(11,'phn2113','12345'),(12,'n1111','12345'),(13,'n11111','12345'),(14,'n2222','12345'),(15,'n22221','12345'),(16,'n222211','12345'),(18,'n222211q','12345'),(19,'n222211q1','12345'),(21,'phamhuungan','12345'),(22,'phamnganq','1');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `max_id_product`
--

/*!50001 DROP VIEW IF EXISTS `max_id_product`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `max_id_product` AS select count(`product`.`id`) AS `max(id)` from `product` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `num_user`
--

/*!50001 DROP VIEW IF EXISTS `num_user`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `num_user` AS select count(`user`.`id`) AS `num` from `user` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `row_num_cat`
--

/*!50001 DROP VIEW IF EXISTS `row_num_cat`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `row_num_cat` AS select `category`.`id` AS `cat`,count(`product`.`id`) AS `num` from (`category` left join `product` on((`product`.`category` = `category`.`id`))) group by `category`.`id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `row_num_cat2`
--

/*!50001 DROP VIEW IF EXISTS `row_num_cat2`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `row_num_cat2` AS select `category2`.`id_1` AS `cat`,`category2`.`id` AS `cat2`,count(`product`.`id`) AS `num` from (`category2` left join `product` on(((`product`.`category2` = `category2`.`id`) and (`product`.`category` = `category2`.`id_1`)))) group by `category2`.`id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `row_num_order`
--

/*!50001 DROP VIEW IF EXISTS `row_num_order`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `row_num_order` AS select count(`order_ma`.`id`) AS `r` from `order_ma` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-02-21 11:50:03
