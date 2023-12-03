# Class Scheduler

This project is for students who are taking courses from multiple years or curricula and are struggling to put together a good Individual Study Program (ISP) or class schedule.

## Tutorial

1. Make sure that you have Docker installed on your system. In case of Windows or MacOS, Docker Desktop should be running.

2. Create a file named `secret-load.sql` at the top level of the project, with the following contents:

   ```sql
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
   ```

   As the name "init_data" indicates, this query will insert the data in `secret-load.sql` into the database. This is all the data that the application will have access to, so choose wisely! This simple dataset will do for now though.

3. Run the command `docker compose up -d` in your terminal. This will launch all the necessary docker containers and make the application available at port 8080.

4. You should now be able to send GET requests to: `http://localhost:8080/api/class-schedule/courses` and receive a response similar to the following:
   ```json
   [
     {
       "id": 1,
       "name": "Testing"
     },
     {
       "id": 51,
       "name": "Basis van objectgericht programmeren"
     },
     {
       "id": 101,
       "name": "Databanken 1"
     },
     {
       "id": 151,
       "name": "Webontwikkeling 1"
     },
     {
       "id": 201,
       "name": "Algoritmisch denken"
     }
   ]
   ```

## Explanation

- Which SQL dialect are you using?

  > ISO SQL is used whenever possible. This is to ensure easy portability to other SQL dialects with minimal changes. Any deviations from this rule are indicated by a comment.

## License

This project is licensed under the [GNU General Public License version 2](https://opensource.org/licenses/gpl-2.0.php)
