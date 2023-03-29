INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
  ('PPAdmin','tcs:delete:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;