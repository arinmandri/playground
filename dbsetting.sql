-- sudo -u postgres psql -d arinmandri
-- created_at 컬럼에 DEFAULT CURRENT_TIMESTAMP 대신 jpa에서

-- DB: arinmandri
-- ALTER SCHEMA playground RENAME TO playground_trash
CREATE SCHEMA playground;

-- DROP USER "{ 사용자 }";
CREATE USER "{ 사용자 }" WITH PASSWORD '{ 비번 }';
GRANT USAGE ON SCHEMA "playground" TO "{ 사용자 }";
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA "playground" TO "{ 사용자 }";
ALTER DEFAULT PRIVILEGES IN SCHEMA "playground" GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO "{ 사용자 }";

DROP TABLE IF EXISTS "playground"."member";
CREATE TABLE "playground"."member"(-- m
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    ,"nick" VARCHAR(20) NOT NULL
    ,"email" VARCHAR(100) NULL
        UNIQUE
    ,"propic" VARCHAR(256) NULL
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
    ,"password" CHAR(160) NOT NULL
    ,"created_at" TIMESTAMPTZ NOT NULL
);
CREATE INDEX "mkey_basic__keyname" ON "playground"."mkey_basic" (keyname);



DROP TABLE IF EXISTS "playground"."post";
CREATE TABLE "playground"."post"(-- p
    "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    ,"author__m" INT NOT NULL
        -- REFERENCES "playground"."member" -- TODO 테스트 끝나고
    ,"content" VARCHAR(500) NOT NULL
    -- TODO 첨부물 수?
    ,"updated_at" TIMESTAMPTZ NULL
    ,"created_at" TIMESTAMPTZ NOT NULL
);

DROP TABLE IF EXISTS "playground"."p_att_image";
CREATE TABLE "playground"."p_att_image"(
    "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    ,"owner__p" INT NOT NULL
        REFERENCES "playground"."post"
    ,"order" SMALLINT NOT NULL
    ,"url" VARCHAR(500) NOT NULL
    ,"created_at" TIMESTAMPTZ NOT NULL
);

DROP TABLE IF EXISTS "playground"."p_att_ex1";
CREATE TABLE "playground"."p_att_ex1"(-- TEST 사실 게시글에 이미지 말고 올릴 것도 없다. 그래도 이미지 외에도 첨부물의 종류가 여럿이 가능하게 하고 싶다. 이것은 그냥 '이미지가 아닌 다른 첨부물'을 나타낸다. 나중에 뭐로든지 대체하려고 한다. 투표라든지.
    "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    ,"owner__p" INT NOT NULL
        REFERENCES "playground"."post"
    ,"order" SMALLINT NOT NULL
    ,"text1" VARCHAR(500) NOT NULL
    ,"int1" INT NOT NULL
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
