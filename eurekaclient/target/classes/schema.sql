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

drop table student if exists;

CREATE TABLE student (
  id int(11) /*unsigned NOT NULL*/ AUTO_INCREMENT,
  name varchar(200) /*COLLATE utf8_bin DEFAULT NULL*/,
  age tinyint(3) /*unsigned DEFAULT NULL*/,
  PRIMARY KEY (id)
) /*ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin*/;
/*LOCK TABLES student WRITE;*/
INSERT INTO student (id, name, age) VALUES (1,'点点',16),(2,'平平',16),(3,'美美',16),(4,'团团',16);
/*UNLOCK TABLES;*/
drop table class if exists;

CREATE TABLE class (
  id int(11) /*unsigned NOT NULL*/ AUTO_INCREMENT,
  name varchar(20) /*COLLATE utf8_bin DEFAULT NULL*/,
  PRIMARY KEY (id)
) /*ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin*/;

INSERT INTO class (id, name) VALUES (1,'一班'),(2,'二班');

drop table classroom if exists;

CREATE TABLE classroom (
  id int(11)/* unsigned NOT NULL*/ AUTO_INCREMENT,
  class_id int(11)/* DEFAULT NULL*/,
  student_id int(11) /*DEFAULT NULL*/,
  PRIMARY KEY (id)
) /*ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin*/;

INSERT INTO classroom (id, class_id, student_id) VALUES (1,1,1),(2,1,2),(3,2,3),(4,2,4);

DROP TABLE logs IF EXISTS ;
CREATE TABLE logs  (
  id bigint(20) AUTO_INCREMENT,
  create_time datetime,
  description varchar(255),
  exception_detail text,
  log_type varchar(255),
  method varchar(255),
  params text,
  request_ip varchar(255),
  time bigint(20),
  username varchar(255),
  address varchar(255),
  browser varchar(255),
  return_value text,
  PRIMARY KEY (id)
);