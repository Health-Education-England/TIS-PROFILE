INSERT INTO Permission (name, type, description,principal,resource,actions,effect) VALUES ('person:view', 'Person', 'Can view people','tis:profile::user:','tis:people:-:person','View','Allow');
INSERT INTO Permission (name, type, description,principal,resource,actions,effect) VALUES ('person:add:modify', 'Person', 'Can create and modify people','tis:profile::user:','tis:people:-:person','Create,Update','Allow');
INSERT INTO Permission (name, type, description,principal,resource,actions,effect) VALUES ('person:bulk:add:modify', 'Person', 'Used by the ETL to upsert intrepid data in bulk','tis:profile::user:','tis:people::person','Create,Update','Allow');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
('ETL', 'person:add:modify'),
('ETL', 'person:bulk:add:modify'),
('ETL', 'person:view');

INSERT INTO `PermissionRequires` (`permission`, `requires`, `reason`)
VALUES
	('person:bulk:add:modify', 'person:view', 'User must be able to view people in order to change them.'),
	('person:bulk:add:modify', 'person:add:modify', 'User must be able to modify one people in order to bulk modify them.'),
	('person:add:modify', 'person:view', 'User must be able to view people in order to change them.');