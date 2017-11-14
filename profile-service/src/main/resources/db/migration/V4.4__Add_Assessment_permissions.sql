SET autocommit = 0;

INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
	('assessment:add:modify', 'ASSESSMENT', 'can add or modify assessments', 'tis:profile::user:', 'tis:assessment:entity:', 'Create,Update', 'Allow'),
	('assessment:delete:entities', 'ASSESSMENT', 'can delete assessments', 'tis:profile::user:', 'tis:assessment:entity:', 'Delete', 'Allow'),
	('assessment:view', 'ASSESSMENT', 'can view assessments', 'tis:profile::user:', 'tis:assessment:entity:', 'View', 'Allow');

INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
	('outcomes:add:modify', 'ASSESSMENT', 'can add or modify outcomes', 'tis:profile::user:', 'tis:outcomes:entity:', 'Create,Update', 'Allow'),
	('outcomes:delete:entities', 'ASSESSMENT', 'can delete outcomes', 'tis:profile::user:', 'tis:outcomes:entity:', 'Delete', 'Allow'),
	('outcomes:view', 'ASSESSMENT', 'can view outcomes', 'tis:profile::user:', 'tis:outcomes:entity:', 'View', 'Allow');

INSERT INTO `Role` (`name`)
VALUES
	('AssessmentsAdmin');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
	('AssessmentsAdmin', 'assessment:add:modify'),
	('AssessmentsAdmin', 'assessment:delete:entities'),
	('AssessmentsAdmin', 'assessment:view'),
	('AssessmentsAdmin', 'outcomes:add:modify'),
	('AssessmentsAdmin', 'outcomes:delete:entities'),
	('AssessmentsAdmin', 'outcomes:view');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
	('ETL', 'assessment:add:modify'),
	('ETL', 'assessment:delete:entities'),
	('ETL', 'assessment:view'),
	('ETL', 'outcomes:add:modify'),
	('ETL', 'outcomes:delete:entities'),
	('ETL', 'outcomes:view');

commit;

SET autocommit = 1;