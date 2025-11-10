create table if not exists app_users
(
    id       serial  primary key not null,
    username varchar(68)         not null,
    phone    bytea               not null
    );

create table if not exists activities
(
    id       serial primary key not null,
    date     timestamp          not null,
    user_id  int8               not null references app_users(id) on delete cascade,
    steps    int8,
    value    text,
    goals    text[],
    calories float8
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