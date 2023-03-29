INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
('HEE Admin','documentmanager:view:entities'),
('HEE Admin','documentmanager:add:modify:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;