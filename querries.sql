SET search_path TO isp_class_schedule;

-- Gives wrong result (old query)
SELECT count(*) / 2 AS collision_count
FROM lessons s1
WHERE EXISTS (
	SELECT '1'
	FROM lessons s2
	WHERE s1.id <> s2.id AND
			(s1.start_timestamp, s1.end_timestamp) OVERLAPS
		  	(s2.start_timestamp, s2.end_timestamp)
)

-- Gives correct result (new query)
SELECT count(*) / 2 AS collision_count
FROM lessons s1 INNER JOIN lessons s2 ON s1.id <> s2.id
WHERE (s1.start_timestamp, s1.end_timestamp) OVERLAPS
	  (s2.start_timestamp, s2.end_timestamp)
-- Replace program_name by program_code
-- AND (course_name, program_name) IN (...)

CREATE TEMPORARY TABLE IF NOT EXISTS selected_lessons AS
	SELECT id, start_timestamp, end_timestamp
	FROM input_data INNER JOIN lessons USING (course_id, class_group_id);

SELECT count(*) / 2 AS collision_count
FROM selected_lessons s1 INNER JOIN selected_lessons s2 ON s1.id <> s2.id
WHERE (s1.start_timestamp, s1.end_timestamp) OVERLAPS (s2.start_timestamp, s2.end_timestamp)

===============================================================================================

CREATE TEMPORARY TABLE input_data (
	course_id           INT             NOT NULL,
    class_group_id      INT             NOT NULL
) ON COMMIT DROP;

INSERT INTO input_data VALUES
(1, 3), -- Algo		/3
(3, 1), -- BOP		/1
(4, 4), -- Data 1	/5
(5, 4); -- Testing	/5

CREATE TEMPORARY TABLE IF NOT EXISTS selected_lessons AS
	SELECT id, start_timestamp, end_timestamp
	FROM lessons s
	WHERE EXISTS (
		SELECT '1'
		FROM input_data i
		WHERE i.course_id = s.course_id AND i.class_group_id = s.class_group_id
	);

SELECT count(*) / 2 AS collision_count
FROM selected_lessons s1 INNER JOIN selected_lessons s2 ON s1.id <> s2.id
WHERE (s1.start_timestamp, s1.end_timestamp) OVERLAPS
	  (s2.start_timestamp, s2.end_timestamp)

================================================================================================

SET search_path TO isp_class_schedule;

CREATE TEMPORARY TABLE temp_data (
	course_id           INT             NOT NULL,
    class_group_id      INT             NOT NULL
) ON COMMIT DROP;

INSERT INTO temp_data VALUES
(1, 3), -- Algo		/3
(3, 3), -- BOP		/1
(4, 4), -- Data 1	/5
(5, 4); -- Testing	/5

CREATE TYPE course_class_group_ids AS (course_id INT, class_group_id INT);

CREATE OR REPLACE FUNCTION calculate_number_of_collisions(input_data course_class_group_ids)
RETURNS INT AS $$
	CREATE TEMPORARY TABLE selected_lessons AS
		SELECT id, start_timestamp, end_timestamp
		FROM lessons s
		WHERE EXISTS (
			SELECT '1'
			FROM input_data i
			WHERE i.course_id = s.course_id AND i.class_group_id = s.class_group_id
		);

	SELECT count(*) / 2 AS collision_count
	FROM selected_lessons s1 INNER JOIN selected_lessons s2 ON s1.id <> s2.id
	WHERE (s1.start_timestamp, s1.end_timestamp) OVERLAPS
		  (s2.start_timestamp, s2.end_timestamp);
$$ LANGUAGE SQL IMMUTABLE;

SELECT calculate_number_of_collisions(temp_data)

================================================================================================

