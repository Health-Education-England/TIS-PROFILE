INSERT INTO `HeeUser` (`name`, `firstName`, `lastName`, `emailAddress`, `active`)
VALUES
    ('sync_service', 'TIS', 'Sync-Service', 'sync_service@tis.nhs.uk', 1)
ON DUPLICATE KEY UPDATE `name` = `name`;

INSERT INTO `UserRole` (`userName`, `roleName`)
VALUES
    ('sync_service', 'ProfileObserver')
ON DUPLICATE KEY UPDATE `userName` = `userName`;