use mailChatViewDb;

CREATE TABLE user (
    id bigint not null AUTO_INCREMENT,
    user_id varchar2(255),
    first_name varchar2(100),
    last_name varchar2(100),
    email varchar2(100),
    picture_url varchar2(255),
    access_token varchar2(255),
    refresh_token varchar2(255),
    created_time datetime,
    PRIMARY KEY (id),
    UNIQUE(user_id)
);