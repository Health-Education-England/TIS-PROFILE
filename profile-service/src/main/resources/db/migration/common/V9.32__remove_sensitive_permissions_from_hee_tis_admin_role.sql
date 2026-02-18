DELETE FROM `RolePermission`
WHERE `permissionName` IN ('personsensitive:add:modify:entities', 'personsensitive:view:entities')
AND `roleName` IN ('HEE TIS Admin');
