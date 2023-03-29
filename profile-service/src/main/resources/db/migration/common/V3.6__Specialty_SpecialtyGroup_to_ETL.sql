INSERT INTO `Permission` (`name`,`type`,`description`)
VALUES
('specialty:add:modify','PCS','Can create and modify specialty'),
('specialty:view','PCS','Can view specialty'),
('specialty:bulk:add:modify','INTERNAL','Used by the ETL to upsert intrepid data in bulk'),
('specialty-group:add:modify','PCS','Can create and modify specialtyGroup'),
('specialty-group:view','PCS','Can view specialtyGroup'),
('specialty-group:bulk:add:modify','INTERNAL','Used by the ETL to upsert intrepid data in bulk');


INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
('ETL', 'specialty:add:modify'),
('ETL', 'specialty:view'),
('ETL', 'specialty:bulk:add:modify'),
('ETL', 'specialty-group:add:modify'),
('ETL', 'specialty-group:view'),
('ETL', 'specialty-group:bulk:add:modify');