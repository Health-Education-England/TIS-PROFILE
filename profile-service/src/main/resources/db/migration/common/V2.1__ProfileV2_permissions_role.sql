INSERT INTO `Permission` (`name`)
VALUES
  ('profile:view:entities'),
  ('profile:add:modify:entities'),
  ('profile:delete:entities');

INSERT INTO `Role` (`name`)
VALUES
  ('ProfileAdmin'),
  ('ProfileObserver');

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
  ('ProfileAdmin', 'profile:view:entities'),
  ('ProfileAdmin', 'profile:add:modify:entities'),
  ('ProfileAdmin', 'profile:delete:entities'),
  ('ProfileObserver', 'profile:view:entities');

