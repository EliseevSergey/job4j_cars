
INSERT INTO fuel (name) VALUES
    ('Бензин'), ('Дизель'), ('Электро'), ('Гибрид'), ('Газ');

INSERT INTO brand (name) VALUES
    ('Renault'), ('Toyota'), ('BMW'), ('Lada'), ('Kia');

INSERT INTO model (name, brand_id) VALUES
    ('Logan', 1), ('Sandero', 1), ('Duster', 1),
    ('Camry', 2), ('Corolla', 2), ('RAV4', 2),
    ('X5', 3), ('3 Series', 3), ('5 Series', 3);

INSERT INTO color (name) VALUES
    ('Синий'), ('Красный'), ('Белый'), ('Чёрный'), ('Серебристый');