CREATE OR REPLACE FUNCTION testRowConstr(course_class_group_ids TEXT) RETURNS INT
	AS $body$
	DECLARE
		sqlquery TEXT;
		collision_count INT;
	BEGIN
		sqlquery := FORMAT($$
			SELECT count(*)
			FROM (VALUES %s) AS result_table(course_id, class_group_id)
			$$, $1);
		EXECUTE sqlquery INTO collision_count;
		RETURN collision_count;
	END;
	$body$
	LANGUAGE plpgsql;

SELECT testRowConstr('(1,2), (3,1), (4,4)')

================================================================================================

CREATE OR REPLACE FUNCTION create_query(course_class_group_ids TEXT) RETURNS TEXT
	AS $$ SELECT FORMAT('SELECT count(*) FROM (VALUES %s) AS result_table(course_id, class_group_id)', $1) $$
	LANGUAGE SQL;

CREATE OR REPLACE FUNCTION testRowConstr(course_class_group_ids TEXT) RETURNS INT
	AS $body$
	DECLARE
		sqlquery TEXT;
		collision_count INT;
	BEGIN
		sqlquery := create_query($1);
		EXECUTE sqlquery INTO collision_count;
		RETURN collision_count;
	END;
	$body$
	LANGUAGE plpgsql;

SELECT testRowConstr('(1,2), (3,1), (4,4)')

Even less code:

-- collision_query
CREATE OR REPLACE FUNCTION create_query(course_ids_class_group_ids TEXT) RETURNS TEXT
	AS $$ SELECT FORMAT('SELECT count(*) FROM (VALUES %s) AS result_table(course_id, class_group_id)', $1) $$
	LANGUAGE SQL;


-- count_collisions
CREATE OR REPLACE FUNCTION testRowConstr(course_ids_class_group_ids TEXT) RETURNS INT
	AS $$
	DECLARE
		collision_count INT;
	BEGIN
		EXECUTE create_query($1) INTO collision_count;
		RETURN collision_count;
	END;
	$$
	LANGUAGE plpgsql;

SELECT testRowConstr('(1,2), (3,1), (4,4)')

================================================================================================

-- Ideally the type of course_ids_class_group_ids would look like this:
-- course_ids_class_group_ids	TABLE(course_id INT, class_group_id INT)
CREATE OR REPLACE FUNCTION collision_query(course_ids_class_group_ids TEXT) RETURNS TEXT
	AS $body$ SELECT FORMAT($$

	-- ON COMMIT DROP can be removed if we work with pooling connections
	CREATE TEMPORARY TABLE IF NOT EXISTS selected_lessons ON COMMIT DROP AS
		SELECT id, start_timestamp, end_timestamp
		FROM lessons s
		WHERE EXISTS (
			SELECT '1'
			FROM (VALUES %s) AS i(course_id, class_group_id)
			WHERE i.course_id = s.course_id AND i.class_group_id = s.class_group_id
		);

	SELECT count(*) / 2 AS collision_count
	FROM selected_lessons s1 INNER JOIN selected_lessons s2 ON s1.id <> s2.id
	WHERE (s1.start_timestamp, s1.end_timestamp) OVERLAPS
		  (s2.start_timestamp, s2.end_timestamp)

	$$, course_ids_class_group_ids)
	$body$
	LANGUAGE SQL;


CREATE OR REPLACE FUNCTION count_collisions(course_ids_class_group_ids TEXT) RETURNS INT
	AS $$
	DECLARE
		collision_count INT;
	BEGIN
		EXECUTE collision_query(course_ids_class_group_ids) INTO STRICT collision_count;
		RETURN collision_count;
	END;
	$$
	LANGUAGE plpgsql;

SELECT count_collisions('(1,3),(3,3),(4,4),(5,4)')

================================================================================================

CREATE TEMP TABLE IF NOT EXISTS temp_tbl ON COMMIT DROP AS
	SELECT DISTINCT course_id, class_group_id
	FROM lessons;

