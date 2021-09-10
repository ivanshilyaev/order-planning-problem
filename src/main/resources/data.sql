INSERT INTO WAREHOUSES (id, name, x, y)
VALUES (1, 'Minsk', 50, 50),
       (2, 'Gomel', 100, 0),
       (3, 'Mogilev', 90, 55),
       (4, 'Vitebsk', 60, 100),
       (5, 'Grodno', 0, 45),
       (6, 'Brest', 0, 0);

INSERT INTO PRODUCTS (id, warehouse_id, name)
VALUES (1, 1, 'iPhone'),
       (2, 1, 'MacBook'),

       (3, 2, 'iPad'),
       (4, 2, 'iMac'),

       (5, 3, 'AirPods'),

       (6, 4, 'AirPods Pro'),

       (7, 5, 'Apple Watch'),

       (8, 6, 'AirPods Max');
