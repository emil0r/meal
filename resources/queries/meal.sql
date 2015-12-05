--name: sql-get-meal

SELECT name, image, description, ingredients FROM recipe WHERE key = :id;

--name: sql-get-meals

SELECT name, image, description, ingredients FROM recipe;

--name: sql-add-meal<!

INSERT INTO recipe VALUES (default, :user_id, :name, :description, :ingredients, '', :key);
