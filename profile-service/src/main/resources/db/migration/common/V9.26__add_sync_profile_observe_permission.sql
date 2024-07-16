INSERT INTO `UserRole` (`userName`, `roleName`)
VALUES
    ('sync_service', 'ProfileObserver')
ON DUPLICATE KEY UPDATE `userName` = `userName`;