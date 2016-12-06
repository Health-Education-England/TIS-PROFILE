insert IGNORE into Role (name) values ('ETL');

insert IGNORE into Permission (name) values ('trainee-id:register:trainee');
insert IGNORE into Permission (name) values ('trainee-id:view:all:mappings');
insert IGNORE into Permission (name) values ('revalidation:register:trainee');
insert IGNORE into Permission (name) values ('concerns:register:trainee');
insert IGNORE into Permission (name) values ('concerns:data:sync');

insert IGNORE into RolePermission (roleName,permissionName) values ('ETL','trainee-id:register:trainee');
insert IGNORE into RolePermission (roleName,permissionName) values ('ETL','trainee-id:view:all:mappings');
insert IGNORE into RolePermission (roleName,permissionName) values ('ETL','revalidation:register:trainee');
insert IGNORE into RolePermission (roleName,permissionName) values ('ETL','revalidation:data:sync');
insert IGNORE into RolePermission (roleName,permissionName) values ('ETL','concerns:register:trainee');
insert IGNORE into RolePermission (roleName,permissionName) values ('ETL','concerns:data:sync');


insert IGNORE into HeeUser (name,designatedBodyCode) values ('gmc_etl','DUMMY-GMC');
insert IGNORE into HeeUser (name,designatedBodyCode) values ('core_etl','DUMMY-CORE');

insert IGNORE into UserRole (userName,roleName) values ('gmc_etl','ETL');
insert IGNORE into UserRole (userName,roleName) values ('core_etl','ETL');