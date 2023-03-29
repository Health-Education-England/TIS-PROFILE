INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
('PersonAdmin','adminmenu:view:entities'),
('PersonAdmin','adminmenu:add:modify:entities'),
('PersonAdmin','personsensitive:view:entities'),
('PersonAdmin','personsensitive:add:modify:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;