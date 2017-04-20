ALTER TABLE HeeUser MODIFY active BIT;

-- Create syntax for TABLE 'EqualityAndDiversity'
CREATE TABLE `EqualityAndDiversity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tisId` decimal(10,2) NOT NULL,
  `maritalStatus` varchar(255) DEFAULT NULL,
  `dateOfBirth` date DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `nationality` varchar(255) DEFAULT NULL,
  `dualNationality` varchar(255) DEFAULT NULL,
  `sexualOrientation` varchar(255) DEFAULT NULL,
  `religiousBelief` varchar(255) DEFAULT NULL,
  `ethnicOrigin` varchar(255) DEFAULT NULL,
  `disability` bit(1) DEFAULT NULL,
  `disabilityDetails` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'GdcDetails'
CREATE TABLE `GdcDetails` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tisId` decimal(10,2) NOT NULL,
  `gdcNumber` varchar(255) DEFAULT NULL,
  `gdcStatus` varchar(255) DEFAULT NULL,
  `gdcStartDate` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'GmcDetails'
CREATE TABLE `GmcDetails` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tisId` decimal(10,2) NOT NULL,
  `gmcNumber` varchar(7) DEFAULT NULL,
  `gmcStatus` varchar(255) DEFAULT NULL,
  `gmcStartDate` date DEFAULT NULL,
  `gmcExpiryDate` date DEFAULT NULL,
  `designatedBodyCode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'Immigration'
CREATE TABLE `Immigration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tisId` decimal(10,2) NOT NULL,
  `eeaResident` bit(1) DEFAULT NULL,
  `permitToWork` varchar(255) DEFAULT NULL,
  `settled` varchar(255) DEFAULT NULL,
  `visaIssued` date DEFAULT NULL,
  `visaValidTo` date DEFAULT NULL,
  `visaDetailsNumber` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'ManageRecord'
CREATE TABLE `ManageRecord` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tisId` decimal(10,2) NOT NULL,
  `recordType` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `recordStatus` varchar(255) DEFAULT NULL,
  `inactiveFrom` date DEFAULT NULL,
  `changedBy` decimal(10,2) DEFAULT NULL,
  `inactiveReason` varchar(255) DEFAULT NULL,
  `inactiveDate` date DEFAULT NULL,
  `deletionReason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'Person'
CREATE TABLE `Person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tisId` decimal(10,2) NOT NULL,
  `publicHealthId` varchar(255) DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'PersonalDetails'
CREATE TABLE `PersonalDetails` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tisId` decimal(10,2) NOT NULL,
  `surnameNb` varchar(255) DEFAULT NULL,
  `legalSurname` varchar(255) DEFAULT NULL,
  `forenames` varchar(255) DEFAULT NULL,
  `legalForenames` varchar(255) DEFAULT NULL,
  `knownAs` varchar(255) DEFAULT NULL,
  `maidenName` varchar(255) DEFAULT NULL,
  `initials` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `telephoneNumber` varchar(255) DEFAULT NULL,
  `mobileNumber` varchar(255) DEFAULT NULL,
  `emailAddress` varchar(255) DEFAULT NULL,
  `correspondenceAddress` varchar(255) DEFAULT NULL,
  `correspondenceAddressPostCode` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'Qualification'
CREATE TABLE `Qualification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tisId` decimal(10,2) NOT NULL,
  `qualification` varchar(255) DEFAULT NULL,
  `qualificationType` varchar(255) DEFAULT NULL,
  `dateAttained` date DEFAULT NULL,
  `medicalSchool` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;