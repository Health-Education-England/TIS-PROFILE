INSERT INTO `Role` (`name`)
VALUES
       ('HEE Trust Observer')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
       ('HEE Trust Observer', 'curriculum:view'),
       ('HEE Trust Observer', 'person:view'),
       ('HEE Trust Observer', 'post:view'),
       ('HEE Trust Observer', 'programme:view'),
       ('HEE Trust Observer', 'specialty:view'),
       ('HEE Trust Observer', 'tcs:view:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;
