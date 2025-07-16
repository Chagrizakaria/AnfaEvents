-- Table pour les utilisateurs
CREATE TABLE user (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  full_name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('ADMIN','USER') NOT NULL DEFAULT 'USER'
);

-- Table pour les événements
CREATE TABLE events (
  event_id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  event_name VARCHAR(255) NOT NULL,
  event_category ENUM('SPORT','MUSIC_DANCE','THEATER','CINEMA') NOT NULL,
  event_city VARCHAR(255) NOT NULL,
  event_date DATETIME NOT NULL,
  event_details TEXT,
  event_place VARCHAR(255) NOT NULL,
  event_price DOUBLE,
  img_url TEXT,
  created_by BIGINT(20),
  FOREIGN KEY (created_by) REFERENCES user(id)
);
