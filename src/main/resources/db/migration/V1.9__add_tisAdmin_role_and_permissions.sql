
insert IGNORE into Role (name) values ('TisAdmin');

insert IGNORE into Permission (name) values ('notification:change:add:notification');

insert IGNORE into RolePermission (roleName,permissionName) values ('TisAdmin','notification:change:add:notification');
