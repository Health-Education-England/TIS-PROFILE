INSERT INTO `Role` (`name`)
VALUES
('ConcernsObserver'),
('PCSAdmin'),
('PCSObserver'),
('PPAdmin'),
('PPObserver'),
('RVManager'),
('RVObserver')
ON DUPLICATE KEY UPDATE `name` = `name`;


INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
('ConcernsObserver','concerns:see:dbc:concerns'),
('RVManager','concerns:see:trainee:concerns'),
('RVObserver','concerns:see:trainee:concerns'),
('PCSAdmin','curriculum:add:modify'),
('PCSAdmin','curriculum:view'),
('PCSObserver','curriculum:view'),
('PersonAdmin','person:add:modify'),
('PersonAdmin','person:view'),
('PPAdmin','post:add:modify'),
('PPAdmin','post:view'),
('PPObserver','post:view'),
('RVManager','profile:get:users'),
('PCSAdmin','programme:add:modify'),
('PCSAdmin','programme:view'),
('PCSObserver','programme:view'),
('RVManager','revalidation:assign:admin'),
('RVManager','revalidation:change:add:note'),
('RVManager','revalidation:manual:archive'),
('RVManager','revalidation:see:dbc:trainees'),
('RVObserver','revalidation:see:dbc:trainees'),
('RVManager','revalidation:submit:on:behalf:of:ro'),
('RVManager','revalidation:submit:to:ro:review'),
('PCSAdmin','specialty-group:add:modify'),
('PCSAdmin','specialty-group:view'),
('PCSObserver','specialty-group:view'),
('PCSAdmin','specialty:add:modify'),
('PCSAdmin','specialty:view'),
('PCSObserver','specialty:view'),
('PCSObserver','tcs:view:entities'),
('PPObserver','tcs:view:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;