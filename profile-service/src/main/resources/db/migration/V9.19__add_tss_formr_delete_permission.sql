INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
    ('trainee-formr:delete',
     'PERSON',
     'Can perform trainee form-r delete actions',
     'tis:profile::user:',
     'tis:people::person:',
     'Update',
     'Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES ('TSS Support Admin','trainee-formr:delete')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;
