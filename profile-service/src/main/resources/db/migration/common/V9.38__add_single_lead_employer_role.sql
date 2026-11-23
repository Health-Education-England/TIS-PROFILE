INSERT INTO `Role` (`name`)
VALUES
       ('Single Lead Employer')
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `RolePermission` (`roleName`, `permissionName`)
VALUES
       ('Single Lead Employer', 'person:view'),
       ('Single Lead Employer', 'post:view'),
       ('Single Lead Employer', 'assessment:view:entities'),
       ('Single Lead Employer', 'curriculum:view'),
       ('Single Lead Employer', 'programme:view'),
       ('Single Lead Employer', 'tcs:view:entities'),
       ('Single Lead Employer', 'person:view:programmeMembershipStatus'),
       ('Single Lead Employer', 'specialty:view'),
       ('Single Lead Employer', 'trainee-formr:view')
ON DUPLICATE KEY UPDATE `roleName` = `roleName`;