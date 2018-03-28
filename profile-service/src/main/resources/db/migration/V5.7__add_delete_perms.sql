INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
  ('Hee Admin','assessment:delete:entities'),
  ('Hee Admin','tcs:delete:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;

INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
  ('Tis Admin','assessment:delete:entities'),
  ('Tis Admin','reference:delete:entities'),
  ('Tis Admin','tcs:delete:entities'),
  ('Tis Admin','profile:delete:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;

INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
  ('Hee Admin','assessment:delete:entities'),
  ('Hee Admin','tcs:delete:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;

INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
  ('Hee Admin','assessment:delete:entities'),
  ('Hee Admin','tcs:delete:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;