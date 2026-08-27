INSERT INTO Role (name)
VALUES ('Reference Trust Admin'),
       ('Reference Site Admin')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO Permission(name, effect, description, type, resource, principal, actions)
VALUES ('trust:add:modify:entities', 'Allow', 'Can create and modify trust reference data',
        'REFERENCE', 'tis:reference::trust:', 'tis:profile::user:', 'Create,Update'),
       ('site:add:modify:entities', 'Allow', 'Can create and modify site reference data',
        'REFERENCE', 'tis:reference::site:', 'tis:profile::user:', 'Create,Update')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO RolePermission(roleName, permissionName)
VALUES ('ETL', 'trust:add:modify:entities'),
       ('ETL', 'site:add:modify:entities'),
       ('HEE TIS Admin', 'trust:add:modify:entities'),
       ('HEE TIS Admin', 'site:add:modify:entities'),
       ('ReferenceAdmin', 'trust:add:modify:entities'),
       ('ReferenceAdmin', 'site:add:modify:entities'),
       ('Reference Site Admin', 'site:add:modify:entities'),
       ('Reference Trust Admin', 'trust:add:modify:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`,`permissionName` = `permissionName`;

