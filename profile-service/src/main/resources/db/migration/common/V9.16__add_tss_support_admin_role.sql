INSERT INTO `Role` (`name`)
VALUES ('TSS Support Admin')
ON DUPLICATE KEY UPDATE `name` = `name`;