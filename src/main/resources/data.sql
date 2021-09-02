INSERT INTO WAREHOUSES (id, x, y)
VALUES ('Minsk', 50, 50),
       ('Gomel', 100, 0),
       ('Mogilev', 90, 55),
       ('Vitebsk', 60, 100),
       ('Grodno', 0, 45),
       ('Brest', 0, 0);

INSERT INTO PRODUCTS (WAREHOUSE_ID, NAME)
VALUES ('Minsk', 'iPhone'),
       ('Minsk', 'MacBook'),

       ('Gomel', 'iPad'),
       ('Gomel', 'iMac'),

       ('Mogilev', 'AirPods'),

       ('Vitebsk', 'AirPods Pro'),

       ('Grodno', 'Apple Watch'),

       ('Brest', 'AirPods Max');
