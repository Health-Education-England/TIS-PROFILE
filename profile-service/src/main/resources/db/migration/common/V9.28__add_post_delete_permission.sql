INSERT INTO `Role` (`name`)
VALUES ('PostDelete')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
    ('post:delete',
     'POST',
     'Can delete posts',
     'tis:profile::user:',
     'tis:posts::post:',
     'Delete',
     'Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
    ('PostDelete','post:delete')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;
