CREATE TABLE fuel (
id SERIAL PRIMARY KEY,
name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE engine (
id          SERIAL           PRIMARY KEY,
fuel_id     INT              NOT NULL REFERENCES fuel(id),
volume      DOUBLE PRECISION NOT NULL,
horse_power INT              NOT NULL
);