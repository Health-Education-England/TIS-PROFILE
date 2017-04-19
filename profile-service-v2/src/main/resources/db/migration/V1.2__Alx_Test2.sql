CREATE TABLE `Role` (
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `Role` (`name`)
VALUES
  ('ConcernsAdmin'),
  ('ConcernsObserver'),
  ('ETL'),
  ('RVAdmin'),
  ('RVObserver'),
  ('RVOfficer'),
  ('TisAdmin');

