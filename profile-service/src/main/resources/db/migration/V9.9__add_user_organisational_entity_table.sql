-- create a table that links users to a organisationalEntity

CREATE TABLE `UserOrganisationalEntity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `heeUser` varchar(255) NOT NULL,
  `organisationalEntityId` bigint NOT NULL,
  `organisationalEntityName` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_userorganisationalentity_heeuser` (`heeUser`),
  CONSTRAINT `fk_userorganisationalentity_heeuser` FOREIGN KEY (`heeUser`) REFERENCES `HeeUser` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8;

INSERT INTO `UserOrganisationalEntity`
(`heeUser`, `organisationalEntityId`, `organisationalEntityName`)
SELECT name, 1, 'HEE' from  `HeeUser`;