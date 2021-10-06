/* Queries for Quintor Amersfoort */
INSERT INTO adress (street, number , postal_code, city)
VALUES ('Maanlander', '14m', '3824 MP', 'Amersfoort');

INSERT INTO location (name, adress_id)
VALUES ('Quintor Amersfoort', 1);

INSERT INTO openinghours (day, date, open, close, location_id)
VALUES
('MONDAY', '2021-10-04', '08:00:00', '18:00:00', 1),
('TUESDAY', '2021-10-05', '08:00:00', '18:00:00', 1),
('WEDNESDAY', '2021-10-06', '08:00:00', '18:00:00', 1),
('THURSDAY', '2021-10-07', '08:00:00', '18:00:00', 1),
('FRIDAY', '2021-10-08', '08:00:00', '18:00:00', 1),

('MONDAY', '2021-10-11', '08:00:00', '18:00:00', 1),
('TUESDAY', '2021-10-12', '08:00:00', '18:00:00', 1),
('WEDNESDAY', '2021-10-13', '08:00:00', '18:00:00', 1),
('THURSDAY', '2021-10-14', '08:00:00', '18:00:00', 1),
('FRIDAY', '2021-10-15', '08:00:00', '18:00:00', 1);

/* Queries for Quintor Den Bosch */
INSERT INTO adress (street, number , postal_code, city)
VALUES ('Havensingel', '1', '5211 TX', 'Den Bosch');

INSERT INTO location (name, adress_id)
VALUES ('Quintor Den Bosch', 2);

INSERT INTO openinghours (day, date, open, close, location_id) -- Openingstijden ontbreken op google dus dit is een dummy
VALUES
('MONDAY', '2021-10-04', '08:00:00', '18:00:00', 2),
('TUESDAY', '2021-10-05', '08:00:00', '18:00:00', 2),
('WEDNESDAY', '2021-10-06', '08:00:00', '18:00:00', 2),
('THURSDAY', '2021-10-07', '08:00:00', '18:00:00', 2),
('FRIDAY', '2021-10-08', '08:00:00', '18:00:00', 2),

('MONDAY', '2021-10-11', '08:00:00', '18:00:00', 2),
('TUESDAY', '2021-10-12', '08:00:00', '18:00:00', 2),
('WEDNESDAY', '2021-10-13', '08:00:00', '18:00:00', 2),
('THURSDAY', '2021-10-14', '08:00:00', '18:00:00', 2),
('FRIDAY', '2021-10-15', '08:00:00', '18:00:00', 2);

/* Queries for Quintor Deventer */
INSERT INTO adress (street, number , postal_code, city)
VALUES ('Zutphenseweg', '6', '7418 AJ', 'Deventer');

INSERT INTO location (name, adress_id)
VALUES ('Quintor Deventer', 3);

INSERT INTO openinghours (day, date, open, close, location_id) -- Openingstijden ontbreken op google dus dit is een dummy
VALUES
('MONDAY', '2021-10-04', '08:00:00', '18:00:00', 3),
('TUESDAY', '2021-10-05', '08:00:00', '18:00:00', 3),
('WEDNESDAY', '2021-10-06', '08:00:00', '18:00:00', 3),
('THURSDAY', '2021-10-07', '08:00:00', '18:00:00', 3),
('FRIDAY', '2021-10-08', '08:00:00', '18:00:00', 3),

('MONDAY', '2021-10-11', '08:00:00', '18:00:00', 3),
('TUESDAY', '2021-10-12', '08:00:00', '18:00:00', 3),
('WEDNESDAY', '2021-10-13', '08:00:00', '18:00:00', 3),
('THURSDAY', '2021-10-14', '08:00:00', '18:00:00', 3),
('FRIDAY', '2021-10-15', '08:00:00', '18:00:00', 3);

/* Queries for Quintor Den Haag */
INSERT INTO adress (street, number , postal_code, city)
VALUES ('Lange Vijverberg', '4-5', '2513 AC', 'Den Haag');

INSERT INTO location (name, adress_id)
VALUES ('Quintor Den Haag', 4);

INSERT INTO openinghours (day, date, open, close, location_id)
VALUES
('MONDAY', '2021-10-04', '08:30:00', '17:00:00', 4),
('TUESDAY', '2021-10-05', '08:30:00', '17:00:00', 4),
('WEDNESDAY', '2021-10-06', '08:30:00', '17:00:00', 4),
('THURSDAY', '2021-10-07', '08:30:00', '17:00:00', 4),
('FRIDAY', '2021-10-08', '08:30:00', '17:00:00', 4),

('MONDAY', '2021-10-11', '08:30:00', '17:00:00', 4),
('TUESDAY', '2021-10-12', '08:30:00', '17:00:00', 4),
('WEDNESDAY', '2021-10-13', '08:30:00', '17:00:00', 4),
('THURSDAY', '2021-10-14', '08:30:00', '17:00:00', 4),
('FRIDAY', '2021-10-15', '08:30:00', '17:00:00', 4);

/* Queries for Quintor Groningen */
INSERT INTO adress (street, number , postal_code, city)
VALUES ('Ubbo Emmiussingel', '112', '9711 BK', 'Groningen');

INSERT INTO location (name, adress_id)
VALUES ('Quintor Groningen', 5);

INSERT INTO openinghours (day, date, open, close, location_id)
VALUES
('MONDAY', '2021-10-04', '08:30:00', '17:00:00', 5),
('TUESDAY', '2021-10-05', '08:30:00', '17:00:00', 5),
('WEDNESDAY', '2021-10-06', '08:30:00', '17:00:00', 5),
('THURSDAY', '2021-10-07', '08:30:00', '17:00:00', 5),
('FRIDAY', '2021-10-08', '08:30:00', '17:00:00', 5),

('MONDAY', '2021-10-11', '08:30:00', '17:00:00', 5),
('TUESDAY', '2021-10-12', '08:30:00', '17:00:00', 5),
('WEDNESDAY', '2021-10-13', '08:30:00', '17:00:00', 5),
('THURSDAY', '2021-10-14', '08:30:00', '17:00:00', 5),
('FRIDAY', '2021-10-15', '08:30:00', '17:00:00', 5);

INSERT INTO employee (last_name, name)
VALUES ('Avedisyan', 'Arutun'),
       ('Docters van Leeuwen', 'Daan'),
       ('Dol', 'Milan'),
       ('Mak', 'Gerson'),
       ('van der Moolen', 'Jan-paul'),
       ('Shirre', 'Said'),
       ('Wieman', 'Mees');

INSERT INTO room(floor, number, location_id)
VALUES
       ('ATTIC', 1 , 5);

INSERT INTO workplace(number, room_id)
VALUES
(1, 1),
(2, 1);

INSERT INTO timeslot(date, start_time, end_time, workplace_id)
VALUES
('2021-10-05', '10:00:00', '10:30:00', 1),
('2021-10-05', '10:30:00', '11:00:00', 1),
('2021-10-05', '11:00:00', '11:30:00', 1),
('2021-10-05', '11:30:00', '12:00:00', 1),
('2021-10-05', '12:00:00', '12:30:00', 1),
('2021-10-05', '12:30:00', '13:00:00', 1),

('2021-10-05', '10:00:00', '10:30:00', 2),
('2021-10-05', '10:30:00', '11:00:00', 2),
('2021-10-05', '11:00:00', '11:30:00', 2),
('2021-10-05', '11:30:00', '12:00:00', 2),
('2021-10-05', '12:00:00', '12:30:00', 2),
('2021-10-05', '12:30:00', '13:00:00', 2);