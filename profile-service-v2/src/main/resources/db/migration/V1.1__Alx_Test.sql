CREATE TABLE `Permission` (
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `Permission` (`name`)
VALUES
	('concerns:change:add:concern'),
	('concerns:data:sync'),
	('concerns:register:trainee'),
	('concerns:see:dbc:concerns'),
	('concerns:see:trainee:concerns'),
	('notification:change:add:notification'),
	('profile:get:ro:user'),
	('profile:get:users'),
	('revalidation:assign:admin'),
	('revalidation:change:add:note'),
	('revalidation:data:sync'),
	('revalidation:manual:archive'),
	('revalidation:register:trainee'),
	('revalidation:see:dbc:trainees'),
	('revalidation:submit:on:behalf:of:ro'),
	('revalidation:submit:to:gmc'),
	('revalidation:submit:to:ro:review'),
	('trainee-id:register:trainee'),
	('trainee-id:view:all:mappings');

