INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
       ('heeuser:view', 'PERSON', 'Can view hee users in user management', 'tis:profile::user:', 'tis:um::heeuser:', 'View', 'Allow'),
       ('heeuser:add:modify', 'PERSON', 'Can create and edit hee users in user management', 'tis:profile::user:', 'tis:um::heeuser:', 'Create,Update', 'Allow'),
       ('heeuser:delete', 'PERSON', 'Can delete hee users from user management', 'tis:profile::user:', 'tis:um::heeuser:', 'Delete', 'Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;


INSERT INTO `Role` (`name`)
VALUES
       ('HEE User Admin'), ('HEE User Observer')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
       ('HEE User Admin','heeuser:view'),
       ('HEE User Admin','heeuser:add:modify'),
       ('HEE User Admin','heeuser:delete'),
       ('HEE User Observer','heeuser:view')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;