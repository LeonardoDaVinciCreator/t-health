create table if not exists app_users
(
    id       bigserial primary key not null,
    username text                  not null,
    phone    bytea                 not null,
    position text,
    department text,
    avatar_url text
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

create table if not exists posts
(
    id             bigserial primary key,
    user_id        bigint    not null references app_users(id) on delete cascade,
    title          text,
    content        text,
    media_url      text,
    created_at     timestamp not null,
    likes_count    integer   default 0,
    comments_count integer   default 0
);

create table if not exists post_comments
(
    id          bigserial primary key,
    post_id     bigint    not null references posts(id)     on delete cascade,
    author_id   bigint    not null references app_users(id) on delete cascade,
    author_name text      not null,
    text        text      not null,
    created_at  timestamp not null
);

create table if not exists post_likes
(
    id         bigserial primary key,
    post_id    bigint    not null references posts(id)     on delete cascade,
    user_id    bigint    not null references app_users(id) on delete cascade,
    created_at timestamp not null,

    unique (post_id, user_id)
);

create extension if not exists pgcrypto;
create index if not exists idx_phone_app_users on app_users (phone);
create index if not exists idx_user_id_activities on activities (user_id);
create index if not exists idx_user_id_training on training (user_id);
create index if not exists idx_user_id_nutrition on nutrition (user_id);

create index if not exists idx_posts_user_id on posts(user_id);
create index if not exists idx_posts_created_at on posts(created_at desc);
create index if not exists idx_comments_post_id on post_comments(post_id);
create index if not exists idx_comments_author_id on post_comments(author_id);
create index if not exists idx_likes_post_id on post_likes(post_id);
create index if not exists idx_likes_user_id on post_likes(user_id);
