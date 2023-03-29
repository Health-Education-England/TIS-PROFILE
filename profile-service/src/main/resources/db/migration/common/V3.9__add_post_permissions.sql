INSERT INTO Permission (name, type, description) VALUES ('post:view', 'PP', 'Can view posts');
INSERT INTO Permission (name, type, description) VALUES ('post:add:modify', 'PP', 'Can create and modify posts');
INSERT INTO Permission (name, type, description) VALUES ('post:bulk:add:modify', 'PP', 'Used by the ETL to upsert intrepid data in bulk');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
('ETL', 'post:add:modify'),
('ETL', 'post:bulk:add:modify'),
('ETL', 'post:view');

INSERT INTO `PermissionRequires` (`permission`, `requires`, `reason`)
VALUES
	('post:bulk:add:modify', 'post:view', 'User must be able to view posts in order to change them.'),
	('post:bulk:add:modify', 'post:add:modify', 'User must be able to modify one post in order to bulk modify them.'),
	('post:add:modify', 'post:view', 'User must be able to view posts in order to change them.');