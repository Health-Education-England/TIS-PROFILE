INSERT INTO `Role` (`name`)
VALUES
      ('RevalSiteAdmin'),
      ('RevalSuperAdmin'),
      ('RevalTISAdmin1'),
      ('RevalTISAdmin2'),
      ('RevalTISAdmin3')
ON DUPLICATE KEY UPDATE `name` = `name`;
