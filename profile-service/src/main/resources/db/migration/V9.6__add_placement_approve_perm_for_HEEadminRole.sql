INSERT INTO `Permission`(`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
	  ('placement:approve',
	  'PCS',
	  'can approve placement',
	  'tis:profile::user:',
	  'tis:placements::placement:',
	  'Update',
	  'Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
      ('HEE Admin', 'placement:approve'),
      ('HEE TIS Admin', 'placement:approve'),
      ('BulkUploadAdmin', 'placement:approve')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;