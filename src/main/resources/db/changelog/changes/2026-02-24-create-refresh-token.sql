--liquibase formatted sql
--changeset almat:2

create table t_refresh_tokens
(
    id         uuid primary key,
    user_id    uuid not null constraint fk_refresh_user references t_users(id),
    token_hash varchar(64) not null,
    expires_at timestamp not null,
    revoked    boolean not null default false,
    created_at timestamp not null,
    updated_at timestamp not null
);

create table t_password_reset_tokens
(
    id         uuid primary key,
    user_id    uuid not null constraint fk_reset_user references t_users(id),
    token_hash varchar(64) not null,
    expires_at timestamp not null,
    used       boolean not null default false,
    created_at timestamp not null,
    updated_at timestamp not null
);