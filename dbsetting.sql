-- sudo -u postgres psql -d arinmandri
-- created_at 컬럼에 DEFAULT CURRENT_TIMESTAMP 대신 jpa에서

CREATE SCHEMA playground;

-- DROP USER "playground-backend";
CREATE USER "playground-backend" WITH PASSWORD 'bungchi^^patchi';
GRANT USAGE ON SCHEMA "playground" TO "playground-backend";
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA "playground" TO "playground-backend";
ALTER DEFAULT PRIVILEGES IN SCHEMA "playground" GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO "playground-backend";

DROP TABLE IF EXISTS "playground"."member";
CREATE TABLE "playground"."member"(
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    ,"nick" VARCHAR(20) NOT NULL
    ,"email" VARCHAR(100) NULL
        UNIQUE
    ,"created_at" TIMESTAMPTZ NOT NULL
);

DROP TABLE IF EXISTS "playground"."mkey_basic";
CREATE TABLE "playground"."mkey_basic"(
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    ,"owner__m" INT NOT NULL
        UNIQUE
        -- REFERENCES "playground"."member" -- TODO 테스트 끝나고
    ,"keyname" VARCHAR(50) NOT NULL
        UNIQUE
    ,"password" VARCHAR(50) NOT NULL
    ,"created_at" TIMESTAMPTZ NOT NULL
);
CREATE INDEX "mkey_basic__keyname" ON "playground"."mkey_basic" (keyname);

DROP TABLE IF EXISTS "playground"."post";
CREATE TABLE "playground"."post"(
    "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    ,"author__m" INT NOT NULL
        -- REFERENCES "playground"."member" -- TODO 테스트 끝나고
    ,"content" VARCHAR(255) NOT NULL
    ,"updated_at" TIMESTAMPTZ NULL
    ,"created_at" TIMESTAMPTZ NOT NULL
);

DROP TABLE IF EXISTS "playground"."refresh_token";
CREATE TABLE "playground"."refresh_token"(
    "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    ,"owner__m" INT NOT NULL
        -- REFERENCES "playground"."member" -- TODO 테스트 끝나고
    ,"refresh_token" VARCHAR(90) NOT NULL
    ,"expires_at" TIMESTAMPTZ NOT NULL
);
CREATE INDEX "refresh_token__refresh_token" ON "playground"."refresh_token" (refresh_token);

-- ---------------------------------

INSERT INTO "playground"."member" ("nick", "email", "created_at")
VALUES
('김영선', 'arinmandri@gmail.com', '2025-07-01'),
('홍길동', 'hong@naver.com', '2025-07-01'),
('킹 세종', 'king@daum.net', '2025-07-01');

INSERT INTO "playground"."mkey_basic" ("owner__m", "keyname", "password", "created_at")
VALUES
 (1, 'abc', '123', '2025-07-01')
,(3, 'www', '321', '2025-07-01')
,(2, 'sss', '222', '2025-07-01')
;

INSERT INTO "playground"."post" ("content", "author__m", "created_at")
VALUES
('글 1 가나다', 1, '2025-07-01'),
('글 2 홍길동', 2, '2025-07-01'),
('글 3 세종대왕', 3, '2025-07-01');
