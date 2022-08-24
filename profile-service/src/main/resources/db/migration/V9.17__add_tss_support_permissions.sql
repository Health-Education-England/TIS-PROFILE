INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
    ('trainee-support:view',
     'PERSON',
     'Can view trainee support information',
     'tis:profile::user:',
     'tis:people::person:',
     'View',
     'Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES ('TSS Support Admin','trainee-support:view')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;
