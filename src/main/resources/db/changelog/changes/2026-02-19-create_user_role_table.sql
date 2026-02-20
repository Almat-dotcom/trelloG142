--liquibase formatted sql

--changeset almat:1

create table t_users
(
    id       varchar(36) primary key,
    username varchar(250),
    name     varchar(100),
    surname  varchar(100),
    password varchar(250),
    created_at timestamp not null ,
    updated_at timestamp not null
);

create table t_roles
(
    id   varchar(36) primary key,
    name varchar(100),
    created_at timestamp not null ,
    updated_at timestamp not null
);

create table t_user_roles(
    user_id varchar(36) constraint fk_user references t_users(id),
    role_id varchar(36) constraint fk_role references t_roles(id),
    primary key (user_id, role_id)
)