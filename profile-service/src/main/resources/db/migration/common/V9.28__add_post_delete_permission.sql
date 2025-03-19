INSERT INTO `Role` (`name`)
VALUES ('PostManagement')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
    ('post:delete:posts',
     'POST',
     'Can delete posts',
     'tis:profile::user:',
     'post:delete:posts:',
     'Delete',
     'Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
    ('PostManagement','post:delete:posts')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;
