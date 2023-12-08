/*
This file contains dummy data for example purposes.

How to load my real data?
    1. create a file named secret-load.sql at the top level of the project
    2. (optional) convert your iCalendar files into SQL insert statements using the ical_to_sql_insert.py script
    3. load your data into the init_data table (created in setup.sql) by putting your SQL inserts in secret-load.sql
*/
INSERT INTO init_data (start_timestamp, end_timestamp, course_name, group_name)
VALUES ('2022-06-26 08:00:00', '2022-06-26 10:00:00', 'Computer Systems',        'Y1G1'),
       ('2022-06-26 10:00:00', '2022-06-26 12:00:00', 'Web Development',         'Y1G1'),
       ('2022-06-26 14:00:00', '2022-06-26 16:00:00', 'Mobile Applications',     'Y1G1'),

       ('2022-06-26 10:00:00', '2022-06-26 12:00:00', 'Computer Systems',        'Y1G2'),
       ('2022-06-26 12:00:00', '2022-06-26 14:00:00', 'Web Development',         'Y1G2'),
       ('2022-06-26 14:00:00', '2022-06-26 16:00:00', 'Mobile Applications',     'Y1G2'),

       ('2022-06-26 08:00:00', '2022-06-26 10:00:00', 'Mobile Applications',     'Y1G3'),
       ('2022-06-26 12:00:00', '2022-06-26 14:00:00', 'Computer Systems',        'Y1G3'),
       ('2022-06-26 14:00:00', '2022-06-26 16:00:00', 'Web Development',         'Y1G3'),
       
       ('2022-06-26 08:00:00', '2022-06-26 10:00:00', 'Spaceship Technologies',  'Y2G1'),
       ('2022-06-26 10:00:00', '2022-06-26 12:00:00', 'Design',                  'Y2G1');
