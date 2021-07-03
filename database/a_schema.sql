use mailChatViewDb;

CREATE TABLE user (
    id bigint not null AUTO_INCREMENT,
    user_id varchar(255),
    first_name varchar(100),
    last_name varchar(100),
    email varchar(100),
    picture_url varchar(255),
    access_token varchar(255),
    refresh_token varchar(255),
    token_expiration_timestamp bigint,
    created_time datetime,
    PRIMARY KEY (id),
    UNIQUE(user_id)
);