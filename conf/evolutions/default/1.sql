# --- First database schema

# --- !Ups

create table app_user (
  id                        bigint not null,
  email                     varchar(255),
  password                  varchar(255),
  name                      varchar(255),
  created_on                timestamp,
  updated_on                timestamp,
  deleted_on                timestamp,
  created_by                varchar(255),
  updated_by                varchar(255),
  deleted_by                varchar(255),
  constraint pk_app_user primary key (id)
);

create sequence app_user_seq start with 1000;

create table task (
  id                        bigint not null,
  title                     varchar(255),
  description               varchar(1024),
  status                    varchar(24),
  owner_id                  bigint,
  due_date                  timestamp,
  priority                  int,
  parent_task_id            bigint,
  created_on                timestamp,
  updated_on                timestamp,
  deleted_on                timestamp,
  created_by                varchar(255),
  updated_by                varchar(255),
  deleted_by                varchar(255),
  constraint pk_task primary key (id)
);

create sequence task_seq start with 1000; 

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists app_user;
drop table if exists task;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists app_user_seq;
drop sequence if exists task_seq;

