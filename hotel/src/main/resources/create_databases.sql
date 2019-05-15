drop table if exists t_order;

create table t_order(
id bigint,
id_client bigint,
id_type_room bigint,
date_in date,
date_out date,
status varchar(10),
date_create date,
date_update date,
id_room bigint,
primary key (id));