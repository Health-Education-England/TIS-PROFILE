INSERT INTO `Role` (`name`)
VALUES
       ('HEE Programme Admin')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
      ('HEE Programme Admin','assessment:add:modify:entities'),
      ('HEE Programme Admin','assessment:view:entities'),
      ('HEE Programme Admin','curriculum:view'),
      ('HEE Programme Admin','curriculum:add:modify'),
      ('HEE Programme Admin','person:view'),
      ('HEE Programme Admin','person:add:modify'),
      ('HEE Programme Admin','post:view'),
      ('HEE Programme Admin','post:add:modify'),
      ('HEE Programme Admin','programme:view'),
      ('HEE Programme Admin','programme:add:modify'),
      ('HEE Programme Admin','specialty:view'),
      ('HEE Programme Admin','specialty:add:modify'),
      ('HEE Programme Admin','tcs:view:entities'),
      ('HEE Programme Admin','tcs:add:modify:entities')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;




