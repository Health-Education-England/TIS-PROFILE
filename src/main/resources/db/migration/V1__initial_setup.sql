CREATE DATABASE IF NOT EXISTS auth;

CREATE TABLE IF NOT EXISTS role (
  id BIGINT NOT NULL AUTO_INCREMENT primary key,
  name varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS permission (
  id BIGINT NOT NULL AUTO_INCREMENT primary key,
  name varchar(50) NOT NULL
);
CREATE TABLE IF NOT EXISTS role_permissions (
  role_id BIGINT NOT NULL,
  permission_id BIGINT NOT NULL,
  PRIMARY KEY (role_id,permission_id),
  KEY fk_rolepermissions_permission_idx (permission_id),
  CONSTRAINT fk_rolepermissions_role FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_rolepermissions_permission FOREIGN KEY (permission_id) REFERENCES permission (id) ON DELETE CASCADE ON UPDATE CASCADE
);


insert IGNORE into role (name) values ('RVAdmin');
insert IGNORE into role (name) values ('RVOfficer');
insert IGNORE into role (name) values ('Trainee');


insert IGNORE into permission (name) values ('see:all:trainees');
insert IGNORE into permission (name) values ('see:ro:trainees');
insert IGNORE into permission (name) values ('action:trainee:workflow');
insert IGNORE into permission (name) values ('submit:to:ro');

insert IGNORE into permission (name) values ('submit:to:gmc');

insert IGNORE into permission (name) values ('contact:details:self');

insert IGNORE into role_permissions (role_id,permission_id) values (1,1);
insert IGNORE into role_permissions (role_id,permission_id) values (1,2);
insert IGNORE into role_permissions (role_id,permission_id) values (1,3);
insert IGNORE into role_permissions (role_id,permission_id) values (1,4);

insert IGNORE into role_permissions (role_id,permission_id) values (2,2);
insert IGNORE into role_permissions (role_id,permission_id) values (2,5);

insert IGNORE into role_permissions (role_id,permission_id) values (3,6);
