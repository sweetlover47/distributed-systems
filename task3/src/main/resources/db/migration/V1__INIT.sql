DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS nodes;

CREATE TABLE nodes(
    id BIGINT NOT NULL PRIMARY KEY,
    username VARCHAR(128) NOT NULL,
    longitude FLOAT NOT NULL,
    latitude FLOAT NOT NULL
);

CREATE TABLE tags(
    node_id BIGINT NOT NULL REFERENCES nodes(id),
    key VARCHAR(128) NOT NULL,
    value VARCHAR(128) NOT NULL,
    PRIMARY KEY (node_id, key)
);