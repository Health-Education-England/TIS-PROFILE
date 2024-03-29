SET autocommit = 0;

INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
	('assessment:add:modify:entities', 'ASSESSMENT', 'can add or modify assessments', 'tis:profile::user:', 'tis:assessment:entity:', 'Create,Update', 'Allow'),
	('assessment:delete:entities', 'ASSESSMENT', 'can delete assessments', 'tis:profile::user:', 'tis:assessment:entity:', 'Delete', 'Allow'),
	('assessment:view:entities', 'ASSESSMENT', 'can view assessments', 'tis:profile::user:', 'tis:assessment:entity:', 'View', 'Allow');

INSERT INTO `Role` (`name`)
VALUES
	('AssessmentsAdmin');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
	('AssessmentsAdmin', 'assessment:add:modify:entities'),
	('AssessmentsAdmin', 'assessment:delete:entities'),
	('AssessmentsAdmin', 'assessment:view:entities');

commit;

SET autocommit = 1;