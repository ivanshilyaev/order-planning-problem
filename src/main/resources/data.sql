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
       ('Minsk', 'AirPods Pro'),
       ('Minsk', 'AirPods Max'),

       ('Gomel', 'iPhone'),
       ('Gomel', 'iPad'),
       ('Gomel', 'iMac'),
       ('Gomel', 'Apple Watch'),

       ('Mogilev', 'AirPods'),
       ('Mogilev', 'AirPods Pro'),
       ('Mogilev', 'AirPods Max'),

       ('Vitebsk', 'iPad'),
       ('Vitebsk', 'MacBook'),
       ('Vitebsk', 'iMac'),

       ('Grodno', 'iPhone'),
       ('Grodno', 'MacBook'),
       ('Grodno', 'AirPods'),

       ('Brest', 'iPad'),
       ('Brest', 'iMac'),
       ('Brest', 'Apple Watch');
