DELETE FROM `RolePermission`
WHERE `permissionName` IN (
  'concerns:change:add:concern',
  'concerns:data:sync',
  'concerns:register:trainee',
  'concerns:see:dbc:concerns',
  'concerns:see:trainee:concerns',
  'connection:discrepancies:manage',
  'connection:discrepancies:view',
  'revalidation:assign:admin',
  'revalidation:change:add:note',
  'revalidation:data:sync',
  'revalidation:manual:archive',
  'revalidation:provide:recommendation',
  'revalidation:register:trainee',
  'revalidation:see:dbc:report',
  'revalidation:submit:on:behalf:of:ro',
  'revalidation:submit:to:gmc',
  'revalidation:submit:to:ro:review'
);

DELETE FROM `PermissionRequires`
WHERE `permission` IN (
  'concerns:change:add:concern',
  'connection:discrepancies:manage',
  'revalidation:assign:admin',
  'revalidation:change:add:note',
  'revalidation:provide:recommendation',
  'revalidation:submit:on:behalf:of:ro',
  'revalidation:submit:to:gmc',
  'revalidation:submit:to:ro:review'
);

DELETE FROM `Permission`
WHERE `name` IN (
  'concerns:change:add:concern',
  'concerns:data:sync',
  'concerns:register:trainee',
  'concerns:see:dbc:concerns',
  'concerns:see:trainee:concerns',
  'connection:discrepancies:manage',
  'connection:discrepancies:view',
  'revalidation:assign:admin',
  'revalidation:change:add:note',
  'revalidation:data:sync',
  'revalidation:manual:archive',
  'revalidation:provide:recommendation',
  'revalidation:register:trainee',
  'revalidation:see:dbc:report',
  'revalidation:submit:on:behalf:of:ro',
  'revalidation:submit:to:gmc',
  'revalidation:submit:to:ro:review'
);

DELETE FROM `UserRole`
WHERE `roleName` IN (
  'ConcernsAdmin',
  'ConcernsObserver',
  'ConnectionDiscrepanciesManager',
  'RVAdmin',
  'RVManager',
  'RVObserver',
  'RevalBeta'
);

DELETE FROM `Role`
WHERE `name` IN (
  'ConcernsAdmin',
  'ConcernsObserver',
  'ConnectionDiscrepanciesManager',
  'RVAdmin',
  'RVManager',
  'RVObserver',
  'RevalBeta'
);
