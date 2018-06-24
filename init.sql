CREATE TABLE users(
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL

);

CREATE TABLE user_group (
  id   INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255)
);

ALTER TABLE users
  ADD COLUMN group_id INT UNSIGNED,
  ADD FOREIGN KEY (group_id) REFERENCES user_group (id);

CREATE TABLE excercises (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR (255),
  description TEXT
);

CREATE TABLE solutions(
  id INT UNSIGNED PRIMARY KEY,
  created DATETIME,
  updated DATETIME,
  description TEXT,
  excercise_id int unsigned,
  user_id BIGINT UNSIGNED,
  FOREIGN KEY (excercise_id) REFERENCES excercises(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);