CREATE EXTENSION IF NOT EXISTS "pgcrypto";

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
    ('657910e1-9f20-4461-a602-9f00c389edec', 'alice@example.com', '$2b$12$DdztHJz5EDLSlHvEsJpiC.lsLg9hKctogrLHrWxTnpIYuajkfJUJ6', now()),
    ('1644bcfd-151d-4439-a1fe-160e3322d129', 'bob@example.com', '$2b$12$6Nj7WflsHk0L0QCJha1ry.emfGWbz3D7hDXhzZsYGYwIah2dArNIi', now()),
    ('79baa49c-368a-4d59-9f5e-daa17f952228', 'charlie@example.com', '$2b$12$Cgf/QMqWA9RkbHeAB3Ky/.1kbu2FWs0WF6guS2hxIHcihXP7Mzbqi', now());

INSERT INTO movies (id, name, year, created_at)
VALUES
    ('0ab1f024-1116-4c76-bdaf-4831364e57c1', 'The Shawshank Redemption', 1994, now()),
    ('8cff2fd2-39b9-4460-897d-d0c0f2ef7d29', 'The Dark Knight', 2008, now()),
    ('5653b29e-2210-48ce-8b70-66eb298e4e6a', 'Inception', 2010, now()),
    ('c027c7d2-6cf8-4905-bdc4-305421bae626', 'Interstellar', 2014, now()),
    ('c7666f87-95bb-4d53-84e0-d51ba5e99680', 'The Matrix', 1999, now());

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
