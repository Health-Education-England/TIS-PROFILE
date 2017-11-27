INSERT INTO `Permission`
  (`name`, `type`, `description`, `principal`, `resource`, `actions`, `effect`)
VALUES
  ('revalidation:see:dbc:report', 'REVALIDATION', 'Can view revalidation report','tis:revalidation::user:','tis:revalidation::trainee:','View','Allow');