SELECT ARRAY [(s1.course_id, s1.class_group_id), (s2.course_id, s2.class_group_id), (s3.course_id, s3.class_group_id)]
FROM temp_tbl s1	INNER JOIN temp_tbl s2 ON s2.course_id NOT IN (s1.course_id)
					INNER JOIN temp_tbl s3 ON s3.course_id NOT IN (s1.course_id, s2.course_id)
ORDER BY 1

================================================================================================

CREATE TEMP TABLE IF NOT EXISTS temp_tbl ON COMMIT DROP AS
	SELECT DISTINCT course_id, class_group_id
	FROM lessons l
	WHERE l.course_id IN (2, 3);

SELECT ARRAY [(s1.course_id, s1.class_group_id), (s2.course_id, s2.class_group_id)] --, (s3.course_id, s3.class_group_id)]
FROM temp_tbl s1	INNER JOIN temp_tbl s2 ON s2.course_id NOT IN (s1.course_id)
					--INNER JOIN temp_tbl s3 ON s3.course_id NOT IN (s1.course_id, s2.course_id)
ORDER BY 1

================================================================================================

-- Make an array of a user defined type

CREATE TYPE course_id_class_group_id AS (course_id INT, class_group_id INT);

DROP FUNCTION IF EXISTS test2;
CREATE OR REPLACE FUNCTION test2(course_id_class_group_id[]) RETURNS TEXT
 	AS $$
 	SELECT $1
 	$$
 	LANGUAGE SQL;

SELECT test2(CAST (ARRAY [(1,3),(3,3),(4,4),(5,4)] AS course_id_class_group_id[]))

================================================================================================

CREATE OR REPLACE FUNCTION test5(selected_courses_count INT) RETURNS TEXT
	AS $$
	DECLARE
		select_clause	TEXT := 'SELECT CAST (ARRAY [(s1.course_id, s1.class_group_id)';
		from_clause		TEXT := 'FROM selected_courses_permutations s1';
	BEGIN
		-- Construct SELECT
		FOR i IN 2..selected_courses_count LOOP
			select_clause := select_clause || FORMAT(', (s%1$s.course_id, s%1$s.class_group_id)', i);
		END LOOP;
		
		-- Construct FROM
		FOR i IN 2..selected_courses_count LOOP
			from_clause := from_clause 
			|| FORMAT(' INNER JOIN selected_courses_permutations s%1$s ON s%1$s.course_id NOT IN (s1.course_id', i);
			
			-- Make sure that every join uses a different course_id
			FOR j IN 2..(i-1) LOOP
				from_clause := from_clause || FORMAT(', s%s.course_id', j);
			END LOOP;
			from_clause := from_clause || ')';
		END LOOP;
		
		select_clause := select_clause || '] AS course_id_class_group_id[]) AS permutation';
		RETURN select_clause || ' ' || from_clause || ' ORDER BY 1';
	END;
	$$
	LANGUAGE plpgsql;

SELECT test5(3)

================================================================================================

CREATE OR REPLACE FUNCTION test7(input_course_id INT) RETURNS SETOF course_id_class_group_id
	AS $$
		SELECT DISTINCT course_id, class_group_id
		FROM lessons
		WHERE course_id = input_course_id;
	$$
	LANGUAGE SQL;

SELECT test7(5);

-- SELECT ARRAY [(s1.course_id, s1.class_group_id), (s2.course_id, s2.class_group_id)]::course_id_class_group_id[] --, (s3.course_id, s3.class_group_id)]::course_id_class_group_id[]
-- FROM test7(2) s1 CROSS JOIN test7(3) s2
-- ORDER BY 1

