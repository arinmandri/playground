CREATE SCHEMA playground;

-- DROP USER "playground-backend";
CREATE USER "playground-backend" WITH PASSWORD 'bungchi^^patchi';
GRANT USAGE ON SCHEMA "playground" TO "playground-backend";
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA "playground" TO "playground-backend";
ALTER DEFAULT PRIVILEGES IN SCHEMA "playground" GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO "playground-backend";


CREATE TABLE "playground"."post"(
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO "playground"."post" ("title", "author")
VALUES
('글제목 1', '작성자1'),
('글제목 2', '작성자2'),
('글제목 3', '작성자3');
