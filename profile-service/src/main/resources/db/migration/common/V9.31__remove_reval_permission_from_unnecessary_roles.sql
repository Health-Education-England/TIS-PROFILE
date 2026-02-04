DELETE FROM `RolePermission`
WHERE `permissionName` = 'revalidation:see:dbc:trainees'
AND `roleName` in ('HEE TIS Admin', 'RVOfficer', 'SuperUser'); -- SuperUser is for Stage env only
