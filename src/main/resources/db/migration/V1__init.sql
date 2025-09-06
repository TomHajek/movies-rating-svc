CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE movies (
                        id UUID PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        year INT NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE ratings (
                         id UUID PRIMARY KEY,
                         user_id UUID REFERENCES users(id),
                         movie_id UUID REFERENCES movies(id),
                         value INT NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT now()
);

INSERT INTO users (id, email, password, created_at)
VALUES
    (gen_random_uuid(), 'alice@example.com', '$2b$12$DdztHJz5EDLSlHvEsJpiC.lsLg9hKctogrLHrWxTnpIYuajkfJUJ6', now()),
    (gen_random_uuid(), 'bob@example.com', '$2b$12$6Nj7WflsHk0L0QCJha1ry.emfGWbz3D7hDXhzZsYGYwIah2dArNIi', now()),
    (gen_random_uuid(), 'charlie@example.com', '$2b$12$Cgf/QMqWA9RkbHeAB3Ky/.1kbu2FWs0WF6guS2hxIHcihXP7Mzbqi', now());

INSERT INTO movies (id, name, year, created_at)
VALUES
    (gen_random_uuid(), 'The Shawshank Redemption', 1994, now()),
    (gen_random_uuid(), 'The Dark Knight', 2008, now()),
    (gen_random_uuid(), 'Inception', 2010, now()),
    (gen_random_uuid(), 'Interstellar', 2014, now()),
    (gen_random_uuid(), 'The Matrix', 1999, now());

INSERT INTO ratings (id, user_id, movie_id, value, created_at)
SELECT gen_random_uuid(), u.id, m.id, 5, now()
FROM users u, movies m
WHERE u.email = 'alice@example.com' AND m.name = 'The Shawshank Redemption';

INSERT INTO ratings (id, user_id, movie_id, value, created_at)
SELECT gen_random_uuid(), u.id, m.id, 4, now()
FROM users u, movies m
WHERE u.email = 'alice@example.com' AND m.name = 'The Dark Knight';

INSERT INTO ratings (id, user_id, movie_id, value, created_at)
SELECT gen_random_uuid(), u.id, m.id, 3, now()
FROM users u, movies m
WHERE u.email = 'bob@example.com' AND m.name = 'Inception';

INSERT INTO ratings (id, user_id, movie_id, value, created_at)
SELECT gen_random_uuid(), u.id, m.id, 4, now()
FROM users u, movies m
WHERE u.email = 'bob@example.com' AND m.name = 'The Dark Knight';

INSERT INTO ratings (id, user_id, movie_id, value, created_at)
SELECT gen_random_uuid(), u.id, m.id, 5, now()
FROM users u, movies m
WHERE u.email = 'charlie@example.com' AND m.name = 'Interstellar';
