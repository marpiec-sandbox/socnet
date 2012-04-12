CREATE TABLE events (aggregate_uid UUID, event_time timestamp, event_type VARCHAR(128), event VARCHAR(10240));

CREATE TABLE aggregates (class VARCHAR(128), uid UUID, version INT);