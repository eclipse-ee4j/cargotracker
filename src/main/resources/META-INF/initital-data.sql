-- Init the sample loading flag, won't update if record exists
INSERT INTO applicationsettings (id, sampleloaded) VALUES (1, FALSE) ON CONFLICT (id) DO NOTHING;