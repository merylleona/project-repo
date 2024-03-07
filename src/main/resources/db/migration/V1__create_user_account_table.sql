CREATE SEQUENCE user_account_id_seq AS integer;

CREATE TABLE IF NOT EXISTS USER_ACCOUNT (
    ID INTEGER NOT NULL DEFAULT nextval('user_account_id_seq'),
    NICKNAME VARCHAR(50) NOT NULL UNIQUE,
    FIRSTNAME VARCHAR(50) NOT NULL,
    LASTNAME VARCHAR(50)
);

ALTER SEQUENCE user_account_id_seq OWNED BY user_account.id;