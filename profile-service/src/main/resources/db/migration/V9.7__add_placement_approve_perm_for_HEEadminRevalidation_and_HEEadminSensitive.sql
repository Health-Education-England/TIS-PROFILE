INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
      ('HEE Admin Revalidation', 'placement:approve'),
      ('HEE Admin Sensitive', 'placement:approve')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;