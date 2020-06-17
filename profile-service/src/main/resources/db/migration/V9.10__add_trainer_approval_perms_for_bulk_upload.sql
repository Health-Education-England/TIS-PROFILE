INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
      ('BulkUploadAdmin','trainerapproval:view'),
      ('BulkUploadAdmin','trainerapproval:add:modify')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;
