INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
    ('trainee-support:move',
     'PERSON',
     'Can move TSS data from one profile to another',
     'tis:profile::user:',
     'tis:people::person:',
     'Update',
     'Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
    ('TSS Data Admin','trainee-support:move')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;
