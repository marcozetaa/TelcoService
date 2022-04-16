CREATE TABLE `subscribe_product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_subscribe` int NOT NULL,
  `name_product` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `product2_fk` (`name_product`),
  KEY `subscribe_fk` (`id_subscribe`),
  CONSTRAINT `subscribe_fk` FOREIGN KEY (`id_subscribe`) REFERENCES `subscription` (`id`),
  CONSTRAINT `product2_fk` FOREIGN KEY (`name_product`) REFERENCES `product` (`name`)
)