-- TODO replace test7() by actual method name
CREATE OR REPLACE FUNCTION test5(selected_course_ids INT ARRAY) RETURNS TEXT
	AS $$
	DECLARE
		number_of_courses	INT	 		DEFAULT array_length(selected_course_ids, 1);
		select_array 		TEXT ARRAY 	DEFAULT ARRAY[]::TEXT[];
		from_array 			TEXT ARRAY 	DEFAULT ARRAY[]::TEXT[];
	BEGIN
		-- Construct SELECT
		FOR i IN 1..number_of_courses LOOP
			select_array := select_array || FORMAT('(s%1$s.course_id, s%1$s.class_group_id)', i);
		END LOOP;
		
		-- Construct FROM
		FOR i IN 1..number_of_courses LOOP
			from_array := from_array || FORMAT('test7(%s) s%s', selected_course_ids[i], i);
		END LOOP;
		
		RETURN FORMAT('SELECT CAST (ARRAY [%s] AS course_id_class_group_id ARRAY) AS permutation FROM %s', 
					  array_to_string(select_array, ','), 
					  array_to_string(from_array, ' CROSS JOIN '))
					  || ' ORDER BY 1';
	END;
	$$
	LANGUAGE plpgsql;
	
SELECT test5(ARRAY [2, 3]);

CREATE OR REPLACE FUNCTION test6(ints INT ARRAY) RETURNS SETOF course_id_class_group_id ARRAY
	AS $$
	BEGIN
		RETURN QUERY EXECUTE test5(ints);
	END;
	$$
	LANGUAGE plpgsql;
	
SELECT test6(ARRAY [1, 2, 3]);

================================================================================================

SET search_path TO isp_class_schedule;


CREATE OR REPLACE FUNCTION get_class_groups_of_course(course_id INT) RETURNS SETOF course_id_class_group_id
	AS $$
		SELECT DISTINCT course_id, class_group_id
		FROM lessons
		WHERE course_id = $1;
	$$
	LANGUAGE SQL;

CREATE OR REPLACE FUNCTION construct_generate_course_class_group_combinations_query(selected_course_ids INT ARRAY) RETURNS TEXT
	AS $$
	DECLARE
		number_of_courses	INT	 		DEFAULT array_length(selected_course_ids, 1);
		select_array 		TEXT ARRAY 	DEFAULT ARRAY[]::TEXT[];
		from_array 			TEXT ARRAY 	DEFAULT ARRAY[]::TEXT[];
	BEGIN
		FOR i IN 1..number_of_courses LOOP
			select_array 	:= select_array || FORMAT('(s%1$s.course_id, s%1$s.class_group_id)', i);
			from_array		:= from_array 	|| FORMAT('get_class_groups_of_course(%s) s%s', selected_course_ids[i], i);
		END LOOP;
		
		RETURN FORMAT('SELECT CAST (ARRAY [%s] AS course_id_class_group_id ARRAY) AS combination FROM %s',
					  array_to_string(select_array, ','), 
					  array_to_string(from_array, ' CROSS JOIN '))
					  || ' ORDER BY 1';
	END;
	$$
	LANGUAGE plpgsql; -- IMMUTABLE

DROP FUNCTION IF EXISTS generate_course_class_group_combinations;
CREATE OR REPLACE FUNCTION generate_course_class_group_combinations(course_ids INT ARRAY) RETURNS SETOF course_id_class_group_id ARRAY
	AS $$
	BEGIN
		RETURN QUERY EXECUTE construct_generate_course_class_group_combinations_query(course_ids);
	END;
	$$
	LANGUAGE plpgsql;

-- DROP FUNCTION IF EXISTS get_first_10;
-- CREATE OR REPLACE FUNCTION get_first_10() RETURNS  SETOF course_id_class_group_id ARRAY
-- 	AS $$
		SELECT 'bla', course_id_class_group_id_array
		FROM generate_course_class_group_combinations(ARRAY [1, 2, 3]) AS combinations(course_id_class_group_id_array)
		ORDER BY 1
		FETCH FIRST 10 ROWS ONLY;
-- 	$$
-- 	LANGUAGE SQL;
	
--SELECT get_first_10();

================================================================================================

