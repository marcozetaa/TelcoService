CREATE TABLE `salesReport` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `id_package` int DEFAULT NULL,
  `PackageName` varchar(45) DEFAULT NULL,
  `TotalPurchase` int DEFAULT '0',
  `TotalFor12` int DEFAULT '0',
  `TotalFor24` int DEFAULT '0',
  `TotalFor36` int DEFAULT '0',
  `NetValue` decimal(10,0) DEFAULT '0',
  `TotalValue` decimal(10,0) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `sales_package_idx` (`id_package`),
  CONSTRAINT `sales_package` FOREIGN KEY (`id_package`) REFERENCES `packages` (`id`)
)