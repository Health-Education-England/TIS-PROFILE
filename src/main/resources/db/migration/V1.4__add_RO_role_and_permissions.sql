ALTER TABLE HeeUser ADD emailAddress VARCHAR(100);

insert IGNORE into Role (name) values ('RVOfficer');

insert IGNORE into Permission (name) values ('revalidation:submit:on:behalf:of:ro');

insert IGNORE into RolePermission (roleName,permissionName) values ('RVAdmin','revalidation:submit:on:behalf:of:ro');

delete from RolePermission where roleName = 'RVAdmin' and permissionName ='revalidation:submit:to:gmc';

insert IGNORE into RolePermission (roleName,permissionName) values ('RVOfficer','revalidation:see:dbc:trainees');
insert IGNORE into RolePermission (roleName,permissionName) values ('RVOfficer','revalidation:change:add:note');
insert IGNORE into RolePermission (roleName,permissionName) values ('RVOfficer','revalidation:submit:to:ro:review');
insert IGNORE into RolePermission (roleName,permissionName) values ('RVOfficer','revalidation:submit:to:gmc');
insert IGNORE into RolePermission (roleName,permissionName) values ('RVOfficer','revalidation:assign:admin');
insert IGNORE into RolePermission (roleName,permissionName) values ('RVOfficer','concerns:see:trainee:concerns');
