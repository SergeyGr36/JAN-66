CREATE SCHEMA IF NOT EXISTS TRAIN_TICKETS;
CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR (150),
    password VARCHAR(20),
    PRIMARY KEY (id)
);
TRUNCATE TABLE users;
