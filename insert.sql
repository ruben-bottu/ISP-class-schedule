BEGIN;
SET search_path TO isp_class_schedule;

CREATE TEMPORARY TABLE temp_lessons (
    start_timestamp     TIMESTAMP       NOT NULL,
    end_timestamp       TIMESTAMP       NOT NULL,
    course_name    	VARCHAR(200)    NOT NULL,
    class_group_name    VARCHAR(100)    NOT NULL
) ON COMMIT DROP;

INSERT INTO temp_lessons (start_timestamp, end_timestamp, course_name, class_group_name) VALUES
('2021-10-26 08:30:00', '2021-10-26 10:30:00', 'M-Probleemoplossend denken', 'ME-1TI/9'),
('2021-09-23 13:45:00', '2021-09-23 15:45:00', 'M-Computernetwerken 1', 'ME-1TI/9'),
('2021-11-26 12:45:00', '2021-11-26 13:45:00', 'ALGEMEEN / GENERAL', 'ME-1TI/9'),
('2021-11-26 10:45:00', '2021-11-26 12:45:00', 'M-Webontwikkeling 1', 'ME-1TI/9'),
('2021-10-04 13:45:00', '2021-10-04 14:45:00', 'M-Webontwikkeling 1', 'ME-1TI/9');

INSERT INTO courses (name)
SELECT DISTINCT course_name
FROM temp_lessons;

INSERT INTO class_groups (name)
SELECT DISTINCT class_group_name
FROM temp_lessons;

INSERT INTO lessons (start_timestamp, end_timestamp, course_id, class_group_id)
SELECT t.start_timestamp, t.end_timestamp, c.id, cg.id
FROM courses c INNER JOIN temp_lessons t ON c.name = t.course_name
	INNER JOIN class_groups cg ON t.class_group_name = cg.name;

COMMIT;