INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
      ('trainerapproval:view', 'PERSON', 'Can view trainer approvals', 'tis:profile::user:', 'tis:tcs::trainerapproval:', 'View', 'Allow'),
      ('trainerapproval:add:modify', 'PERSON', 'Can create and edit trainer approvals', 'tis:profile::user:', 'tis:tcs::trainerapproval:', 'Create,Update', 'Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;


INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
      ('HEE Admin Revalidation','trainerapproval:view'),
      ('HEE Admin Revalidation','trainerapproval:add:modify'),
      ('HEE Admin Sensitive','trainerapproval:view'),
      ('HEE Admin Sensitive','trainerapproval:add:modify'),
      ('HEE TIS Admin','trainerapproval:view'),
      ('HEE TIS Admin','trainerapproval:add:modify'),
      ('HEE Admin','trainerapproval:view'),
      ('HEE Admin','trainerapproval:add:modify')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;
