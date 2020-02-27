CREATE TABLE OrganisationalEntity (
  `name` varchar(45) NOT NULL,
PRIMARY KEY (`name`),
UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO OrganisationalEntity VALUES
('HEE'),('NI');

CREATE TABLE UserOrganisationalEntity (
  userName varchar(50) NOT NULL,
  organisationalEntityName varchar(45) NOT NULL,
PRIMARY KEY (userName,organisationalEntityName),
CONSTRAINT fk_userorganisationalentity_heeuser FOREIGN KEY (userName) REFERENCES HeeUser (name) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `UserOrganisationalEntity`
(`userName`, `organisationalEntityName`)
SELECT name, 'HEE' from  `HeeUser`;