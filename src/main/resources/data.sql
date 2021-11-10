/* Queries for Quintor Amersfoort */
INSERT INTO address (street, house_number, postal_code, city, addition)
VALUES ('Maanlander', '14', '3824 MP', 'Amersfoort', 'm');

INSERT INTO location (name, address_id)
VALUES ('Quintor Amersfoort', 1);

/* Queries for Quintor Den Bosch */
INSERT INTO address (street, house_number, postal_code, city, addition)
VALUES ('Havensingel', '1', '5211 TX', 'Den Bosch', '');

INSERT INTO location (name, address_id)
VALUES ('Quintor Den Bosch', 2);

/* Queries for Quintor Deventer */
INSERT INTO address (street, house_number, postal_code, city, addition)
VALUES ('Zutphenseweg', '6', '7418 AJ', 'Deventer', '');

INSERT INTO location (name, address_id)
VALUES ('Quintor Deventer', 3);

/* Queries for Quintor Den Haag */
INSERT INTO address (street, house_number, postal_code, city, addition)
VALUES ('Lange Vijverberg', '4', '2513 AC', 'Den Haag', '-5');

INSERT INTO location (name, address_id)
VALUES ('Quintor Den Haag', 4);

/* Queries for Quintor Groningen */
INSERT INTO address (street, house_number, postal_code, city, addition)
VALUES ('Ubbo Emmiussingel', '112', '9711 BK', 'Groningen', '');

INSERT INTO location (name, address_id)
VALUES ('Quintor Groningen', 5);

INSERT INTO room(floor, location_id, capacity)
VALUES (3, 5, 24),
       (-1, 5, 6);

/* Queries for employee */
INSERT INTO employee (last_name, first_name)
VALUES ('Avedisyan', 'Arutun'),
       ('Docters van Leeuwen', 'Daan'),
       ('Dol', 'Milan'),
       ('Mak', 'Gerson'),
       ('van der Moolen', 'Jan-paul'),
       ('Shirre', 'Said'),
       ('Wieman', 'Mees');
