BEGIN;

SET search_path = isp_class_schedule;

CREATE TEMPORARY TABLE temp_data
(
    start_timestamp TIMESTAMP    NOT NULL,
    end_timestamp   TIMESTAMP    NOT NULL,
    course_name     VARCHAR(255) NOT NULL,
    group_name      VARCHAR(255) NOT NULL
) ON COMMIT DROP;

INSERT INTO temp_data (start_timestamp, end_timestamp, course_name, group_name)
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

ALTER TABLE course
    ALTER COLUMN id SET DEFAULT nextval('course_seq');

ALTER TABLE "group"
    ALTER COLUMN id SET DEFAULT nextval('group_seq');

ALTER TABLE course_group
    ALTER COLUMN id SET DEFAULT nextval('course_group_seq');

ALTER TABLE class
    ALTER COLUMN id SET DEFAULT nextval('class_seq');


INSERT INTO course (name)
SELECT DISTINCT course_name
FROM temp_data;

INSERT INTO "group" (name)
SELECT DISTINCT group_name
FROM temp_data;

INSERT INTO course_group (course_id, group_id)
SELECT DISTINCT c.id, g.id
FROM "group" g
         INNER JOIN temp_data t ON g.name = t.group_name
         INNER JOIN course c ON c.name = t.course_name;

INSERT INTO class (start_timestamp, end_timestamp, course_group_id)
SELECT t.start_timestamp, t.end_timestamp, cg.id
FROM course_group cg
         INNER JOIN "group" g ON g.id = cg.group_id
         INNER JOIN course c ON c.id = cg.course_id
         INNER JOIN temp_data t ON g.name = t.group_name AND c.name = t.course_name;

/*INSERT INTO course (name)
VALUES ('Algoritmisch denken'),
       ('Webontwikkeling 1'),
       ('Basis van objectgericht programmeren'),
       ('Databanken 1'),
       ('Testing');

INSERT INTO "group" (name)
VALUES ('ME-1TI/1'),
       ('ME-1TI/2'),
       ('ME-1TI/3'),
       ('ME-2TI/5');

INSERT INTO course_group (course_id, group_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 2),
       (2, 3),
       (3, 1),
       (3, 2),
       (3, 3),
       (4, 4),
       (5, 4);


INSERT INTO class (start_timestamp, end_timestamp, course_group_id)
VALUES ('2022-06-26 08:00:00', '2022-06-26 10:00:00', 1),
       ('2022-06-26 10:00:00', '2022-06-26 12:00:00', 4),
       ('2022-06-26 14:00:00', '2022-06-26 16:00:00', 7),

       ('2022-06-26 10:00:00', '2022-06-26 12:00:00', 2),
       ('2022-06-26 12:00:00', '2022-06-26 14:00:00', 5),
       ('2022-06-26 14:00:00', '2022-06-26 16:00:00', 8),

       ('2022-06-26 08:00:00', '2022-06-26 10:00:00', 9),
       ('2022-06-26 12:00:00', '2022-06-26 14:00:00', 7),
       ('2022-06-26 14:00:00', '2022-06-26 16:00:00', 6),

       ('2022-06-26 08:00:00', '2022-06-26 10:00:00', 10),
       ('2022-06-26 10:00:00', '2022-06-26 12:00:00', 11);*/

COMMIT;