CREATE OR REPLACE FUNCTION count_collisions(combination course_id_class_group_id ARRAY) RETURNS INT
	AS $$
	DECLARE
		collision_count INT;
	BEGIN
		-- ON COMMIT DROP can be removed if we work with pooling connections
		CREATE TEMPORARY TABLE IF NOT EXISTS selected_lessons ON COMMIT DROP AS
			SELECT id, start_timestamp, end_timestamp
			FROM unnest(combination) INNER JOIN lessons USING (course_id, class_group_id);

		SELECT count(*) / 2 INTO collision_count
		FROM selected_lessons s1 INNER JOIN selected_lessons s2 ON s1.id <> s2.id
		WHERE (s1.start_timestamp, s1.end_timestamp) OVERLAPS
			  (s2.start_timestamp, s2.end_timestamp);
			  
		RETURN collision_count;
	END;
	$$ LANGUAGE plpgsql;

================================================================================================

CREATE OR REPLACE FUNCTION count_collisions(combination course_id_class_group_id ARRAY) RETURNS INT
	AS $$
	WITH selected_lessons AS (
		SELECT id, start_timestamp, end_timestamp
		FROM unnest(combination) INNER JOIN lessons USING (course_id, class_group_id)
	)
	SELECT count(*) / 2 AS collision_count
	FROM selected_lessons s1 INNER JOIN selected_lessons s2 ON s1.id <> s2.id
	WHERE (s1.start_timestamp, s1.end_timestamp) OVERLAPS
		  (s2.start_timestamp, s2.end_timestamp);
	$$ 
	LANGUAGE SQL;
	
--SELECT count_collisions(CAST( ARRAY [(2,1), (1,2), (5,4)] AS course_id_class_group_id[] ))

CREATE OR REPLACE FUNCTION get_class_groups_of_course(course_id INT) RETURNS SETOF course_id_class_group_id
	AS $$
		SELECT DISTINCT course_id, class_group_id
		FROM lessons
		WHERE course_id = $1;
	$$
	LANGUAGE SQL;

CREATE OR REPLACE FUNCTION construct_generate_course_class_group_combinations_query(selected_course_ids INT ARRAY) RETURNS TEXT
	AS $$
	DECLARE
		number_of_courses	INT	 		DEFAULT array_length(selected_course_ids, 1);
		select_array 		TEXT ARRAY 	DEFAULT ARRAY[]::TEXT[];
		from_array 			TEXT ARRAY 	DEFAULT ARRAY[]::TEXT[];
	BEGIN
		FOR i IN 1..number_of_courses LOOP
			select_array 	:= select_array || FORMAT('(s%1$s.course_id, s%1$s.class_group_id)', i);
			from_array		:= from_array 	|| FORMAT('get_class_groups_of_course(%s) s%s', selected_course_ids[i], i);
		END LOOP;
		
		RETURN FORMAT('SELECT CAST (ARRAY [%s] AS course_id_class_group_id ARRAY) AS combination FROM %s',
					  array_to_string(select_array, ','), 
					  array_to_string(from_array, ' CROSS JOIN '))
					  || ' ORDER BY 1';
	END;
	$$
	LANGUAGE plpgsql; -- IMMUTABLE

DROP FUNCTION IF EXISTS generate_course_class_group_combinations;
CREATE OR REPLACE FUNCTION generate_course_class_group_combinations(course_ids INT ARRAY) RETURNS SETOF course_id_class_group_id ARRAY
	AS $$
	BEGIN
		RETURN QUERY EXECUTE construct_generate_course_class_group_combinations_query(course_ids);
	END;
	$$
	LANGUAGE plpgsql;

-- DROP FUNCTION IF EXISTS get_first_10;
-- CREATE OR REPLACE FUNCTION get_first_10() RETURNS  SETOF course_id_class_group_id ARRAY
-- 	AS $$
		SELECT count_collisions(course_id_class_group_id_array) AS cnt, course_id_class_group_id_array AS crs
		FROM generate_course_class_group_combinations(ARRAY [1, 2, 3, 4, 5]) AS combinations(course_id_class_group_id_array)
		ORDER BY 1
		--FETCH FIRST 10 ROWS ONLY;
