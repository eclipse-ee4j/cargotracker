-- It's OK for this to fail if the record already exists.
INSERT INTO applicationsettings (select 1 as id, 0 as sampleloaded from applicationsettings where id = 1 having count(*)=0)