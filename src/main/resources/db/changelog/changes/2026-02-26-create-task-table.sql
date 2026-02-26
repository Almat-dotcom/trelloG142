--liquibase formatted sql
--changeset almat:3

create table t_tasks
(
    id         uuid primary key,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    creator_id uuid NOT NULL,
    assignee_id uuid,
    CONSTRAINT fk_tasks_creator FOREIGN KEY (creator_id) REFERENCES t_users(id),
    CONSTRAINT fk_tasks_assignee FOREIGN KEY (assignee_id) REFERENCES t_users(id),
    created_at timestamp not null,
    updated_at timestamp not null
);