-- new permissions for reference
INSERT INTO `Permission` (`name`)
VALUES
('reference:add:modify:entities'), ('reference:delete:entities');

-- new roles for reference
INSERT INTO `Role` (`name`)
VALUES
('ReferenceAdmin');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
('ReferenceAdmin', 'reference:add:modify:entities'), ('ReferenceAdmin', 'reference:delete:entities');
