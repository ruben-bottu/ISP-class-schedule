BEGIN;
SET search_path TO isp_class_schedule;

DROP TABLE lessons;
DROP TABLE class_groups;
DROP TABLE courses;

COMMIT;