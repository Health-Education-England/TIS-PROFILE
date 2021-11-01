-- Get rid of the existing roles and detach them from the users

DELETE Role, UserRole
FROM Role
	INNER JOIN UserRole
	ON Role.name = UserRole.roleName
WHERE Role.name
  IN
  ('RevalSiteAdmin',
	'RevalSuperAdmin',
	'RevalTISAdmin1',
	'RevalTISAdmin2',
	'RevalTISAdmin3');

-- add the new roles

INSERT INTO `Role` (`name`)
VALUES
      ('RevalApprover'),
      ('RevalAdmin'),
      ('RevalObserver')
ON DUPLICATE KEY UPDATE `name` = `name`;