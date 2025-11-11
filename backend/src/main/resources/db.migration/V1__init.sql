create table if not exists app_users
(
    id       bigserial primary key not null,
    username text                  not null,
    phone    bytea                 not null
    );

create table if not exists activities
(
    id       bigserial primary key not null,
    user_id  bigint                not null references app_users(id) on delete cascade,
    value    numeric,
    type     varchar(50),
    calories double precision,
    date     timestamp             not null
    );

create table if not exists training
(
    id         serial primary key not null,
    user_id    int8               not null references app_users(id) on delete cascade,
    start_date date not null
    );

create table if not exists exercises
(
    id          serial primary key not null,
    training_id int8               not null references training(id) on delete cascade,
    title       text,
    description text,
    media       bytea,
    timing      jsonb
    );

create extension if not exists pgcrypto;
create index if not exists idx_phone_app_users on app_users (phone);
create index if not exists idx_user_id_activities on activities (user_id);
create index if not exists idx_user_id_training on training (user_id);
create index if not exists idx_training_id_exercises on exercises (training_id);