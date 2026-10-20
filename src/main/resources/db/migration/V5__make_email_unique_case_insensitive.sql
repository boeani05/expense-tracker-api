ALTER TABLE users DROP CONSTRAINT unique_email;
CREATE UNIQUE INDEX unique_user_email ON users (LOWER(email));