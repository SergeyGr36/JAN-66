CREATE TABLE if not exists rooms (
  id             BIGINT SERIAL NOT NULL,
  num            VARCHAR(100)    NOT NULL,
  type_id        VARCHAR(20)   NOT NULL,
  description    VARCHAR(30)   NOT NULL,
  constraint room_pk PRIMARY KEY (id))