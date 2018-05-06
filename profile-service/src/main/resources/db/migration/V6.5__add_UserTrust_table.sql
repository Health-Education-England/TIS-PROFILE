-- create a table that links users to a trust

CREATE TABLE `UserTrust` (
    `id` bigint(20) NOT NULL,
    `heeUser` varchar(255) NOT NULL,
    `trustId` bigint (20) NOT NULL,
    `trustCode` varchar(255) NOT NULL,
    `trustName` varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
  	KEY `fk_usertrust_heeuserx` (`heeUser`),
  	CONSTRAINT `fk_usertrust_heeuser` FOREIGN KEY (`heeUser`) REFERENCES `HeeUser` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
