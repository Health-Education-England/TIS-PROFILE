LOCK TABLES
    HeeUser WRITE,
    Permission WRITE,
    Role WRITE,
    RolePermission WRITE,
    UserRole WRITE;

ALTER TABLE RolePermission DROP FOREIGN KEY fk_rolepermissions_role;
ALTER TABLE RolePermission DROP FOREIGN KEY fk_rolepermissions_permission;

ALTER TABLE UserRole DROP FOREIGN KEY fk_userrole_heeuser;
ALTER TABLE UserRole DROP FOREIGN KEY fk_userrole_role;

ALTER TABLE HeeUser MODIFY name VARCHAR(100);
ALTER TABLE HeeUser MODIFY firstName VARCHAR(70);
ALTER TABLE HeeUser MODIFY firstName VARCHAR(70);
ALTER TABLE HeeUser MODIFY phoneNumber VARCHAR(20);

ALTER TABLE Permission MODIFY name VARCHAR(100);

ALTER TABLE Role MODIFY name VARCHAR(100);

ALTER TABLE RolePermission MODIFY roleName VARCHAR(100);
ALTER TABLE RolePermission MODIFY permissionName VARCHAR(100);

ALTER TABLE UserRole MODIFY userName VARCHAR(100);
ALTER TABLE UserRole MODIFY roleName VARCHAR(100);

ALTER TABLE RolePermission ADD CONSTRAINT fk_rolepermissions_role FOREIGN KEY (roleName) REFERENCES Role (name) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE RolePermission ADD CONSTRAINT fk_rolepermissions_permission FOREIGN KEY (permissionName) REFERENCES Permission (name) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE UserRole ADD CONSTRAINT fk_userrole_heeuser FOREIGN KEY (userName) REFERENCES HeeUser (name) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE UserRole ADD CONSTRAINT fk_userrole_role FOREIGN KEY (roleName) REFERENCES Role (name) ON DELETE CASCADE ON UPDATE CASCADE;

UNLOCK TABLES;