INSERT INTO `Permission` (`name`)
VALUES ('tcs:bulk:view:entities');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES ('ETL', 'tcs:bulk:view:entities');
