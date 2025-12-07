CREATE TABLE auto_post
(
    id                 SERIAL PRIMARY KEY,
    description        TEXT                          NOT NULL,
    created            TIMESTAMP                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    auto_user_id       INT REFERENCES auto_user (id) NOT NULL
);