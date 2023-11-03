-- Get ID from sequence if it is omitted from query
ALTER TABLE course
    ALTER COLUMN id SET DEFAULT nextval('course_seq');

ALTER TABLE group_
    ALTER COLUMN id SET DEFAULT nextval('group__seq');

ALTER TABLE course_group
    ALTER COLUMN id SET DEFAULT nextval('course_group_seq');

ALTER TABLE class
    ALTER COLUMN id SET DEFAULT nextval('class_seq');

INSERT INTO course (name)
SELECT DISTINCT course_name
FROM init_data;

INSERT INTO group_ (name)
SELECT DISTINCT group_name
FROM init_data;

INSERT INTO course_group (course_id, group_id)
SELECT DISTINCT c.id, g.id
FROM group_ g
         INNER JOIN init_data t ON g.name = t.group_name
         INNER JOIN course c ON t.course_name = c.name;

-- DISTINCT is necessary here because in our dataset there are duplicate classes
-- where only the description differs. Since we consider these classes to all be
-- the same class, DISTINCT is used.
INSERT INTO class (start_timestamp, end_timestamp, course_group_id)
SELECT DISTINCT start_timestamp, end_timestamp, cg.id
FROM course_group cg
         INNER JOIN group_ g ON cg.group_id = g.id
         INNER JOIN course c ON cg.course_id = c.id
         INNER JOIN init_data t ON g.name = t.group_name AND c.name = t.course_name;

/* Performance results using ANALYSE EXPLAIN in PostgreSQL:

Queries without temp table:
    course_group:       Insert on course_group  (cost=30.83..33.03 rows=0 width=0)
    class:              Insert on class  (cost=33.75..42.26 rows=0 width=0)

Queries with temp table:
    create temp table:  Hash Join  (cost=3.15..27.05 rows=756 width=32)
    course_group:       Insert on course_group  (cost=30.40..34.90 rows=0 width=0)
    class:              Insert on class  (cost=32.98..33.02 rows=0 width=0)*/
