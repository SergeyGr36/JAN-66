CREATE SCHEMA IF NOT EXISTS DEVELOPERS_TEAM;
CREATE TABLE IF NOT EXISTS bills (
    id INT NOT NULL AUTO_INCREMENT,
    docdate DATE NOT NULL,
    PRIMARY KEY (id)
);
TRUNCATE TABLE bills;