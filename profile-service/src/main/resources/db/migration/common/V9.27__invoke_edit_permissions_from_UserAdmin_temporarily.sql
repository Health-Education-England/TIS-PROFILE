DELETE FROM `RolePermission`
WHERE `roleName` = 'HEE User Admin'
  AND `permissionName` IN ('heeuser:add:modify', 'heeuser:delete');
