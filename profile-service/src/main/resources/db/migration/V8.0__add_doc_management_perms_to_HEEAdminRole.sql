INSERT INTO `Permission` (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
       ('qualification:view', 'PERSON', 'Can view qualifications', 'tis:profile::user:', 'tis:tcs::qualification:', 'View', 'Allow'),
       ('qualification:add:modify', 'PERSON', 'Can create and edit qualifications', 'tis:profile::user:', 'tis:tcs::qualification:', 'Create,Update', 'Allow')
ON DUPLICATE KEY UPDATE `name` = `name`;

# assign the current roles that have person view/edit the new qualification permission except the trust admin role
INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
       ('BulkUploadAdmin','qualification:view'),
       ('ESR','qualification:view'),
       ('ETL','qualification:view'),
       ('HEE Admin','qualification:view'),
       ('HEE Admin Revalidation','qualification:view'),
       ('HEE Admin Sensitive','qualification:view'),
       ('HEE TIS Admin','qualification:view'),
       ('PersonAdmin','qualification:view')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;

INSERT INTO `RolePermission` (`roleName`,`permissionName`)
VALUES
      ('BulkUploadAdmin','qualification:add:modify'),
      ('ETL','qualification:add:modify'),
      ('HEE Admin','qualification:add:modify'),
      ('HEE Admin Revalidation','qualification:add:modify'),
      ('HEE Admin Sensitive','qualification:add:modify'),
      ('HEE TIS Admin','qualification:add:modify'),
      ('PersonAdmin','qualification:add:modify')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;
