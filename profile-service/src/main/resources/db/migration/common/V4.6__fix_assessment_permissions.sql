UPDATE `Permission`
SET `resource`='tis:assessment::entity:'
WHERE `name` in('assessment:add:modify:entities','assessment:delete:entities','assessment:view:entities')