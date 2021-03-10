CREATE DATABASE films2;

CREATE TABLE  directors(
id SERIAL PRIMARY KEY,
name VARCHAR(45) NOT NULL,
surname VARCHAR(45) NOT NULL
birth_date DATE);

CREATE TABLE films(
id SERIAL PRIMARY KEY,
name VARCHAR(45) NOT NULL,
year INT NOT NULL,
directors_id INT,
CONSTRAINT fk_films_directors
FOREIGN KEY (directors_id) REFERENCES directors (id));
   
CREATE TABLE genres(
id SERIAL PRIMARY KEY,
name VARCHAR(45) NOT NULL);
  
CREATE TABLE genres_films(
genres_id INT NOT NULL,
films_id INT NOT NULL,
PRIMARY KEY (genres_id, films_id),
CONSTRAINT fk_genres_films
FOREIGN KEY (genres_id) REFERENCES genres (id),
CONSTRAINT fk_genres_films1
FOREIGN KEY (films_id) REFERENCES films (id));
