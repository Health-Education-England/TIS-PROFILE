-- modify the id column to be auto increment as it was missed out

ALTER TABLE `UserTrust`
MODIFY COLUMN `id` BIGINT(20) NOT NULL AUTO_INCREMENT;
