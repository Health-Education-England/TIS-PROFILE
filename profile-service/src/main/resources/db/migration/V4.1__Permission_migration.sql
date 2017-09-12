ALTER TABLE `Permission` ADD `principal` VARCHAR(255) NOT NULL;
ALTER TABLE `Permission` ADD `resource` VARCHAR(255) NOT NULL;
ALTER TABLE `Permission` ADD `actions` VARCHAR(255) NOT NULL;
ALTER TABLE `Permission` ADD `effect` VARCHAR(255) NOT NULL;

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:concerns:-:concern:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'concerns:change:add:concern';

UPDATE `Permission` SET `principal` = 'tis:profile::user:core_etl',
`resource` = 'tis:concerns:::',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'concerns:data:sync';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:concerns:-:trainee:',
`actions` = 'Create', `effect` = 'Allow'
WHERE `name` = 'concerns:register:trainee';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:concerns:-:concern:',
`actions` = 'View', `effect` = 'Allow'
WHERE `name` = 'concerns:see:dbc:concerns';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:concerns:-:trainee-concern:',
`actions` = 'View', `effect` = 'Allow'
WHERE `name` = 'concerns:see:trainee:concerns';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:connection-discrepancies:-:connection-discrepancy:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'connection:discrepancies:manage';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:connection-discrepancies:-:connection-discrepancy:',
`actions` = 'View', `effect` = 'Allow'
WHERE `name` = 'connection:discrepancies:view';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:curricula:-:curriculum:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'curriculum:add:modify';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:curricula:-:curriculum:',
`actions` = 'View', `effect` = 'Allow'
WHERE `name` = 'curriculum:view';

UPDATE `Permission` SET `principal` = 'tis:profile::user:consolidated_etl',
`resource` = 'tis:curricula:::',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'curriculum:bulk:add:modify';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:notifications:-:notification:',
`actions` = 'Create', `effect` = 'Allow'
WHERE `name` = 'notification:change:add:notification';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:posts:-:post:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'post:add:modify';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:posts:-:post:',
`actions` = 'View', `effect` = 'Allow'
WHERE `name` = 'post:view';

UPDATE `Permission` SET `principal` = 'tis:profile::user:consolidated_etl',
`resource` = 'tis:posts:-:post:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'post:bulk:add:modify';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:profiles:-:profile:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'profile:add:modify:entities';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:profiles:-:profile:',
`actions` = 'Delete', `effect` = 'Allow'
WHERE `name` = 'profile:delete:entities';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:profiles:-:ro-profile:',
`actions` = 'View', `effect` = 'Allow'
WHERE `name` = 'profile:get:ro:user';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:profiles:-:profile:',
`actions` = 'View', `effect` = 'Allow'
WHERE `name` = 'profile:get:users';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:profiles::trainee:',
`actions` = 'Create', `effect` = 'Allow'
WHERE `name` = 'profile:register:trainee';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:profiles::mapping:',
`actions` = 'View', `effect` = 'Allow'
WHERE `name` = 'profile:view:all:mappings';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:profiles::entity:',
`actions` = 'View', `effect` = 'Allow'
WHERE `name` = 'profile:view:entities';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:programmes::programme:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'programme:add:modify';

UPDATE `Permission` SET `principal` = 'tis:profile::user:consolidated_etl',
`resource` = 'tis:programmes::programme:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'programme:bulk:add:modify';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:programmes::programme:',
`actions` = 'View', `effect` = 'Allow'
WHERE `name` = 'programme:view';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:references::reference:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'reference:add:modify:entities';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:references::reference:',
`actions` = 'Delete', `effect` = 'Allow'
WHERE `name` = 'reference:delete:entities';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:revalidation::admin:',
`actions` = 'Assign', `effect` = 'Allow'
WHERE `name` = 'revalidation:assign:admin';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:revalidation::note:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'revalidation:change:add:note';

UPDATE `Permission` SET `principal` = 'tis:profile::user:core_etl',
`resource` = 'tis:revalidation::revalidation:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'revalidation:data:sync';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:revalidation::revalidation:',
`actions` = 'Archive', `effect` = 'Allow'
WHERE `name` = 'revalidation:manual:archive';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:revalidation::recommendation:',
`actions` = 'Create', `effect` = 'Allow'
WHERE `name` = 'revalidation:provide:recommendation';

UPDATE `Permission` SET `principal` = 'tis:profile::user:core_etl',
`resource` = 'tis:revalidation::trainee:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'revalidation:register:trainee';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:revalidation:-:trainee:',
`actions` = 'View', `effect` = 'Allow'
WHERE `name` = 'revalidation:see:dbc:trainees';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:revalidation:-:episode:',
`actions` = 'Submit', `effect` = 'Allow'
WHERE `name` = 'revalidation:submit:on:behalf:of:ro';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:revalidation:-:recommendation:',
`actions` = 'SubmitGmc', `effect` = 'Allow'
WHERE `name` = 'revalidation:submit:to:gmc';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:revalidation:-:recommendation:',
`actions` = 'SubmitRO', `effect` = 'Allow'
WHERE `name` = 'revalidation:submit:to:ro:review';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:specialty-groups:-:specialty-group:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'specialty-group:add:modify';

UPDATE `Permission` SET `principal` = 'tis:profile::user:consolidated_etl',
`resource` = 'tis:specialty-groups::specialty-group:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'specialty-group:bulk:add:modify';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:specialty-groups:-:specialty-group:',
`actions` = 'View', `effect` = 'Allow'
WHERE `name` = 'specialty-group:view';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:specialties:-:specialty:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'specialty:add:modify';

UPDATE `Permission` SET `principal` = 'tis:profile::user:consolidated_etl',
`resource` = 'tis:specialties::specialty:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'specialty:bulk:add:modify';

UPDATE `Permission` SET `principal` = 'tis:profile::user:consolidated_etl',
`resource` = 'tis:specialties::specialty:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'specialty:bulk:add:modify';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:tcs::entity:',
`actions` = 'Create,Update', `effect` = 'Allow'
WHERE `name` = 'tcs:add:modify:entities';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:tcs::entity:',
`actions` = 'Delete', `effect` = 'Allow'
WHERE `name` = 'tcs:delete:entities';

UPDATE `Permission` SET `principal` = 'tis:profile::user:',
`resource` = 'tis:tcs::entity:',
`actions` = 'View', `effect` = 'Allow'
WHERE `name` = 'tcs:view:entities';