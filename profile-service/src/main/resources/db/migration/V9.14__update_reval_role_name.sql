-- add the new roles

INSERT INTO `Role` (`name`)
VALUES
      ('RevalApprover'),
      ('RevalAdmin'),
      ('RevalObserver')
ON DUPLICATE KEY UPDATE `name` = `name`;

-- update the user role with the new roles

UPDATE `UserRole`
SET `roleName` = 'RevalApprover'
WHERE `roleName`='RevalTISAdmin1';

UPDATE `UserRole`
SET `roleName` = 'RevalAdmin'
WHERE `roleName`='RevalTISAdmin2';

UPDATE `UserRole`
SET `roleName` = 'RevalObserver'
WHERE `roleName`='RevalTISAdmin3';

-- handle existing RevalSiteAdmin, which is a HEE User Admin
-- and RevalSuperAdmin user, it is a combination of HEE User Admin and Reval Approver.
-- both of these roles will not be published to reval users

DELETE
FROM `UserRole`
WHERE `roleName`
IN
  ('RevalSuperAdmin',
  'RevalSiteAdmin');

-- delete the existing old roles

DELETE
FROM `Role`
WHERE `name`
IN
  ('RevalSiteAdmin',
	'RevalSuperAdmin',
	'RevalTISAdmin1',
	'RevalTISAdmin2',
	'RevalTISAdmin3');
