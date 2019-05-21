drop table if exists T_ORDER;
create table T_ORDER(id bigint, id_client bigint, id_type_room bigint, date_in date, date_out date, status varchar(10), date_create date, date_update date, id_room bigint, primary key (id));
insert into T_ORDER (ID_CLIENT, ID_TYPE_ROOM, DATE_IN, DATE_OUT, STATUS, DATE_CREATE, DATE_UPDATE, ID_ROOM, ID) values (0, 0, null, null, null, null, null, null, 1);
insert into T_ORDER (ID_CLIENT, ID_TYPE_ROOM, DATE_IN, DATE_OUT, STATUS, DATE_CREATE, DATE_UPDATE, ID_ROOM, ID) values (0, 0, null, null, null, null, null, null, 2);
