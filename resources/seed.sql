INSERT INTO courses (id, title) VALUES (1, 'Biology');
INSERT INTO courses (id, title) VALUES (2, 'Mathematics');
INSERT INTO courses (id, title) VALUES (3, 'Chemistry');
INSERT INTO courses (id, title) VALUES (4, 'Physics');
INSERT INTO courses (id, title) VALUES (5, 'Computer Science');
INSERT INTO courses (id, title) VALUES (6, 'Web Development');
INSERT INTO courses (id, title) VALUES (7, 'JavaScript (Node.js + React.js)');

INSERT INTO teachers (id, name, surname) VALUES (1, 'Natalya', 'Pavlovsky');
INSERT INTO teachers (id, name, surname) VALUES (2, 'Gracie', 'Gardner');
INSERT INTO teachers (id, name, surname) VALUES (3, 'Yevhen', 'Ahmed');
INSERT INTO teachers (id, name, surname) VALUES (4, 'Hikaru', 'Yamazaki');
INSERT INTO teachers (id, name, surname) VALUES (5, 'Tai Jung-Hee', 'Wong');
INSERT INTO teachers (id, name, surname) VALUES (6, 'Angele', 'Metzger');

INSERT INTO repetitions (teacher_id, course_id) VALUES (1, 1);
INSERT INTO repetitions (teacher_id, course_id) VALUES (2, 1);
INSERT INTO repetitions (teacher_id, course_id) VALUES (2, 2);
INSERT INTO repetitions (teacher_id, course_id) VALUES (3, 2);
INSERT INTO repetitions (teacher_id, course_id) VALUES (5, 2);
INSERT INTO repetitions (teacher_id, course_id) VALUES (4, 3);
INSERT INTO repetitions (teacher_id, course_id) VALUES (5, 3);
INSERT INTO repetitions (teacher_id, course_id) VALUES (1, 4);
INSERT INTO repetitions (teacher_id, course_id) VALUES (2, 4);
INSERT INTO repetitions (teacher_id, course_id) VALUES (3, 5);
INSERT INTO repetitions (teacher_id, course_id) VALUES (3, 6);
INSERT INTO repetitions (teacher_id, course_id) VALUES (4, 6);
INSERT INTO repetitions (teacher_id, course_id) VALUES (6, 6);
INSERT INTO repetitions (teacher_id, course_id) VALUES (4, 7);
INSERT INTO repetitions (teacher_id, course_id) VALUES (5, 7);
INSERT INTO repetitions (teacher_id, course_id) VALUES (6, 7);

INSERT INTO users (id, account, password_salt, password_pepper, password_hash, password_loop, role) VALUES (1, 'sigtest', 'b9c9b37026d7aa097218480a080e5130', 'b5625ee4c90790e5f37e6c1e90d22774', 'cdfe65f61e2bfa760489fb77bb46f0c5aedef2bc2a2287aee9e3b1b6c2c9a3a31883346f784a7f7cc4c9a7c11b63aec8b53521de763737ab47f4af0fdfcfa49d', 3, 'user');
INSERT INTO users (id, account, password_salt, password_pepper, password_hash, password_loop, role) VALUES (2, 'deana-íniguez', 'b9c9b37026d7aa097218480a080e5130', 'b5625ee4c90790e5f37e6c1e90d22774', 'cdfe65f61e2bfa760489fb77bb46f0c5aedef2bc2a2287aee9e3b1b6c2c9a3a31883346f784a7f7cc4c9a7c11b63aec8b53521de763737ab47f4af0fdfcfa49d', 3, 'user');
INSERT INTO users (id, account, password_salt, password_pepper, password_hash, password_loop, role) VALUES (3, 'elior-wakefield', 'b9c9b37026d7aa097218480a080e5130', 'b5625ee4c90790e5f37e6c1e90d22774', 'cdfe65f61e2bfa760489fb77bb46f0c5aedef2bc2a2287aee9e3b1b6c2c9a3a31883346f784a7f7cc4c9a7c11b63aec8b53521de763737ab47f4af0fdfcfa49d', 3, 'user');
INSERT INTO users (id, account, password_salt, password_pepper, password_hash, password_loop, role) VALUES (4, 'jamie-mcdonald', 'b9c9b37026d7aa097218480a080e5130', 'b5625ee4c90790e5f37e6c1e90d22774', 'cdfe65f61e2bfa760489fb77bb46f0c5aedef2bc2a2287aee9e3b1b6c2c9a3a31883346f784a7f7cc4c9a7c11b63aec8b53521de763737ab47f4af0fdfcfa49d', 3, 'user');
INSERT INTO users (id, account, password_salt, password_pepper, password_hash, password_loop, role) VALUES (5, 'abu-sasaki', 'b9c9b37026d7aa097218480a080e5130', 'b5625ee4c90790e5f37e6c1e90d22774', 'cdfe65f61e2bfa760489fb77bb46f0c5aedef2bc2a2287aee9e3b1b6c2c9a3a31883346f784a7f7cc4c9a7c11b63aec8b53521de763737ab47f4af0fdfcfa49d', 3, 'user');
INSERT INTO users (id, account, password_salt, password_pepper, password_hash, password_loop, role) VALUES (6, 'sigtest-admin', 'b9c9b37026d7aa097218480a080e5130', 'b5625ee4c90790e5f37e6c1e90d22774', 'cdfe65f61e2bfa760489fb77bb46f0c5aedef2bc2a2287aee9e3b1b6c2c9a3a31883346f784a7f7cc4c9a7c11b63aec8b53521de763737ab47f4af0fdfcfa49d', 3, 'admin');

INSERT INTO lessons (user_id, teacher_id, course_id, day, hour, completed, created_at, updated_at) VALUES (2, 2, 1, 1, 16, FALSE, NOW(), NOW());
INSERT INTO lessons (user_id, teacher_id, course_id, day, hour, completed, created_at, updated_at) VALUES (3, 5, 2, 3, 17, FALSE, NOW(), NOW());
INSERT INTO lessons (user_id, teacher_id, course_id, day, hour, completed, created_at, updated_at) VALUES (4, 2, 4, 3, 18, FALSE, NOW(), NOW());
INSERT INTO lessons (user_id, teacher_id, course_id, day, hour, completed, created_at, updated_at) VALUES (5, 6, 6, 2, 17, FALSE, NOW(), NOW());
INSERT INTO lessons (user_id, teacher_id, course_id, day, hour, completed, created_at, updated_at) VALUES (2, 5, 3, 1, 16, FALSE, NOW(), NOW());
INSERT INTO lessons (user_id, teacher_id, course_id, day, hour, completed, created_at, updated_at) VALUES (3, 5, 3, 1, 18, FALSE, NOW(), NOW());
INSERT INTO lessons (user_id, teacher_id, course_id, day, hour, completed, created_at, updated_at) VALUES (5, 5, 7, 1, 17, FALSE, NOW(), NOW());
INSERT INTO lessons (user_id, teacher_id, course_id, day, hour, completed, created_at, updated_at) VALUES (3, 6, 7, 2, 18, FALSE, NOW(), NOW());
INSERT INTO lessons (user_id, teacher_id, course_id, day, hour, completed, created_at, updated_at) VALUES (4, 5, 7, 4, 17, FALSE, NOW(), NOW());
INSERT INTO lessons (user_id, teacher_id, course_id, day, hour, completed, created_at, updated_at) VALUES (5, 6, 7, 5, 16, FALSE, NOW(), NOW());
