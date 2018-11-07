INSERT INTO `Role` (`name`)
VALUES
       ('HEE Programme Observer')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
       ('HEE Programme Observer', 'person:view'),
       ('HEE Programme Observer', 'post:view'),
       ('HEE Programme Observer', 'assessment:view:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;

-- create a table that links users to prgrammes
CREATE TABLE `UserProgramme` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `heeUser` varchar(255) NOT NULL,
    `programmenId` bigint (20) NOT NULL,
    `programmenName` varchar(255) NOT NULL,
    `programmeNumber` varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
  	KEY `fk_userprogramme_heeuserx` (`heeUser`),
  	CONSTRAINT `fk_userprogramme_heeuserx` FOREIGN KEY (`heeUser`) REFERENCES `HeeUser` (`name`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
