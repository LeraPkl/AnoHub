CREATE TABLE political_view
(
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_profile
(
    id                  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id         VARCHAR UNIQUE NOT NULL,
    bio                 TEXT,
    interests           TEXT[],
    relationship        varchar,
    fav_music           TEXT[],
    fav_movies          TEXT[],
    fav_games           TEXT[],
    fav_quotes          TEXT[],
    inspired_by         TEXT[],
    views_on_alcohol    varchar,
    views_on_smoking    varchar,
    important_in_others TEXT[],
    personal_priority   TEXT[],
    political_views INT REFERENCES political_view (id),
    hobbies             TEXT[]
);
