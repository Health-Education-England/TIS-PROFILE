INSERT INTO `Role` (`name`)
VALUES ('TSS Data Admin')
ON DUPLICATE KEY UPDATE `name` = `name`;