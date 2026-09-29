INSERT INTO Role (name)
VALUES ('Reference Observer'),
       ('Reference Observer')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO RolePermission(roleName, permissionName)
VALUES ('Reference Observer', 'adminmenu:view:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`,`permissionName` = `permissionName`;