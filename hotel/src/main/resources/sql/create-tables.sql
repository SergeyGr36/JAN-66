CREATE TABLE if not exists client (
    id           BIGINT SERIAL NOT NULL,
    full_name    VARCHAR(100)  NOT NULL,
    phone_number VARCHAR(20)   NOT NULL,
    email        VARCHAR(30)   NOT NULL,
    birthday     DATE          NOT NULL,
    constraint client_pk PRIMARY KEY (id));

CREATE TABLE if not exists rooms (
    id             BIGINT SERIAL NOT NULL,
    num            VARCHAR(100)  NOT NULL,
    type_id        VARCHAR(20)   NOT NULL,
    description    VARCHAR(30)   NOT NULL,
    constraint room_pk PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS TYPE_ROOM (
    ID BIGINT                   NOT NULL,
    COUNT_PLACES INT            NOT NULL,
    PRISE INT                   NOT NULL,
    DESCRIPTION VARCHAR(1000)   NOT NULL,
    CLASS_OF_ROOM VARCHAR(100)  NOT NULL,
    PRIMARY KEY (ID));

create table if not exists T_ORDER(
    id bigint auto_increment primary key,
    id_client bigint,
    id_type_room bigint,
    date_in date,
    date_out date,
    status varchar(10),
    date_create date,
    date_update date,
    id_room bigint);
