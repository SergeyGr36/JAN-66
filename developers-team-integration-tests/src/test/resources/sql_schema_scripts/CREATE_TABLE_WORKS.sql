CREATE SCHEMA IF NOT EXISTS DEVELOPERS_TEAM;
CREATE TABLE IF NOT EXISTS works(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    price DECIMAL (10,2),
    PRIMARY KEY (id)
);
TRUNCATE TABLE works;