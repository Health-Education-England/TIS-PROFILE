INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
('HEE Trust Admin','programme:view'),
('HEE Trust Admin','programme:add:modify'),
('HEE Trust Admin','curriculum:add:modify'),
('HEE Trust Admin','curriculum:view');


DELETE FROM `RolePermission` WHERE `roleName` = 'HEE Trust Admin' AND `permissionName` = 'assessment:view:entities';