CREATE TABLE political_view
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO political_view (name)
VALUES ('Anarchist');
INSERT INTO political_view (name)
VALUES ('Communist');
INSERT INTO political_view (name)
VALUES ('Socialist');
INSERT INTO political_view (name)
VALUES ('Moderate');
INSERT INTO political_view (name)
VALUES ('Liberal');
INSERT INTO political_view (name)
VALUES ('Conservative');
INSERT INTO political_view (name)
VALUES ('Monarchist');
INSERT INTO political_view (name)
VALUES ('Ultraconservative');
INSERT INTO political_view (name)
VALUES ('Apathetic');
INSERT INTO political_view (name)
VALUES ('Libertarian');

CREATE TABLE user_profile
(
    id                  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id             VARCHAR NOT NULL,
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
    political_views     political_view references political_view (id),
    hobbies             TEXT[]
);

INSERT INTO user_profile (user_id, bio, interests, relationship, fav_music, fav_movies, fav_games, fav_quotes,
                          inspired_by, views_on_alcohol, views_on_smoking, important_in_others, personal_priority,
                          political_views, hobbies,)
VALUES ('user1', 'Avid reader and hiker', ARRAY ['Reading', 'Hiking'], 'Single', ARRAY ['Classical', 'Jazz'],
        ARRAY ['Inception', 'Interstellar'], ARRAY ['The Witcher', 'Stardew Valley'],
        ARRAY ['To be or not to be', 'Stay hungry, stay foolish'], ARRAY ['Nikola Tesla', 'Marie Curie'], 'Neutral',
        'Negative', ARRAY ['Kindness', 'Creativity'], ARRAY ['Career and money', 'Personal development'], 'Liberal',
        ARRAY ['Guitar', 'Cooking']);

INSERT INTO user_profile (user_id, bio, interests, relationship, fav_music, fav_movies, fav_games, fav_quotes,
                          inspired_by, views_on_alcohol, views_on_smoking, important_in_others, personal_priority,
                          political_views, hobbies)
VALUES ('user2', 'Tech enthusiast and blogger', ARRAY ['Technology', 'Blogging'], 'Married',
        ARRAY ['Electronic', 'Pop'], ARRAY ['The Matrix', 'Blade Runner'], ARRAY ['Cyberpunk 2077', 'Portal'],
        ARRAY ['The only way to do great work is to love what you do', 'Don’t panic'],
        ARRAY ['Steve Jobs', 'Ada Lovelace'], 'Positive', 'Tolerant', ARRAY ['Intelligence', 'Honesty'],
        ARRAY ['Science and research', 'Improving the world'], 'Moderate', ARRAY ['Coding', 'Chess']);

INSERT INTO user_profile (user_id, bio, interests, relationship, fav_music, fav_movies, fav_games, fav_quotes,
                          inspired_by, views_on_alcohol, views_on_smoking, important_in_others, personal_priority,
                          political_views, hobbies)
VALUES ('user3', 'Freelance artist and photographer', ARRAY ['Art', 'Photography'], 'Engaged',
        ARRAY ['Indie', 'Alternative'], ARRAY ['Amelie', 'Eternal Sunshine of the Spotless Mind'],
        ARRAY ['Minecraft', 'Journey'],
        ARRAY ['Art is not what you see, but what you make others see', 'Everything you can imagine is real'],
        ARRAY ['Frida Kahlo', 'Vincent Van Gogh'], 'Negative', 'Very negative', ARRAY ['Creativity', 'Empathy'],
        ARRAY ['Beauty and art', 'Personal development'], 'Anarchist', ARRAY ['Painting', 'Drawing']);
