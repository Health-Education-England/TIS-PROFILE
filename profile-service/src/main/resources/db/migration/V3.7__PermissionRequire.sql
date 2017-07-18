CREATE TABLE `PermissionRequires` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission` varchar(100) NOT NULL,
  `requires` varchar(100) NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_permission_requires_permission` FOREIGN KEY (`permission`) REFERENCES `Permission` (`name`),
  CONSTRAINT `fk_permission_requires_requires` FOREIGN KEY (`requires`) REFERENCES `Permission` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO Permission (name, type, description) VALUES ('revalidation:provide:recommendation', 'REVALIDATION', 'Can fill in and submit the provide recommendation form');

INSERT INTO `PermissionRequires` (`id`, `permission`, `requires`, `reason`)
VALUES
	(1, 'concerns:change:add:concern', 'concerns:see:dbc:concerns', 'User must be able to see concerns in order to change them.'),
	(2, 'connection:discrepancies:manage', 'connection:discrepancies:view', 'User must be able to view connection discrepancies in order to action them'),
	(3, 'curriculum:add:modify', 'curriculum:view', 'User must be able to view curricula in order to modify them'),
	(4, 'curriculum:add:modify', 'specialty:view', 'User must be able to at least view specialties when creating curricula'),
	(5, 'programme:add:modify', 'programme:view', 'User must be able to view programmes in order to modify them'),
	(6, 'programme:add:modify', 'curriculum:view', 'When creating a programme, user needs to at least be able to view curricula to select them on the form'),
	(7, 'specialty-group:add:modify', 'specialty-group:view', 'User must be able to view specialty groups in odrer to modify them'),
	(8, 'specialty:add:modify', 'specialty:view', 'User must be able to view specialties in order to modify them'),
	(9, 'specialty:add:modify', 'specialty-group:view', 'User must be able to at least view specialty groups when creating specialties'),
	(10, 'revalidation:assign:admin', 'revalidation:see:dbc:trainees', 'User must be able to see revalidation episodes in order to assign admins to them'),
	(11, 'revalidation:change:add:note', 'revalidation:see:dbc:trainees', 'User must be able to see revalidaton episodes to add notes to them'),
	(12, 'revalidation:provide:recommendation', 'revalidation:see:dbc:trainees', 'User must be able to see revalidation episodes in order to provide recommendation'),
	(13, 'revalidation:submit:on:behalf:of:ro', 'revalidation:see:dbc:trainees', 'User must be able to see revlidation epiodes in order to sumit to GMC'),
	(14, 'revalidation:submit:to:gmc', 'revalidation:see:dbc:trainees', 'User must be able to see revlidation epiodes in order to sumit to GMC'),
	(15, 'revalidation:submit:to:ro:review', 'revalidation:see:dbc:trainees', 'User must be able to see revlidation epiodes in order to sumit them to RO review');
