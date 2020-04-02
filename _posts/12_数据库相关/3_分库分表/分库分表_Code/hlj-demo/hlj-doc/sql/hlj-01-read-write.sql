create database `ds_0` character set 'utf8' collate 'utf8_general_ci';
create database `ds_1` character set 'utf8' collate 'utf8_general_ci';

drop table if exists  user;
create table `user`(
  `id` bigint(16) unsigned not null auto_increment,
	city varchar(20) not null,
	name varchar(20) not null,
	primary key (`id`)
) engine=innodb default charset=utf8;

