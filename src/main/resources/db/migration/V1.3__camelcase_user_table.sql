drop table if exists role_permissions;
drop table if exists permission;
drop table if exists user_roles;
drop table if exists role;
drop table if exists user;


CREATE TABLE role (
  name varchar(50) primary key
);

CREATE TABLE permission (
  name varchar(50)primary key
);

CREATE TABLE IF NOT EXISTS user (
  name varchar(50) NOT NULL primary key,
  firstName varchar(50) NOT NULL,
  lastName varchar(50) NOT NULL,
  gmcId varchar(7) NOT NULL,
  designatedBodyCode varchar(20) NOT NULL,
  phoneNumber varchar(10)
);

CREATE TABLE role_permissions (
  role_name varchar(50) NOT NULL,
  permission_name varchar(50) NOT NULL,
  PRIMARY KEY (role_name,permission_name),
  KEY fk_rolepermissions_permission_namex (permission_name),
  CONSTRAINT fk_rolepermissions_role FOREIGN KEY (role_name) REFERENCES role (name) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_rolepermissions_permission FOREIGN KEY (permission_name) REFERENCES permission (name) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE user_roles (
  user_name varchar(50) NOT NULL,
  role_name varchar(50) NOT NULL,
  PRIMARY KEY (user_name,role_name),
  KEY fk_userroles_role_namex (role_name),
  CONSTRAINT fk_userroles_user FOREIGN KEY (user_name) REFERENCES user (name) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_userroles_role FOREIGN KEY (role_name) REFERENCES role (name) ON DELETE CASCADE ON UPDATE CASCADE
);

insert IGNORE into user (name,firstName,lastName,gmcId,designatedBodyCode,phoneNumber) values ('jamesh','James','Hudson','1000000','1-85KJU0','7788996655');

insert IGNORE into permission (name) values ('revalidation:see:dbc:trainees');
insert IGNORE into permission (name) values ('revalidation:change:add:note');
insert IGNORE into permission (name) values ('revalidation:submit:to:ro:review');
insert IGNORE into permission (name) values ('revalidation:submit:to:gmc');
insert IGNORE into permission (name) values ('revalidation:assign:admin');
insert IGNORE into permission (name) values ('revalidation:data:sync');

insert IGNORE into permission (name) values ('concerns:register:trainee');
insert IGNORE into permission (name) values ('concerns:see:dbc:concerns');
insert IGNORE into permission (name) values ('concerns:change:add:concern');

insert IGNORE into role (name) values ('RVAdmin');
insert IGNORE into role (name) values ('ConcernsAdmin');

insert IGNORE into role_permissions (role_name,permission_name) values ('RVAdmin','revalidation:see:dbc:trainees');
insert IGNORE into role_permissions (role_name,permission_name) values ('RVAdmin','revalidation:change:add:note');
insert IGNORE into role_permissions (role_name,permission_name) values ('RVAdmin','revalidation:submit:to:ro:review');
insert IGNORE into role_permissions (role_name,permission_name) values ('RVAdmin','revalidation:submit:to:gmc');
insert IGNORE into role_permissions (role_name,permission_name) values ('RVAdmin','revalidation:assign:admin');

insert IGNORE into role_permissions (role_name,permission_name) values ('ConcernsAdmin','concerns:see:dbc:concerns');
insert IGNORE into role_permissions (role_name,permission_name) values ('ConcernsAdmin','concerns:change:add:concern');

insert IGNORE into user_roles (user_name,role_name) values ('jamesh','RVAdmin');
insert IGNORE into user_roles (user_name,role_name) values ('jamesh','ConcernsAdmin');
