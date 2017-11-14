SET autocommit = 0;

INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
	('assessment:add:modify:entities', 'ASSESSMENT', 'can add or modify assessments', 'tis:profile::user:', 'tis:assessment:entity:', 'Create,Update', 'Allow'),
	('assessment:delete:entities', 'ASSESSMENT', 'can delete assessments', 'tis:profile::user:', 'tis:assessment:entity:', 'Delete', 'Allow'),
	('assessment:view:entities', 'ASSESSMENT', 'can view assessments', 'tis:profile::user:', 'tis:assessment:entity:', 'View', 'Allow');

INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
	('outcomes:add:modify:entities', 'ASSESSMENT', 'can add or modify outcomes', 'tis:profile::user:', 'tis:outcomes:entity:', 'Create,Update', 'Allow'),
	('outcomes:delete:entities', 'ASSESSMENT', 'can delete outcomes', 'tis:profile::user:', 'tis:outcomes:entity:', 'Delete', 'Allow'),
	('outcomes:view:entities', 'ASSESSMENT', 'can view outcomes', 'tis:profile::user:', 'tis:outcomes:entity:', 'View', 'Allow');

INSERT INTO `Role` (`name`)
VALUES
	('AssessmentsAdmin');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
	('AssessmentsAdmin', 'assessment:add:modify:entities'),
	('AssessmentsAdmin', 'assessment:delete:entities'),
	('AssessmentsAdmin', 'assessment:view:entities'),
	('AssessmentsAdmin', 'outcomes:add:modify:entities'),
	('AssessmentsAdmin', 'outcomes:delete:entities'),
	('AssessmentsAdmin', 'outcomes:view:entities');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
	('ETL', 'assessment:add:modify:entities'),
	('ETL', 'assessment:delete:entities'),
	('ETL', 'assessment:view:entities'),
	('ETL', 'outcomes:add:modify:entities'),
	('ETL', 'outcomes:delete:entities'),
	('ETL', 'outcomes:view:entities');

commit;

SET autocommit = 1;