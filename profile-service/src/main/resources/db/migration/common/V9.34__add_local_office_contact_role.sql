INSERT INTO `Role`(`name`)
VALUES ('Reference Local Office Contact Admin') ON DUPLICATE KEY
UPDATE `name` = `name`;

INSERT INTO `Permission`(`name`, `effect`, `description`, `type`, `resource`, `principal`, `actions`)
VALUES ('local-office-contact:add:modify:entities', 'Allow',
        'Can create and modify local office contact reference data', 'REFERENCE',
        'tis:reference::local-office-contact:', 'tis:profile::user:',
        'Create,Update') ON DUPLICATE KEY
UPDATE `name` = `name`;

INSERT INTO `RolePermission`(`roleName`, `permissionName`)
VALUES ('ETL', 'local-office-contact:add:modify:entities'),
       ('HEE TIS Admin', 'local-office-contact:add:modify:entities'),
       ('ReferenceAdmin', 'local-office-contact:add:modify:entities'),
       ('Reference Local Office Contact Admin',
        'local-office-contact:add:modify:entities') ON DUPLICATE KEY
UPDATE `roleName` = `roleName`,`permissionName` = `permissionName`;
