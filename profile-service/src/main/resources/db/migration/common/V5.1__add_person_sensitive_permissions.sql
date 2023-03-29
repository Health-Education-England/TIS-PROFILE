INSERT INTO `Role` (`name`)
VALUES
('SuperUser')
ON DUPLICATE KEY UPDATE `name` = `name`;


INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
('SuperUser','personsensitive:view:entities'),
('SuperUser','personsensitive:add:modify:entities')
ON DUPLICATE KEY UPDATE `permissionName` = `permissionName`;