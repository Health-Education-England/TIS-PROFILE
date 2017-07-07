INSERT INTO `Permission` (`name`)
VALUES
('curriculum:add:modify'),
('curriculum:view'),
('curriculum:bulk:add:modify');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
('ETL', 'curriculum:add:modify'),
('ETL', 'curriculum:view'),
('ETL', 'curriculum:bulk:add:modify');