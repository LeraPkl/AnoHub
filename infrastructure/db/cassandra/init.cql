CREATE KEYSPACE IF NOT EXISTS user_profiles WITH REPLICATION = {
    'class' : 'SimpleStrategy', 'replication_factor' : 1
    };

USE user_profiles;

CREATE TABLE political_view
(
    id   UUID PRIMARY KEY,
    name TEXT
);

INSERT INTO political_view (id, name)
VALUES (uuid(), 'Anarchist');
INSERT INTO political_view (id, name)
VALUES (uuid(), 'Communist');
INSERT INTO political_view (id, name)
VALUES (uuid(), 'Socialist');
INSERT INTO political_view (id, name)
VALUES (uuid(), 'Moderate');
INSERT INTO political_view (id, name)
VALUES (uuid(), 'Liberal');
INSERT INTO political_view (id, name)
VALUES (uuid(), 'Conservative');
INSERT INTO political_view (id, name)
VALUES (uuid(), 'Monarchist');
INSERT INTO political_view (id, name)
VALUES (uuid(), 'Ultraconservative');
INSERT INTO political_view (id, name)
VALUES (uuid(), 'Apathetic');
INSERT INTO political_view (id, name)
VALUES (uuid(), 'Libertarian');

CREATE TABLE user_profile
(
    id                  UUID PRIMARY KEY,
    bio                 TEXT,
    link_to_pfp         TEXT,
    interests           LIST<TEXT>,
    relationship        TEXT,
    fav_music           LIST<TEXT>,
    fav_movies          LIST<TEXT>,
    fav_games           LIST<TEXT>,
    fav_quotes          LIST<TEXT>,
    inspired_by         LIST<TEXT>,
    important_in_others LIST<TEXT>,
    personal_priority   LIST<TEXT>,
    political_views_id  UUID,
    hobbies             LIST<TEXT>
);

INSERT INTO user_profile (id, bio, link_to_pfp, interests, relationship, fav_music, fav_movies, fav_games, fav_quotes,
                          inspired_by, important_in_others, personal_priority,
                          political_views_id, hobbies)
VALUES (uuid(), 'Avid reader and hiker', 'https://somepic', ['Reading', 'Hiking'], 'SINGLE', ['Classical', 'Jazz'],
        ['Inception', 'Interstellar'], ['The Witcher', 'Stardew Valley'],
        ['To be or not to be', 'Stay hungry, stay foolish'], ['Nikola Tesla', 'Marie Curie'],
        ['Kindness', 'Creativity'], ['Career and money', 'Personal development'],
        2661ad2e-461b-4cd4-bf3c-f30fc7efa0a3, ['Guitar', 'Cooking']);

