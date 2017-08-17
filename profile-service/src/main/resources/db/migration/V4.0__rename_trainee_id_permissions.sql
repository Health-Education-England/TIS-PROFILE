SET FOREIGN_KEY_CHECKS=0;

UPDATE `RolePermission`
SET `permissionName` = 'profile:register:trainee'
WHERE `permissionName` = 'trainee-id:register:trainee';

UPDATE `RolePermission`
SET `permissionName` = 'profile:view:all:mappings'
WHERE `permissionName` = 'trainee-id:view:all:mappings';

UPDATE `Permission`
SET `name` = 'profile:register:trainee'
WHERE `name` = 'trainee-id:register:trainee';

UPDATE `Permission`
SET `name` = 'profile:view:all:mappings'
WHERE `name` = 'trainee-id:view:all:mappings';

SET FOREIGN_KEY_CHECKS=1;