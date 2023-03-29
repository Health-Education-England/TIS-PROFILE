INSERT INTO `Role` (`name`)
VALUES
('HEE TIS Admin')
ON DUPLICATE KEY UPDATE `name` = `name`;

UPDATE `RolePermission`
SET `roleName` = 'HEE TIS Admin'
WHERE `roleName` = 'TIS Admin';

UPDATE `UserRole`
SET `roleName` = 'HEE TIS Admin'
WHERE `roleName` = 'TIS Admin';

UPDATE `UserRole`
SET `roleName` = 'HEE TIS Admin'
WHERE `roleName` = 'TisAdmin';

INSERT INTO `RolePermission`
VALUES
('HEE TIS Admin','notification:change:add:notification')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;

DELETE FROM `RolePermission` WHERE `roleName` = 'TisAdmin';

DELETE FROM `Role` where `name` = 'TIS Admin';
DELETE FROM `Role` where `name` = 'TisAdmin';