-- Users table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Movies table
CREATE TABLE IF NOT EXISTS movies (
    id UUID PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    year INT NOT NULL,
    avg_rating NUMERIC(4,2) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Ratings table
CREATE TABLE IF NOT EXISTS ratings (
    id UUID PRIMARY KEY,
    value INT NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id),
    movie_id UUID NOT NULL REFERENCES movies(id)
);

-- ========================
-- Users (10)
-- ========================
INSERT INTO users (id, email, password, created_at) VALUES
                                                        ('11111111-1111-1111-1111-111111111111', 'alice@example.com', 'pass123', NOW()),
                                                        ('22222222-2222-2222-2222-222222222222', 'bob@example.com', 'pass123', NOW()),
                                                        ('33333333-3333-3333-3333-333333333333', 'carol@example.com', 'pass123', NOW()),
                                                        ('44444444-4444-4444-4444-444444444444', 'dave@example.com', 'pass123', NOW()),
                                                        ('55555555-5555-5555-5555-555555555555', 'eve@example.com', 'pass123', NOW()),
                                                        ('66666666-6666-6666-6666-666666666666', 'frank@example.com', 'pass123', NOW()),
                                                        ('77777777-7777-7777-7777-777777777777', 'grace@example.com', 'pass123', NOW()),
                                                        ('88888888-8888-8888-8888-888888888888', 'heidi@example.com', 'pass123', NOW()),
                                                        ('99999999-9999-9999-9999-999999999999', 'ivan@example.com', 'pass123', NOW()),
                                                        ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'judy@example.com', 'pass123', NOW());

-- ========================
-- Movies (10)
-- ========================
INSERT INTO movies (id, name, year, avg_rating, created_at) VALUES
                                                                ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Inception', 2010, 0, NOW()),
                                                                ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'The Matrix', 1999, 0, NOW()),
                                                                ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Interstellar', 2014, 0, NOW()),
                                                                ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'The Godfather', 1972, 0, NOW()),
                                                                ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Pulp Fiction', 1994, 0, NOW()),
                                                                ('aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0001', 'The Dark Knight', 2008, 0, NOW()),
                                                                ('aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0002', 'Fight Club', 1999, 0, NOW()),
                                                                ('aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0003', 'Forrest Gump', 1994, 0, NOW()),
                                                                ('aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0004', 'Gladiator', 2000, 0, NOW()),
                                                                ('aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0005', 'The Shawshank Redemption', 1994, 0, NOW());

