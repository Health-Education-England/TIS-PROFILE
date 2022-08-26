INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
    ('trainee-support:modify',
     'PERSON',
     'Can perform data modifying trainee support actions',
     'tis:profile::user:',
     'tis:people::person:',
     'Update',
     'Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES ('TSS Support Admin','trainee-support:modify')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;
