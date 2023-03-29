INSERT INTO `Permission`(`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
	  ('person:view:programmeMembershipStatus',
	  'PERSON',
	  'can view programme membership status column in person list',
	  'tis:profile::user:',
	  'tis:people::person:',
	  'View',
	  'Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
      ('HEE Programme Admin','person:view:programmeMembershipStatus'),
      ('HEE Programme Observer','person:view:programmeMembershipStatus')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;
