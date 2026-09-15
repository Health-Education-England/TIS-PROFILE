INSERT INTO `RolePermission`(`roleName`, `permissionName`)
VALUES ('HEE Programme Admin', 'trainee-formr:view')
        ON DUPLICATE KEY
UPDATE `roleName` = `roleName`,`permissionName` = `permissionName`;
