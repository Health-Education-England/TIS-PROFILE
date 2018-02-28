ALTER TABLE `Permission` DROP PRIMARY KEY, ADD PRIMARY KEY(`name`, `type`);

INSERT INTO `Role` (`name`) VALUES ('BulkUploadAdmin');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
('BulkUploadAdmin', 'person:add:modify'),
('BulkUploadAdmin', 'curriculum:view'),
('BulkUploadAdmin', 'programme:view');

INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
('curriculum:view', 'BULK_UPLOAD', 'Can view curricula', 'tis:profile::user:bulk_upload', 'tis:curricula:-:curriculum:', 'View', 'Allow'),
('programme:view', 'BULK_UPLOAD', 'Can view programme', 'tis:profile::user:bulk_upload', 'tis:programmes::programme:', 'View', 'Allow'),
('person:add:modify', 'BULK_UPLOAD', 'Can create and modify people', 'tis:profile::user:bulk_upload', 'tis:people::person:', 'Create,Update', 'Allow');


