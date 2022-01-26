create table file_info
(
  id bigserial primary key,
  hash UUID,
  filename varchar(50),
  sub_type int
);