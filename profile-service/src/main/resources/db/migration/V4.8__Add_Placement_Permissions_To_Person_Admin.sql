INSERT INTO `Role` VALUES('PersonAdmin') ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
('PersonAdmin','tcs:view:entities'),
('PersonAdmin','tcs:add:modify:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`,`permissionName` = `permissionName`
