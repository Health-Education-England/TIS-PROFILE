drop table if exists role_permissions;
drop table if exists permission;
drop table if exists user_roles;
drop table if exists role;
drop table if exists user;


CREATE TABLE Role (
  name varchar(50) primary key
);

CREATE TABLE Permission (
  name varchar(50)primary key
);

CREATE TABLE IF NOT EXISTS HeeUser (
  name varchar(50) NOT NULL primary key,
  firstName varchar(50) NOT NULL,
  lastName varchar(50) NOT NULL,
  gmcId varchar(7) NOT NULL,
  designatedBodyCode varchar(20) NOT NULL,
  phoneNumber varchar(10)
);

CREATE TABLE RolePermission (
  roleName varchar(50) NOT NULL,
  permissionName varchar(50) NOT NULL,
  PRIMARY KEY (roleName,permissionName),
  KEY fk_rolepermissions_permission_namex (permissionName),
  CONSTRAINT fk_rolepermissions_role FOREIGN KEY (roleName) REFERENCES Role (name) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_rolepermissions_permission FOREIGN KEY (permissionName) REFERENCES Permission (name) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE UserRole (
  userName varchar(50) NOT NULL,
  roleName varchar(50) NOT NULL,
  PRIMARY KEY (userName,roleName),
  KEY fk_userrole_role_namex (roleName),
  CONSTRAINT fk_userrole_heeuser FOREIGN KEY (userName) REFERENCES HeeUser (name) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_userrole_role FOREIGN KEY (roleName) REFERENCES Role (name) ON DELETE CASCADE ON UPDATE CASCADE
);

insert IGNORE into HeeUser (name,firstName,lastName,gmcId,designatedBodyCode,phoneNumber) values ('jamesh','James','Hudson','1000000','1-85KJU0','7788996655');

insert IGNORE into Permission (name) values ('revalidation:see:dbc:trainees');
insert IGNORE into Permission (name) values ('revalidation:change:add:note');
insert IGNORE into Permission (name) values ('revalidation:submit:to:ro:review');
insert IGNORE into Permission (name) values ('revalidation:submit:to:gmc');
insert IGNORE into Permission (name) values ('revalidation:assign:admin');
insert IGNORE into Permission (name) values ('revalidation:data:sync');

insert IGNORE into Permission (name) values ('concerns:register:trainee');
insert IGNORE into Permission (name) values ('concerns:see:dbc:concerns');
insert IGNORE into Permission (name) values ('concerns:change:add:concern');
insert IGNORE into Permission (name) values ('concerns:see:trainee:concerns');

insert IGNORE into Role (name) values ('RVAdmin');
insert IGNORE into Role (name) values ('ConcernsAdmin');

insert IGNORE into RolePermission (roleName,permissionName) values ('RVAdmin','revalidation:see:dbc:trainees');
insert IGNORE into RolePermission (roleName,permissionName) values ('RVAdmin','revalidation:change:add:note');
insert IGNORE into RolePermission (roleName,permissionName) values ('RVAdmin','revalidation:submit:to:ro:review');
insert IGNORE into RolePermission (roleName,permissionName) values ('RVAdmin','revalidation:submit:to:gmc');
insert IGNORE into RolePermission (roleName,permissionName) values ('RVAdmin','revalidation:assign:admin');
insert IGNORE into RolePermission (roleName,permissionName) values ('RVAdmin','concerns:see:trainee:concerns');

insert IGNORE into RolePermission (roleName,permissionName) values ('ConcernsAdmin','concerns:see:dbc:concerns');
insert IGNORE into RolePermission (roleName,permissionName) values ('ConcernsAdmin','concerns:change:add:concern');

insert IGNORE into UserRole (userName,roleName) values ('jamesh','RVAdmin');
insert IGNORE into UserRole (userName,roleName) values ('jamesh','ConcernsAdmin');
