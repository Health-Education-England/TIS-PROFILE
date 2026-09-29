DELETE FROM `RolePermission`
WHERE `permissionName` = 'curriculum:add:modify'
  AND `roleName` IN (
                     'HEE Trust Admin',
                     'HEE Programme Admin',
                     'HEE Admin',
                     'HEE Admin Sensitive',
                     'HEE Admin Revalidation'
                    );
