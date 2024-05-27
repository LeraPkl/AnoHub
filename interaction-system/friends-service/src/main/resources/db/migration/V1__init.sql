CREATE TABLE friends
(
    user1_id    BIGINT  NOT NULL,
    user2_id    BIGINT  NOT NULL,
    is_accepted BOOLEAN NOT NULL,
    PRIMARY KEY (user1_id, user2_id)
);

INSERT INTO friends (user1_id, user2_id, is_accepted)
VALUES (1, 2, FALSE);
INSERT INTO friends (user1_id, user2_id, is_accepted)
VALUES (1, 3, FALSE);
INSERT INTO friends (user1_id, user2_id, is_accepted)
VALUES (2, 4, FALSE);
