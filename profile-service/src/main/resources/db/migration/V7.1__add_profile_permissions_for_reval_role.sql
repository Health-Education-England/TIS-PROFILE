INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
('HEE Admin Revalidation','profile:get:ro:user'),
('HEE Admin Revalidation','profile:get:users')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;