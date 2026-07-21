CREATE TABLE history_owner(
car_id INT NOT NULL REFERENCES car(id),
owner_id INT NOT NULL REFERENCES owner(id),
UNIQUE (car_id, owner_id)
);