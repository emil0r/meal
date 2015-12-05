--name: sql-get-meal

SELECT name, image, description, ingredients, key AS slug FROM recipe WHERE key = :id;

--name: sql-get-meals

SELECT name, image, description, ingredients, key AS slug FROM recipe
ORDER BY id DESC
OFFSET :offset LIMIT :limit;

--name: sql-add-meal<!

INSERT INTO recipe VALUES (default, :user_id, :name, :description, :ingredients, '', :key);


--name: sql-get-meals-count
SELECT COUNT(*) FROM recipe;
