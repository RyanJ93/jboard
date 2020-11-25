CREATE TABLE IF NOT EXISTS course (id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(50) NOT NULL);
CREATE TABLE IF NOT EXISTS lessons (id INT AUTO_INCREMENT PRIMARY KEY, course_id INT NOT NULL, user_id INT NOT NULL, teacher_id INT NOT NULL, day  INT NOT NULL, hour INT NOT NULL);
CREATE TABLE IF NOT EXISTS repetitions (id INT AUTO_INCREMENT PRIMARY KEY, teacher_id INT NOT NULL, course_id INT NOT NULL);
CREATE TABLE IF NOT EXISTS teachers (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50) NOT NULL, surname VARCHAR(50) NOT NULL);
CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, account VARCHAR(50) NOT NULL, password_salt VARCHAR(32) NOT NULL, password_pepper VARCHAR(32) NOT NULL, password_hash VARCHAR(128) NOT NULL, password_loop INT NOT NULL, role VARCHAR(10) NOT NULL);