CREATE TABLE IF NOT EXISTS events (aggregate_uid BIGINT, event_time TIMESTAMP, version INT, event_type VARCHAR(128), event VARCHAR(10240));

CREATE TABLE IF NOT EXISTS aggregates (class VARCHAR(128), uid BIGINT, version INT);

CREATE TABLE IF NOT EXISTS uids(uidName VARCHAR(128), uid BIGINT);