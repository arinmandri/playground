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
ALTER ROLE "{ 사용자 }" SET search_path = "playground", public;

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
    ,"belongs_to__m" INT NOT NULL
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

DROP TABLE IF EXISTS "playground"."p_attachment_image";
CREATE TABLE "playground"."p_attachment_image"(
    "id" BIGINT PRIMARY KEY
    ,"belongs_to__p" INT NOT NULL
        REFERENCES "playground"."post"
    ,"order" SMALLINT NOT NULL
    ,"url" VARCHAR(500) NOT NULL
    ,"created_at" TIMESTAMPTZ NOT NULL
);

DROP TABLE IF EXISTS "playground"."p_attachment_file";
CREATE TABLE "playground"."p_attachment_file"(
    "id" BIGINT  PRIMARY KEY
    ,"belongs_to__p" INT NOT NULL
        REFERENCES "playground"."post"
    ,"order" SMALLINT NOT NULL
    ,"url" VARCHAR(500) NOT NULL
    ,"size" SMALLINT NOT NULL -- 단위: KB
    ,"created_at" TIMESTAMPTZ NOT NULL
);

DROP SEQUENCE IF EXISTS "playground"."pattachment_seq";
CREATE SEQUENCE "playground"."pattachment_seq"
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 50
;
GRANT USAGE, SELECT, UPDATE ON SEQUENCE "playground"."pattachment_seq" TO "{ 사용자 }"
;



DROP TABLE IF EXISTS "playground"."refresh_token";
CREATE TABLE "playground"."refresh_token"(
    "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    ,"belongs_to__m" INT NOT NULL
        -- REFERENCES "playground"."member" -- TODO 테스트 끝나고
    ,"refresh_token" VARCHAR(90) NOT NULL
    ,"expires_at" TIMESTAMPTZ NOT NULL
);
CREATE INDEX "refresh_token__refresh_token" ON "playground"."refresh_token" (refresh_token);
