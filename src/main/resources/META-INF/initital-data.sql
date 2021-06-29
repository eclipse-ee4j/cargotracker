CREATE SEQUENCE IF NOT EXISTS cargotracker_seq START WITH 50 INCREMENT 1;

CREATE TABLE IF NOT EXISTS applicationsettings(id bigint PRIMARY KEY, sampleloaded boolean);

CREATE TABLE IF NOT EXISTS cargo ( id bigint NOT NULL, calculated_at VARCHAR, eta VARCHAR, unloaded_at_dest boolean, misdirected boolean, routing_status VARCHAR, transport_status VARCHAR, next_expected_handling_event_type VARCHAR, next_expected_location_id bigint, next_expected_voyage_id bigint, current_voyage_id bigint, last_event_id bigint, last_known_location_id bigint, spec_arrival_deadline VARCHAR, spec_destination_id bigint, spec_origin_id bigint, tracking_id VARCHAR, origin_id bigint );

CREATE TABLE IF NOT EXISTS carrier_movement ( id bigint NOT NULL, arrival_time VARCHAR, departure_time VARCHAR, arrival_location_id bigint, departure_location_id bigint, voyage_id bigint, movement_order integer );

CREATE TABLE IF NOT EXISTS handlingevent ( id bigint NOT NULL, completiontime VARCHAR, registration VARCHAR, type VARCHAR, cargo_id bigint, location_id bigint, voyage_id bigint );

CREATE TABLE IF NOT EXISTS leg ( id bigint NOT NULL, load_time VARCHAR, unload_time VARCHAR, load_location_id bigint, unload_location_id bigint, voyage_id bigint, cargo_id bigint, leg_order integer );

CREATE TABLE IF NOT EXISTS location ( id bigint NOT NULL, name VARCHAR, unlocode VARCHAR );

CREATE TABLE IF NOT EXISTS voyage ( id bigint NOT NULL, voyage_number VARCHAR );
-- Init the sample loading flag won't update if record exists

INSERT INTO applicationsettings (id, sampleloaded) VALUES (1, FALSE) ON CONFLICT (id) DO NOTHING;