CREATE SCHEMA if NOT EXISTS TRAIN_TICKETS;
CREATE TABLE  if NOT EXISTS invoices(
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
price DECIMAL (10,2),
attributes VARCHAR (150)
);