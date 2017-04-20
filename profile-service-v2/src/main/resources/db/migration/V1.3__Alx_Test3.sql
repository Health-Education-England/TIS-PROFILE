CREATE TABLE `HeeUser` (
  `name` varchar(100) NOT NULL,
  `firstName` varchar(70) DEFAULT NULL,
  `lastName` varchar(50) NOT NULL,
  `gmcId` varchar(7) DEFAULT NULL,
  `phoneNumber` varchar(20) DEFAULT NULL,
  `emailAddress` varchar(100) NOT NULL,
  `active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;