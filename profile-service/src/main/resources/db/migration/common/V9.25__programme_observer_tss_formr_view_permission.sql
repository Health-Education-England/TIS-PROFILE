INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
    ('HEE Programme Observer','trainee-formr:view')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;
