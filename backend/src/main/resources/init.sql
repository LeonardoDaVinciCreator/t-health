create extension if not exists pgcrypto;

create table if not exists otps(
    id          serial      not null,
    phone       bytea       not null,
    code        bytea       not null,
    created_at  timestamp   not null
);

create sequence if not exists otp_id;
create index if not exists idx_otp_phone on otps(phone);
