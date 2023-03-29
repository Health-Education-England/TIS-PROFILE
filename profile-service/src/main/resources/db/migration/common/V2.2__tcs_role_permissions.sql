-- Data for table Role
INSERT INTO Role VALUES ('TcsAdmin');

-- Data for table Permission
INSERT INTO Permission VALUES
('tcs:view:entities'),('tcs:add:modify:entities'),('tcs:delete:entities');

-- Data for table RolePermission
INSERT INTO RolePermission VALUES ('TcsAdmin','tcs:view:entities')
,('TcsAdmin','tcs:add:modify:entities')
,('TcsAdmin','tcs:delete:entities');
