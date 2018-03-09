INSERT INTO `Permission` (`name`,`type`,`description`,`principal`,`resource`,`actions`,`effect`)
VALUES
('personsensitive:view:entities','PERSON','Can view person sensitive data','tis:profile::user:','tis:people::person:','View','Allow'),
('personsensitive:add:modify:entities','PERSON','Can add or modify person sensitive data','tis:profile::user:','tis:people::person:','Create,Update','Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;