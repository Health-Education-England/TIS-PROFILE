INSERT INTO `Permission` (`name`)
VALUES
	('programme:bulk:add:modify');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
('ETL', 'programme:bulk:add:modify');