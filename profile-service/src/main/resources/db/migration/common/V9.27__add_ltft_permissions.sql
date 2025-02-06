INSERT INTO `Role` (`name`)
VALUES ('NHSE LTFT Admin')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
    ('ltft:view',
     'DOCUMENT',
     'Can view LTFT applications',
     'tis:profile::user:',
     'tis:forms::ltft:',
     'View',
     'Allow'),
     ('ltft:modify',
      'DOCUMENT',
      'Can modify LTFT applications',
      'tis:profile::user:',
      'tis:forms::ltft:',
      'Update',
      'Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
    ('NHSE LTFT Admin','ltft:view'),
    ('NHSE LTFT Admin','ltft:modify')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;
