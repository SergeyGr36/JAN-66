CREATE TABLE if not exists client (
    ID           BIGINT SERIAL PRIMARY KEY,
    FULL_NAME    VARCHAR(100)  NOT NULL,
    PHONE_NUMBER VARCHAR(20)   NOT NULL,
    EMAIL        VARCHAR(30)   NOT NULL,
    BIRTHDAY     DATE          NOT NULL);

CREATE TABLE if not exists rooms (
    ID             BIGINT SERIAL PRIMARY KEY,
    NUM            VARCHAR(100)  NOT NULL,
    TYPE_ID        VARCHAR(20)   NOT NULL,
    DESCRIPTION    VARCHAR(30)   NOT NULL);

CREATE TABLE IF NOT EXISTS TYPE_ROOM (
    ID              BIGINT SERIAL   PRIMARY KEY,
    COUNT_PLACES    INT             NOT NULL,
    PRISE           INT             NOT NULL,
    DESCRIPTION     VARCHAR(1000)   NOT NULL,
    CLASS_OF_ROOM   VARCHAR(100)    NOT NULL);

create table if not exists T_ORDER(
    ID BIGINT           SERIAL PRIMARY KEY,
    ID_CLIENT           BIGINT NOT NULL,
    ID_TYPE_ROOM        BIGINT,
    DATE_IN             DATE,
    DATE_OUT            DATE,
    STATUS              VARCHAR(10) NOT NULL,
    DATE_CREATE         DATE,
    DATE_UPDATE         DATE,
    ID_ROOM             BIGINT);
