-- name: sql-get-meals

SELECT * FROM recipe;

--name: sql-add-meal<!

INSERT INTO recipe VALUES (default, :user_id, :name, :description, :ingredients, '', :key);
