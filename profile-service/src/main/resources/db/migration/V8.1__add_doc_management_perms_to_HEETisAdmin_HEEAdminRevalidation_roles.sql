INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
  ('HEE TIS Admin','documentmanager:view:entities'),
  ('HEE TIS Admin','documentmanager:add:modify:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;

INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
  ('HEE Admin Revalidation','documentmanager:view:entities'),
  ('HEE Admin Revalidation','documentmanager:add:modify:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;