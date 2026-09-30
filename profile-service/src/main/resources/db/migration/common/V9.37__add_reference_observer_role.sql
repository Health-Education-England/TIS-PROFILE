INSERT INTO Role (name)
VALUES ('Reference Observer'),
       ('Reference Observer')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO Permission(name, effect, description, type, resource, principal, actions)
VALUES ('reference:view:entities', 'Allow', 'Can view reference data',
        'REFERENCE', 'tis:reference::entity:', 'tis:reference::user:', 'View')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO RolePermission(roleName, permissionName)
VALUES ('Reference Observer', 'reference:view:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`,`permissionName` = `permissionName`;