CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id   BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (friend_id) REFERENCES users (id)
);

INSERT INTO users (username, password)
VALUES ('johnsmith', '$2a$10$zsA8G7i7e9FZfY8J6KRC7uZLzZI0wRkZZgWDMj5EsF7yZKNZf3r6G'); -- Plain password: password1
INSERT INTO users (username, password)
VALUES ('janedoe', '$2a$10$eI8QHroZoT01QYSHqG4WRehwmqLr8YtgRgI6FX0z6gL/1Y8VZpM0K'); -- Plain password: password2
INSERT INTO users (username, password)
VALUES ('mikejohnson', '$2a$10$3l6S0TnFhD3X96VvM.4VYubZUwAXs1gq4bBG5JGy1IRwd0c0YqLpG'); -- Plain password: password
INSERT INTO users (username, password)
VALUES ('sarahbrown', '$2a$10$2p8WuQq3X4yjJ3RJgP4vzOD6iF3YXI82Jwq9af3FMiVCtXpVJW83y'); -- Plain password: password4
INSERT INTO users (username, password)
VALUES ('alexwilson', '$2a$10$e4OG9s6W5Nv5aCA832b3BeW3LwPq1zqX7XvTyxQFHCBClff.2I1cW'); -- Plain password: password5
INSERT INTO users (username, password)
VALUES ('emilythomas', '$2a$10$Rz6CeyBzc4GjUx1Dh2N2WuWfGw/fZP4u1XyRr5n1Yzv7bq.NIyI7C'); -- Plain password: password6
INSERT INTO users (username, password)
VALUES ('davidmiller', '$2a$10$y3qoZ9PZmp0JnJWM5kzgWuQe3OQN1U9LdH8V6m0H3wCp9gMhVGQV.'); -- Plain password: password7
INSERT INTO users (username, password)
VALUES ('laurawilson', '$2a$10$Rd1a8KZ4Kx7zLZIEpY5lGOzG0BJ4e3Fj3OeFZBDd3oMf2CzT1vqRy'); -- Plain password: password8
INSERT INTO users (username, password)
VALUES ('brianjones', '$2a$10$5UvMEUaFf0DpAiLgPZ1T8eJ2J6ESVbmZ5qMe1p7u3y5J6IiI3kV1e'); -- Plain password: password9
INSERT INTO users (username, password)
VALUES ('kateanderson', '$2a$10$2T8DQr3W0RwMw3L0M4fZnOJYnT4Z9W8W4o2fZK8JVKY0NlVj6qH8G'); -- Plain password: password10


INSERT INTO friends (user_id, friend_id)
VALUES (9, 2);
INSERT INTO friends (user_id, friend_id)
VALUES (8, 2);
INSERT INTO friends (user_id, friend_id)
VALUES (7, 2);
INSERT INTO friends (user_id, friend_id)
VALUES (6, 3);
INSERT INTO friends (user_id, friend_id)
VALUES (2, 3);
INSERT INTO friends (user_id, friend_id)
VALUES (1, 3);
INSERT INTO friends (user_id, friend_id)
VALUES (9, 3);