INSERT INTO `Role` (`name`)
VALUES ('TssSupportAdmin')
ON DUPLICATE KEY UPDATE `name` = `name`;