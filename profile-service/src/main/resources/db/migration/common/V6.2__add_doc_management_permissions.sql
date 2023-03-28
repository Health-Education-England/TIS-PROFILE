INSERT INTO `Permission` (`name`,`type`,`description`,`principal`,`resource`,`actions`,`effect`)
VALUES
('documentmanager:view:entities','DOCUMENT','Can view document manager data','tis:profile::user:','tis:documents::document:','View','Allow'),
('documentmanager:add:modify:entities','DOCUMENT','Can add or modify document manager data','tis:profile::user:','tis:documents::document:','Create,Update','Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;