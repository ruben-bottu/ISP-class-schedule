/*
This file contains dummy data for example purposes.

How to load my real data?
    1. create a file named secret-load.sql at the top level of the project
    2. (optional) convert your iCalendar files into SQL insert statements using the ical_to_sql_insert.py script
    3. load your data into the init_data table (created in setup.sql) by putting your SQL inserts in secret-load.sql
*/
INSERT INTO init_data (start_timestamp, end_timestamp, course_name, group_name)
VALUES ('2022-06-26 08:00:00', '2022-06-26 10:00:00', 'Algoritmisch denken', 'ME-1TI/1'),
       ('2022-06-26 10:00:00', '2022-06-26 12:00:00', 'Webontwikkeling 1', 'ME-1TI/1'),
       ('2022-06-26 14:00:00', '2022-06-26 16:00:00', 'Basis van objectgericht programmeren', 'ME-1TI/1'),

       ('2022-06-26 10:00:00', '2022-06-26 12:00:00', 'Algoritmisch denken', 'ME-1TI/2'),
       ('2022-06-26 12:00:00', '2022-06-26 14:00:00', 'Webontwikkeling 1', 'ME-1TI/2'),
       ('2022-06-26 14:00:00', '2022-06-26 16:00:00', 'Basis van objectgericht programmeren', 'ME-1TI/2'),

       ('2022-06-26 08:00:00', '2022-06-26 10:00:00', 'Basis van objectgericht programmeren', 'ME-1TI/3'),
       ('2022-06-26 12:00:00', '2022-06-26 14:00:00', 'Algoritmisch denken', 'ME-1TI/3'),
       ('2022-06-26 14:00:00', '2022-06-26 16:00:00', 'Webontwikkeling 1', 'ME-1TI/3'),

       ('2022-06-26 08:00:00', '2022-06-26 10:00:00', 'Databanken 1', 'ME-2TI/5'),
       ('2022-06-26 10:00:00', '2022-06-26 12:00:00', 'Testing', 'ME-2TI/5');
