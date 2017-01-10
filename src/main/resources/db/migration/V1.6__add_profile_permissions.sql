insert IGNORE into Permission (name) values ('profile:get:users');
insert IGNORE into Permission (name) values ('profile:get:ro:user');

insert IGNORE into RolePermission (roleName,permissionName) values ('RVAdmin','profile:get:users');
insert IGNORE into RolePermission (roleName,permissionName) values ('RVOfficer','profile:get:users');
insert IGNORE into RolePermission (roleName,permissionName) values ('RVAdmin','profile:get:ro:user');
