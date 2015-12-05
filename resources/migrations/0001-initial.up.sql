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
       image text not null,
       key text not null
);


CREATE TABLE ingredient (
       id serial primary key,
       recipe_id int references recipe(id),
       name text not null,
       amount int not null,
       unit varchar(10) not null
);