-- 	$$
-- 	LANGUAGE SQL;

================================================================================================

CREATE TYPE course_id_class_group_id AS (course_id INT, class_group_id INT);

CREATE OR REPLACE FUNCTION count_collisions(combination course_id_class_group_id ARRAY) RETURNS INT
	AS $$
	WITH selected_lessons AS (
		SELECT id, start_timestamp, end_timestamp
		FROM unnest(combination) INNER JOIN lessons USING (course_id, class_group_id)
	)
	SELECT count(*) / 2 AS collision_count
	FROM selected_lessons s1 INNER JOIN selected_lessons s2 ON s1.id <> s2.id
	WHERE (s1.start_timestamp, s1.end_timestamp) OVERLAPS
		(s2.start_timestamp, s2.end_timestamp);
	$$ 
	LANGUAGE SQL;
	
--SELECT count_collisions(CAST( ARRAY [(2,1), (1,2), (5,4)] AS course_id_class_group_id[] ))

CREATE OR REPLACE FUNCTION get_class_groups_of_course(course_id INT) RETURNS SETOF course_id_class_group_id
	AS $$
		SELECT DISTINCT course_id, class_group_id
		FROM lessons
		WHERE course_id = $1;
	$$
	LANGUAGE SQL;

CREATE OR REPLACE FUNCTION construct_generate_course_class_group_combinations_query(selected_course_ids INT ARRAY) RETURNS TEXT
	AS $$
	DECLARE
		number_of_courses	INT	 	DEFAULT array_length(selected_course_ids, 1);
		select_array 		TEXT ARRAY 	DEFAULT ARRAY[]::TEXT[];
		from_array 		TEXT ARRAY 	DEFAULT ARRAY[]::TEXT[];
	BEGIN
		FOR i IN 1..number_of_courses LOOP
			select_array 	:= select_array || FORMAT('(s%1$s.course_id, s%1$s.class_group_id)', i);
			from_array	:= from_array 	|| FORMAT('get_class_groups_of_course(%s) s%s', selected_course_ids[i], i);
		END LOOP;
		
		RETURN FORMAT('SELECT CAST (ARRAY [%s] AS course_id_class_group_id ARRAY) AS combination FROM %s',
					  array_to_string(select_array, ','), 
					  array_to_string(from_array, ' CROSS JOIN '));
	END;
	$$ LANGUAGE plpgsql; -- IMMUTABLE

CREATE OR REPLACE FUNCTION generate_course_class_group_combinations(course_ids INT ARRAY) RETURNS TABLE(combination course_id_class_group_id ARRAY)
	AS $$
	BEGIN
		RETURN QUERY EXECUTE construct_generate_course_class_group_combinations_query(course_ids);
	END;
	$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_combinations_with_collision_count(row_limit INT, course_ids INT ARRAY)
	RETURNS TABLE(collision_count INT, combination course_id_class_group_id ARRAY)
	AS $$
		SELECT count_collisions(combination), combination
		FROM generate_course_class_group_combinations(course_ids) AS combinations
		ORDER BY 1, 2
		FETCH FIRST row_limit ROWS ONLY;
	$$
	LANGUAGE SQL;
	
--SELECT get_combinations_with_collision_count(100, ARRAY [1, 2, 3, 4, 5]);

CREATE OR REPLACE FUNCTION get_combinations_with_collision_count_json(row_limit INT, course_ids INT ARRAY) RETURNS TEXT
	AS $$
		SELECT array_to_json(array_agg(row_to_json(c)))
		FROM get_combinations_with_collision_count(row_limit, course_ids) AS c
	$$
	LANGUAGE SQL;

SELECT get_combinations_with_collision_count_json(100, ARRAY [1, 2, 3, 4, 5]);