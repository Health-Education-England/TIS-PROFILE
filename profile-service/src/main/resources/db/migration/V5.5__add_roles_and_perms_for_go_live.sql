INSERT INTO `Role` VALUES('HEE Admin') ON DUPLICATE KEY UPDATE `name` = `name`;
INSERT INTO `Role` VALUES('HEE Admin Sensitive') ON DUPLICATE KEY UPDATE `name` = `name`;
INSERT INTO `Role` VALUES('HEE Admin Revalidation') ON DUPLICATE KEY UPDATE `name` = `name`;
INSERT INTO `Role` VALUES('TIS Admin') ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `Permission` (`name`,`type`,`description`,`principal`,`resource`,`actions`,`effect`)
VALUES
  ('adminmenu:view:entities','REFERENCE','Can view admin menu data','tis:profile::user:','tis:reference::reference:','View','Allow'),
  ('adminmenu:add:modify:entities','REFERENCE','Can add or modify admin menu data','tis:profile::user:','tis:reference::reference:','Create,Update','Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
('HEE Admin','assessment:add:modify:entities'),
('HEE Admin','assessment:view:entities'),
('HEE Admin','curriculum:add:modify'),
('HEE Admin','curriculum:view'),
('HEE Admin','person:add:modify'),
('HEE Admin','person:view'),
('HEE Admin','post:add:modify'),
('HEE Admin','post:view'),
('HEE Admin','programme:add:modify'),
('HEE Admin','programme:view'),
('HEE Admin','specialty-group:add:modify'),
('HEE Admin','specialty-group:view'),
('HEE Admin','specialty:add:modify'),
('HEE Admin','specialty:view'),
('HEE Admin','tcs:add:modify:entities'),
('HEE Admin','tcs:view:entities')

ON DUPLICATE KEY UPDATE `roleName` = `roleName`,`permissionName` = `permissionName`;

INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
('TIS Admin','assessment:add:modify:entities'),
('TIS Admin','assessment:view:entities'),
('TIS Admin','curriculum:add:modify'),
('TIS Admin','curriculum:view'),
('TIS Admin','person:add:modify'),
('TIS Admin','person:view'),
('TIS Admin','post:add:modify'),
('TIS Admin','post:view'),
('TIS Admin','programme:add:modify'),
('TIS Admin','programme:view'),
('TIS Admin','specialty-group:add:modify'),
('TIS Admin','specialty-group:view'),
('TIS Admin','specialty:add:modify'),
('TIS Admin','specialty:view'),
('TIS Admin','tcs:add:modify:entities'),
('TIS Admin','tcs:view:entities'),
('TIS Admin','concerns:change:add:concern'),
('TIS Admin','concerns:see:dbc:concerns'),
('TIS Admin','connection:discrepancies:manage'),
('TIS Admin','connection:discrepancies:view'),
('TIS Admin','notification:change:add:notification'),
('TIS Admin','profile:add:modify:entities'),
('TIS Admin','profile:view:entities'),
('TIS Admin','reference:add:modify:entities'),
('TIS Admin','revalidation:assign:admin'),
('TIS Admin','revalidation:change:add:note'),
('TIS Admin','revalidation:manual:archive'),
('TIS Admin','revalidation:provide:recommendation'),
('TIS Admin','revalidation:see:dbc:report'),
('TIS Admin','revalidation:see:dbc:trainees'),
('TIS Admin','revalidation:submit:on:behalf:of:ro'),
('TIS Admin','revalidation:submit:to:gmc'),
('TIS Admin','revalidation:submit:to:ro:review'),
('TIS Admin','adminmenu:view:entities'),
('TIS Admin','adminmenu:add:modify:entities')

ON DUPLICATE KEY UPDATE `roleName` = `roleName`,`permissionName` = `permissionName`;


INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
('HEE Admin Sensitive','assessment:add:modify:entities'),
('HEE Admin Sensitive','assessment:view:entities'),
('HEE Admin Sensitive','curriculum:add:modify'),
('HEE Admin Sensitive','curriculum:view'),
('HEE Admin Sensitive','person:add:modify'),
('HEE Admin Sensitive','person:view'),
('HEE Admin Sensitive','post:add:modify'),
('HEE Admin Sensitive','post:view'),
('HEE Admin Sensitive','programme:add:modify'),
('HEE Admin Sensitive','programme:view'),
('HEE Admin Sensitive','specialty-group:add:modify'),
('HEE Admin Sensitive','specialty-group:view'),
('HEE Admin Sensitive','specialty:add:modify'),
('HEE Admin Sensitive','specialty:view'),
('HEE Admin Sensitive','tcs:add:modify:entities'),
('HEE Admin Sensitive','tcs:view:entities'),
('HEE Admin Sensitive','personsensitive:view:entities'),
('HEE Admin Sensitive','personsensitive:add:modify:entities')

ON DUPLICATE KEY UPDATE `roleName` = `roleName`,`permissionName` = `permissionName`;

INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
('HEE Admin Reval','assessment:add:modify:entities'),
('HEE Admin Reval','assessment:view:entities'),
('HEE Admin Reval','curriculum:add:modify'),
('HEE Admin Reval','curriculum:view'),
('HEE Admin Reval','person:add:modify'),
('HEE Admin Reval','person:view'),
('HEE Admin Reval','post:add:modify'),
('HEE Admin Reval','post:view'),
('HEE Admin Reval','programme:add:modify'),
('HEE Admin Reval','programme:view'),
('HEE Admin Reval','specialty-group:add:modify'),
('HEE Admin Reval','specialty-group:view'),
('HEE Admin Reval','specialty:add:modify'),
('HEE Admin Reval','specialty:view'),
('HEE Admin Reval','tcs:add:modify:entities'),
('HEE Admin Reval','tcs:view:entities'),
('HEE Admin Reval','concerns:change:add:concern'),
('HEE Admin Reval','concerns:see:dbc:concerns'),
('HEE Admin Reval','connection:discrepancies:manage'),
('HEE Admin Reval','connection:discrepancies:view'),
('HEE Admin Reval','revalidation:assign:admin'),
('HEE Admin Reval','revalidation:change:add:note'),
('HEE Admin Reval','revalidation:manual:archive'),
('HEE Admin Reval','revalidation:provide:recommendation'),
('HEE Admin Reval','revalidation:see:dbc:report'),
('HEE Admin Reval','revalidation:see:dbc:trainees'),
('HEE Admin Reval','revalidation:submit:on:behalf:of:ro')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`,`permissionName` = `permissionName`;
