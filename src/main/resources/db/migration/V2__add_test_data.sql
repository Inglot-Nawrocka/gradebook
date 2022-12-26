INSERT INTO teacher (id, first_name, last_name, address, phone_number, email)
VALUES (1, 'Anna', 'Nowak', 'ul. Szkolna 55', '1234', 'email@com'),
       (2, 'Robert', 'Kania', 'ul. Jasna 43', '1234', 'email@com');

INSERT INTO class_group (id, name, school_year)
VALUES (1, '6B', '2021'), (default, '7A', '2021');

INSERT INTO student (id, first_name, last_name, address, phone_number, email, class_group_id)
VALUES (1, 'Albert', 'Einstein', 'ul. Mokra 7', '1234', 'email@com', 1),
       (2, 'Marta', 'Lewandowska', 'os. Bajkowe 33', '1234', 'email@com', 2),
       (3, 'Maciej', 'Kot', 'ul. Fajna 10', '1234', 'email@com', 1),
       (4, 'Karolina', 'Nowakowska', 'ul. Kwiatowa 22', '1234', 'email@com', 2);

INSERT INTO subject (id, name)
VALUES (1, 'Biologia'),
       (2, 'Informatyka'),
       (3, 'Matematyka'),
       (4, 'Geografia'),
       (5, 'J.Polski'),
       (6, 'J.Angielski'),
       (7, 'Fizyka'),
       (8, 'Chemia'),
       (9, 'W-F'),
       (10, 'Technika');