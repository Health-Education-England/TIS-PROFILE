INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
       ('HEE Programme Observer', 'curriculum:view'),
       ('HEE Programme Observer', 'programme:view'),
       ('HEE Programme Observer', 'tcs:view:entities'),
       ('HEE Programme Observer', 'specialty:view')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;