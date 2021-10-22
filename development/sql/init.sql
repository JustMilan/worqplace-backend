/* Queries for Quintor Amersfoort */
INSERT INTO address (street, number , postal_code, city)
VALUES ('Maanlander', '14m', '3824 MP', 'Amersfoort');

INSERT INTO location (name, address_id)
VALUES ('Quintor Amersfoort', 1);

/* Queries for Quintor Den Bosch */
INSERT INTO address (street, number , postal_code, city)
VALUES ('Havensingel', '1', '5211 TX', 'Den Bosch');

INSERT INTO location (name, address_id)
VALUES ('Quintor Den Bosch', 2);

/* Queries for Quintor Deventer */
INSERT INTO address (street, number , postal_code, city)
VALUES ('Zutphenseweg', '6', '7418 AJ', 'Deventer');

INSERT INTO location (name, address_id)
VALUES ('Quintor Deventer', 3);

/* Queries for Quintor Den Haag */
INSERT INTO address (street, number , postal_code, city)
VALUES ('Lange Vijverberg', '4-5', '2513 AC', 'Den Haag');

INSERT INTO location (name, address_id)
VALUES ('Quintor Den Haag', 4);

/* Queries for Quintor Groningen */
INSERT INTO address (street, number , postal_code, city)
VALUES ('Ubbo Emmiussingel', '112', '9711 BK', 'Groningen');

INSERT INTO location (name, address_id)
VALUES ('Quintor Groningen', 5);

INSERT INTO room(floor, location_id)
VALUES
(3 , 5),
(-1, 5);

INSERT INTO workplace(room_id)
VALUES
(1), (1), (1), (1), (1), (1), (1), (1), (1), (1), (1), (1), (1), (1), (1), (1), (1), (1), (1), (1), (1), (1), (1), (1),
(2), (2), (2), (2), (2), (2);

/* Queries for employee */
INSERT INTO employee (last_name, first_name)
VALUES ('Avedisyan', 'Arutun'),
       ('Docters van Leeuwen', 'Daan'),
       ('Dol', 'Milan'),
       ('Mak', 'Gerson'),
       ('van der Moolen', 'Jan-paul'),
       ('Shirre', 'Said'),
       ('Wieman', 'Mees');
