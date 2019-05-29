CREATE TABLE if not exists client
(
    id           BIGINT SERIAL NOT NULL,
    full_name    VARCHAR(100)  NOT NULL,
    phone_number VARCHAR(20)   NOT NULL,
    email        VARCHAR(30)   NOT NULL,
    birthday     DATE          NOT NULL,
    constraint client_pk PRIMARY KEY (id)
)
