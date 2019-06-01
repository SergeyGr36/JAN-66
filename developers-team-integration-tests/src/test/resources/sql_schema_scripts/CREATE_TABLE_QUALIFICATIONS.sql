CREATE SCHEMA IF NOT EXISTS DEVELOPERS_TEAM;
CREATE TABLE IF NOT EXISTS qualifications (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    responsibility VARCHAR (250),
    PRIMARY KEY (id)
);
TRUNCATE TABLE qualifications;