
-- Table structure for table Permission
CREATE TABLE Permission (
  name varchar(100) NOT NULL,
  PRIMARY KEY (name)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data for table Permission
INSERT INTO Permission VALUES 
('concerns:change:add:concern'),('concerns:data:sync'),('concerns:register:trainee'),
('concerns:see:dbc:concerns'),('concerns:see:trainee:concerns'),('notification:change:add:notification'),
('profile:get:ro:user'),('profile:get:users'),('revalidation:assign:admin'),('revalidation:change:add:note'),
('revalidation:data:sync'),('revalidation:manual:archive'),('revalidation:register:trainee'),
('revalidation:see:dbc:trainees'),('revalidation:submit:on:behalf:of:ro'),('revalidation:submit:to:gmc'),
('revalidation:submit:to:ro:review'),('trainee-id:register:trainee'),('trainee-id:view:all:mappings');

-- Table structure for table Role
CREATE TABLE Role (
  name varchar(100) NOT NULL,
  PRIMARY KEY (name)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data for table Role
INSERT INTO Role VALUES ('ConcernsAdmin'),('ETL'),('RVAdmin'),('RVOfficer'),('TisAdmin');

-- Table structure for table RolePermission
CREATE TABLE RolePermission (
  roleName varchar(100) NOT NULL,
  permissionName varchar(100) NOT NULL,
  PRIMARY KEY (roleName,permissionName),
  KEY fk_rolepermissions_permission_namex (permissionName),
  CONSTRAINT fk_rolepermissions_permission FOREIGN KEY (permissionName) REFERENCES Permission (name) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_rolepermissions_role FOREIGN KEY (roleName) REFERENCES Role (name) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data for table RolePermission
INSERT INTO RolePermission VALUES ('ConcernsAdmin','concerns:change:add:concern'),('ETL','concerns:data:sync'),
('ETL','concerns:register:trainee'),('ConcernsAdmin','concerns:see:dbc:concerns'),
('RVAdmin','concerns:see:trainee:concerns'),('RVOfficer','concerns:see:trainee:concerns'),
('TisAdmin','notification:change:add:notification'),('RVAdmin','profile:get:ro:user'),('RVAdmin','profile:get:users'),
('RVOfficer','profile:get:users'),('RVAdmin','revalidation:assign:admin'),('RVOfficer','revalidation:assign:admin'),
('RVAdmin','revalidation:change:add:note'),('RVOfficer','revalidation:change:add:note'),('ETL','revalidation:data:sync'),
('RVAdmin','revalidation:manual:archive'),('ETL','revalidation:register:trainee'),('RVAdmin','revalidation:see:dbc:trainees'),
('RVOfficer','revalidation:see:dbc:trainees'),('RVAdmin','revalidation:submit:on:behalf:of:ro'),
('RVOfficer','revalidation:submit:to:gmc'),('RVAdmin','revalidation:submit:to:ro:review'),
('RVOfficer','revalidation:submit:to:ro:review'),('ETL','trainee-id:register:trainee'),('ETL','trainee-id:view:all:mappings');

-- Table structure for table HeeUser
CREATE TABLE HeeUser (
  name varchar(100) NOT NULL,
  firstName varchar(70) DEFAULT NULL,
  lastName varchar(50) NOT NULL,
  gmcId varchar(7) DEFAULT NULL,
  phoneNumber varchar(20) DEFAULT NULL,
  emailAddress varchar(100) NOT NULL,
  active tinyint(1) DEFAULT '1',
  PRIMARY KEY (name)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table structure for table UserDesignatedBody
CREATE TABLE UserDesignatedBody (
  userName varchar(50) NOT NULL,
  designatedBodyCode varchar(50) NOT NULL,
  PRIMARY KEY (userName,designatedBodyCode),
  CONSTRAINT fk_dbc_heeuser FOREIGN KEY (userName) REFERENCES HeeUser (name) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Table structure for table UserRole
CREATE TABLE UserRole (
  userName varchar(100) NOT NULL,
  roleName varchar(100) NOT NULL,
  PRIMARY KEY (userName,roleName),
  KEY fk_userrole_role_namex (roleName),
  CONSTRAINT fk_userrole_heeuser FOREIGN KEY (userName) REFERENCES HeeUser (name) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_userrole_role FOREIGN KEY (roleName) REFERENCES Role (name) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Static user data (ETLs)
INSERT INTO HeeUser VALUES ('core_etl',NULL,'',NULL,NULL,'fake_email@fake.com',1),('gmc_etl',NULL,'',NULL,NULL,'fake_email@fake.com',1);

INSERT INTO UserDesignatedBody VALUES ('core_etl','DUMMY-CORE'),('gmc_etl','DUMMY-GMC');

INSERT INTO UserRole VALUES ('core_etl','ETL'),('gmc_etl','ETL');

-- Table structure for table TraineeProfile
CREATE TABLE TraineeProfile (
  tisId bigint(20) NOT NULL AUTO_INCREMENT,
  gmcNumber varchar(7) NOT NULL,
  active tinyint(1) DEFAULT '1',
  dateAdded date DEFAULT NULL,
  designatedBodyCode varchar(20) DEFAULT NULL,
  PRIMARY KEY (tisId),
  UNIQUE KEY gmcNumber (gmcNumber)
) ENGINE=InnoDB AUTO_INCREMENT=1090 DEFAULT CHARSET=latin1;