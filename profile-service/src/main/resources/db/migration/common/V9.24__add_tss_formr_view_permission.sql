INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
    ('trainee-formr:view',
     'PERSON',
     'Can view submitted trainee form-r',
     'tis:profile::user:',
     'tis:people::person:',
     'View',
     'Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
    ('HEE Admin','trainee-formr:view'),
    ('HEE Admin Revalidation','trainee-formr:view'),
    ('HEE Admin Sensitive','trainee-formr:view'),
    ('HEE TIS Admin','trainee-formr:view')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;
