INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES ('TSS Data Admin','trainee-support:view')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES ('TSS Data Admin','trainee-support:modify')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES ('TSS Data Admin','trainee-formr:delete')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;