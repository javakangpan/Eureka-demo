drop table t_coffee if exists;


create table t_coffee (
    id bigint auto_increment,
    create_time timestamp,
    update_time timestamp,
    name varchar(255),
    price bigint,
    count int default 0,
    primary key (id,name)
);


insert into t_coffee (name, price, count,create_time, update_time) values ('espresso',10, 2000, now(), now());
insert into t_coffee (name, price, count,create_time, update_time) values ('latte', 10,2500, now(), now());
insert into t_coffee (name, price, count,create_time, update_time) values ('capuccino',10, 2500, now(), now());
insert into t_coffee (name, price, count,create_time, update_time) values ('mocha', 10,3000, now(), now());
insert into t_coffee (name, price, count,create_time, update_time) values ('macchiato',10, 3000, now(), now());
