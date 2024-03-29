CREATE SEQUENCE user_message_id_seq AS integer;

CREATE TABLE IF NOT EXISTS USER_MESSAGE (
    ID INTEGER NOT NULL DEFAULT nextval('user_message_id_seq'),
    SENDER VARCHAR(50) NOT NULL,
    RECEIVER VARCHAR(50) NOT NULL,
    MESSAGE TEXT NOT NULL,
    CREATED_TIME TIMESTAMP NOT NULL,
    CONSTRAINT SENDER FOREIGN KEY (SENDER) REFERENCES USER_ACCOUNT(NICKNAME),
    CONSTRAINT RECEIVER FOREIGN KEY (RECEIVER) REFERENCES USER_ACCOUNT(NICKNAME)
);

ALTER SEQUENCE user_message_id_seq OWNED BY user_message.id;