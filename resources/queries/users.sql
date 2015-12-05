-- name: sql-get-user

SELECT * FROM users WHERE oauth = :oauth;


--name: sql-create-user!

INSERT INTO users VALUES (default, :name, :oauth);
