CREATE TABLE IF NOT EXISTS friends
(
    user_id   VARCHAR NOT NULL,
    friend_id VARCHAR NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES keycloak.public.user_entity (id),
    FOREIGN KEY (friend_id) REFERENCES keycloak.public.user_entity (id)
);

INSERT INTO friends (user_id, friend_id)
VALUES ('238c349c3mmc39', '958495674564ddd');
INSERT INTO friends (user_id, friend_id)
VALUES ('wixmrwqwxuihrw', '958495674564ddd');
INSERT INTO friends (user_id, friend_id)
VALUES ('9485cm34543mc4', 'mv454v8myvm495v');
INSERT INTO friends (user_id, friend_id)
VALUES ('238c349c3mmc36', 'mv454v8myvm495v');
INSERT INTO friends (user_id, friend_id)
VALUES ('95846vm458v694', 'er68vm9458v945mv');
INSERT INTO friends (user_id, friend_id)
VALUES ('238c349c3mmc31', 'er68vm9458v945mv');
INSERT INTO friends (user_id, friend_id)
VALUES ('238c3494844444', '4958mv8945m986m89');