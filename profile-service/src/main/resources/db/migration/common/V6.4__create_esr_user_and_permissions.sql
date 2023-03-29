INSERT INTO HeeUser
VALUES ('esr_kc_user',NULL,'',NULL,NULL,'fake_esr_email@fake.com',1);

-- Data for table Role
INSERT INTO Role VALUES ('ESR');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
('ESR','personsensitive:view:entities'),
('ESR','tcs:view:entities'),
('ESR','assessment:view:entities'),
('ESR','programme:view'),
('ESR','profile:view:entities'),
('ESR','post:view'),
('ESR','person:view'),
('ESR','curriculum:view');

INSERT INTO UserRole VALUES ('esr_kc_user','ESR');

