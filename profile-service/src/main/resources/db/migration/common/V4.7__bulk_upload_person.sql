INSERT INTO `Role` (`name`) VALUES ('BulkUploadAdmin');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
('BulkUploadAdmin', 'person:add:modify'),
('BulkUploadAdmin', 'curriculum:view'),
('BulkUploadAdmin', 'programme:view');

