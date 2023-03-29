INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
('BulkUploadAdmin','personsensitive:view:entities'),
('BulkUploadAdmin','personsensitive:add:modify:entities');

INSERT INTO `HeeUser` (`name`,`lastName`,`emailAddress`,`active`)
VALUES ('bulk_upload','','dev.test@transformuk.com',1)
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO UserRole VALUES ('bulk_upload','BulkUploadAdmin')
ON DUPLICATE KEY UPDATE `userName` = `userName`;
