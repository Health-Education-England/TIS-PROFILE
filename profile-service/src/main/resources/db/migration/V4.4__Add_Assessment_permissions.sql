SET autocommit = 0;

INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
	('assessment:add:modify:entities', 'ASSESSMENT', 'can add or modify assessments', 'tis:profile::user:', 'tis:assessment:entity:', 'Create,Update', 'Allow'),
	('assessment:delete:entities', 'ASSESSMENT', 'can delete assessments', 'tis:profile::user:', 'tis:assessment:entity:', 'Delete', 'Allow'),
	('assessment:view:entities', 'ASSESSMENT', 'can view assessments', 'tis:profile::user:', 'tis:assessment:entity:', 'View', 'Allow'),
	('assessment:bulk:add:modify:entities', 'ASSESSMENT', 'can bulk add or modify assessments', 'tis:profile::user:', 'tis:assessment:entity:', 'Create,Update', 'Allow');

INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
	('outcomes:add:modify:entities', 'ASSESSMENT', 'can add or modify outcomes', 'tis:profile::user:', 'tis:outcomes:entity:', 'Create,Update', 'Allow'),
	('outcomes:delete:entities', 'ASSESSMENT', 'can delete outcomes', 'tis:profile::user:', 'tis:outcomes:entity:', 'Delete', 'Allow'),
	('outcomes:view:entities', 'ASSESSMENT', 'can view outcomes', 'tis:profile::user:', 'tis:outcomes:entity:', 'View', 'Allow'),
	('outcomes:bulk:add:modify:entities', 'ASSESSMENT', 'can bulk create or modify outcomes', 'tis:profile::user:', 'tis:outcomes:entity:', 'Create,Update', 'Allow');

INSERT INTO `Role` (`name`)
VALUES
	('AssessmentsAdmin');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
	('AssessmentsAdmin', 'assessment:add:modify:entities'),
	('AssessmentsAdmin', 'assessment:delete:entities'),
	('AssessmentsAdmin', 'assessment:view:entities'),
	('AssessmentsAdmin', 'assessment:bulk:add:modify:entities'),
	('AssessmentsAdmin', 'outcomes:add:modify:entities'),
	('AssessmentsAdmin', 'outcomes:delete:entities'),
	('AssessmentsAdmin', 'outcomes:view:entities'),
	('AssessmentsAdmin', 'outcomes:bulk:add:modify:entities');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
	('ETL', 'assessment:add:modify:entities'),
	('ETL', 'assessment:delete:entities'),
	('ETL', 'assessment:view:entities'),
	('ETL', 'assessment:bulk:add:modify:entities'),
	('ETL', 'outcomes:add:modify:entities'),
	('ETL', 'outcomes:delete:entities'),
	('ETL', 'outcomes:view:entities'),
	('ETL', 'outcomes:bulk:add:modify:entities');

commit;

SET autocommit = 1;