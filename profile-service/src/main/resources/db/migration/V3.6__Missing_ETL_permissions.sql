INSERT INTO `Permission` (`name`, `type`, `description`)
VALUES ('programme:bulk:add:modify', 'INTERNAL', 'Used by the ETL to upsert intrepid data in bulk');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES ('ETL', 'programme:bulk:add:modify');