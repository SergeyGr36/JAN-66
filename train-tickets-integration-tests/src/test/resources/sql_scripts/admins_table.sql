CREATE SCHEMA IF NOT EXISTS TRAIN_TICKETS;
CREATE TABLE IF NOT EXISTS ADMINS(
   ID BIGINT NOT NULL AUTO_INCREMENT,
   NAME VARCHAR(50) NOT NULL,
   LASTNAME VARCHAR (50) NOT NULL,
   PASSWORD VARCHAR (50) NOT NULL,
   PRIMARY KEY(ID),
);
TRUNCATE TABLE admins;