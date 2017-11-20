TRUNCATE hibernate_sequence;

INSERT INTO hibernate_sequence VALUES (LAST_INSERT_ID() + 1);