-- ========================
-- Ratings (0–10 scale, bigger gaps)
-- ========================
INSERT INTO ratings (id, value, user_id, movie_id) VALUES
                                                       -- Inception (high average)
                                                       ('11111111-1111-1111-1111-aaaaaaaaaaaa', 10, '11111111-1111-1111-1111-111111111111', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
                                                       ('22222222-2222-2222-2222-bbbbbbbbbbbb', 9, '22222222-2222-2222-2222-222222222222', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
                                                       ('33333333-3333-3333-3333-cccccccccccc', 10, '33333333-3333-3333-3333-333333333333', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),

                                                       -- The Matrix (medium average)
                                                       ('44444444-4444-4444-4444-dddddddddddd', 6, '11111111-1111-1111-1111-111111111111', 'cccccccc-cccc-cccc-cccc-cccccccccccc'),
                                                       ('55555555-5555-5555-5555-eeeeeeeeeeee', 5, '44444444-4444-4444-4444-444444444444', 'cccccccc-cccc-cccc-cccc-cccccccccccc'),
                                                       ('66666666-6666-6666-6666-ffffffffffff', 7, '55555555-5555-5555-5555-555555555555', 'cccccccc-cccc-cccc-cccc-cccccccccccc'),

                                                       -- Interstellar (low average)
                                                       ('77777777-7777-7777-7777-aaaaaaaaaaaa', 3, '22222222-2222-2222-2222-222222222222', 'dddddddd-dddd-dddd-dddd-dddddddddddd'),
                                                       ('88888888-8888-8888-8888-bbbbbbbbbbbb', 2, '66666666-6666-6666-6666-666666666666', 'dddddddd-dddd-dddd-dddd-dddddddddddd'),
                                                       ('99999999-9999-9999-9999-cccccccccccc', 4, '77777777-7777-7777-7777-777777777777', 'dddddddd-dddd-dddd-dddd-dddddddddddd'),

                                                       -- The Godfather (high average)
                                                       ('aaaaaaaa-aaaa-aaaa-aaaa-dddddddddddd', 10, '33333333-3333-3333-3333-333333333333', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee'),
                                                       ('bbbbbbbb-bbbb-bbbb-bbbb-eeeeeeeeeeee', 9, '11111111-1111-1111-1111-111111111111', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee'),
                                                       ('cccccccc-cccc-cccc-cccc-aaaaaaaaaaaa', 8, '44444444-4444-4444-4444-444444444444', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee'),

                                                       -- Pulp Fiction (medium average)
                                                       ('dddddddd-dddd-dddd-dddd-bbbbbbbbbbbb', 6, '55555555-5555-5555-5555-555555555555', 'ffffffff-ffff-ffff-ffff-ffffffffffff'),
                                                       ('eeeeeeee-eeee-eeee-eeee-cccccccccccc', 5, '22222222-2222-2222-2222-222222222222', 'ffffffff-ffff-ffff-ffff-ffffffffffff'),
                                                       ('ffffffff-ffff-ffff-ffff-dddddddddddd', 7, '66666666-6666-6666-6666-666666666666', 'ffffffff-ffff-ffff-ffff-ffffffffffff'),

                                                       -- The Dark Knight (high average)
                                                       ('00000000-0000-0000-0000-aaaaaaaaaaaa', 10, '77777777-7777-7777-7777-777777777777', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0001'),
                                                       ('00000000-0000-0000-0000-bbbbbbbbbbbb', 9, '88888888-8888-8888-8888-888888888888', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0001'),
                                                       ('00000000-0000-0000-0000-cccccccccccc', 8, '99999999-9999-9999-9999-999999999999', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0001'),

                                                       -- Fight Club (low average)
                                                       ('00000000-0000-0000-0000-dddddddddddd', 3, '11111111-1111-1111-1111-111111111111', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0002'),
                                                       ('00000000-0000-0000-0000-eeeeeeeeeeee', 4, '33333333-3333-3333-3333-333333333333', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0002'),
                                                       ('00000000-0000-0000-0000-ffffffffffff', 5, '55555555-5555-5555-5555-555555555555', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0002'),

                                                       -- Forrest Gump (medium average)
                                                       ('00000000-0000-0000-0000-111111111111', 6, '66666666-6666-6666-6666-666666666666', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0003'),
                                                       ('00000000-0000-0000-0000-222222222222', 7, '77777777-7777-7777-7777-777777777777', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0003'),
                                                       ('00000000-0000-0000-0000-333333333333', 5, '88888888-8888-8888-8888-888888888888', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0003'),

                                                       -- Gladiator (low average)
                                                       ('00000000-0000-0000-0000-444444444444', 2, '99999999-9999-9999-9999-999999999999', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0004'),
                                                       ('00000000-0000-0000-0000-555555555555', 3, '22222222-2222-2222-2222-222222222222', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0004'),
                                                       ('00000000-0000-0000-0000-666666666666', 4, '33333333-3333-3333-3333-333333333333', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0004'),

                                                       -- The Shawshank Redemption (high average)
                                                       ('00000000-0000-0000-0000-777777777777', 9, '44444444-4444-4444-4444-444444444444', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0005'),
                                                       ('00000000-0000-0000-0000-888888888888', 10, '55555555-5555-5555-5555-555555555555', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0005'),
                                                       ('00000000-0000-0000-0000-999999999999', 8, '66666666-6666-6666-6666-666666666666', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0005');

-- ========================
-- Update movies average ratings (0-10 scale)
-- ========================
UPDATE movies SET avg_rating = sub.avg
FROM (
         SELECT movie_id, AVG(value)::numeric(4,2) AS avg
         FROM ratings
         GROUP BY movie_id
     ) AS sub
WHERE movies.id = sub.movie_id;

