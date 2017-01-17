insert IGNORE into Permission (name) values ('revalidation:manual:archive');

insert IGNORE into RolePermission (roleName, permissionName) values ('RVAdmin','revalidation:manual:archive');
