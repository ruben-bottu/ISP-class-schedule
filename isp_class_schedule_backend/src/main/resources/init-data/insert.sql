-- Get ID from sequence if it is omitted from query
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
FROM init_data;

INSERT INTO "group" (name)
SELECT DISTINCT group_name
FROM init_data;

INSERT INTO course_group (course_id, group_id)
SELECT DISTINCT c.id, g.id
FROM "group" g
         INNER JOIN init_data t ON g.name = t.group_name
         INNER JOIN course c ON t.course_name = c.name;

INSERT INTO class (start_timestamp, end_timestamp, course_group_id)
SELECT start_timestamp, end_timestamp, cg.id
FROM course_group cg
         INNER JOIN "group" g ON cg.group_id = g.id
         INNER JOIN course c ON cg.course_id = c.id
         INNER JOIN init_data t ON g.name = t.group_name AND c.name = t.course_name;

-- Possible performance improvement:
/*CREATE TEMPORARY TABLE temp_course_group ON COMMIT DROP AS
SELECT c.id AS course_id, g.id AS group_id, start_timestamp, end_timestamp
FROM "group" g
         INNER JOIN temp_data t ON g.name = t.group_name
         INNER JOIN course c ON t.course_name = c.name;

INSERT INTO course_group (course_id, group_id)
SELECT DISTINCT t.course_id, t.group_id
FROM temp_course_group t;

INSERT INTO class (start_timestamp, end_timestamp, course_group_id)
SELECT t.start_timestamp, t.end_timestamp, cg.id
FROM course_group cg INNER JOIN temp_course_group t USING (course_id, group_id);*/
