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
    id            serial primary key not null,
    user_id       bigint             not null references app_users(id) on delete cascade,
    title         varchar(100),
    type          varchar(100)       not null,
    duration_secs bigint             not null,
    calories      int8,
    date          timestamp          not null
    );

create table if not exists nutrition
(
    id            serial primary key not null,
    user_id       bigint             not null references app_users(id) on delete cascade,
    meal_name     varchar(100)       not null,
    meal_calories integer,
    meal_type     varchar(100)       not null,
    parameters    jsonb,
    date          timestamp          not null
);

create extension if not exists pgcrypto;
create index if not exists idx_phone_app_users on app_users (phone);
create index if not exists idx_user_id_activities on activities (user_id);
create index if not exists idx_user_id_training on training (user_id);
create index if not exists idx_user_id_nutrition on nutrition (user_id);