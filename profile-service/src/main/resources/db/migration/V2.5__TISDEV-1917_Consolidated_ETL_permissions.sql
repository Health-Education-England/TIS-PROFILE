
INSERT INTO HeeUser
VALUES ('consolidated_etl',NULL,'',NULL,NULL,'fake_email@fake.com',1);

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES ('ETL', 'reference:add:modify:entities');

INSERT INTO UserRole VALUES ('consolidated_etl','ETL');