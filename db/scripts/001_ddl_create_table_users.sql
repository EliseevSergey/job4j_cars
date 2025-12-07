CREATE TABLE auto_user
(
    id        SERIAL PRIMARY KEY,
    login     VARCHAR(20) UNIQUE NOT NULL,
    password  VARCHAR     NOT NULL
);