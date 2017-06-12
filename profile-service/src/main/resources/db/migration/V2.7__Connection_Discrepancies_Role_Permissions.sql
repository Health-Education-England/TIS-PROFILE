INSERT INTO `Role` (`name`)
VALUES
	('ConnectionDiscrepanciesManager');

INSERT INTO `Permission` (`name`)
VALUES
('connection:discrepancies:view'),
('connection:discrepancies:manage');


INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
('ConnectionDiscrepanciesManager', 'connection:discrepancies:view'),
('ConnectionDiscrepanciesManager', 'connection:discrepancies:manage');