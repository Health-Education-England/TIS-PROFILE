INSERT INTO `Role` (`name`)
VALUES
        ('HEE')
ON DUPLICATE KEY UPDATE `name` = `name`;


INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
        ('HEE','tcs:view:entities')
ON DUPLICATE KEY UPDATE `permissionName` = `permissionName`;


-- add HEE role for all the current users
INSERT INTO `UserRole` (`userName`, `roleName`)
SELECT `name`,
       'HEE'
FROM `HeeUser`
ON DUPLICATE KEY UPDATE `userName` = `userName`;
