INSERT INTO `Role` VALUES('Machine User') ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `Permission`(`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
	  ('sync:run:jobs',
	  'INTERNAL',
	  'can run sync jobs',
	  'tis:profile::user:',
	  'tis:sync::jobs:',
	  'Update,View',
	  'Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
      ('Machine User','sync:run:jobs')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;