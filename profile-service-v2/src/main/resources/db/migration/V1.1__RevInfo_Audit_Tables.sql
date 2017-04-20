-- Table structure for table REVINFO
CREATE TABLE REVINFO (
  REV int(11) NOT NULL AUTO_INCREMENT,
  REVTSTMP bigint(20) DEFAULT NULL,
  PRIMARY KEY (REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Table structure for table TraineeProfile_AUD
CREATE TABLE TraineeProfile_AUD (
  tisId bigint(20) NOT NULL,
  REV int(11) NOT NULL,
  REVTYPE tinyint(4) DEFAULT NULL,
  active bit(1) DEFAULT NULL,
  dateAdded date DEFAULT NULL,
  designatedBodyCode varchar(255) DEFAULT NULL,
  gmcNumber varchar(255) DEFAULT NULL,
  PRIMARY KEY (tisId,REV),
  KEY fk_traineeProfile_revinfo (REV),
  CONSTRAINT fk_traineeProfile_revinfo FOREIGN KEY (REV) REFERENCES REVINFO (REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;