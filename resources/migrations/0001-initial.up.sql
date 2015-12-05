CREATE TABLE users (
       id bigserial primary key,
       name text not null default '',
       oauth text not null default ''
);


CREATE TABLE recipe (
       id serial primary key,
       user_id bigint references users(id),
       name text not null,
       description text not null,
       ingredients text not null,
       image text not null,
       key text not null
);
