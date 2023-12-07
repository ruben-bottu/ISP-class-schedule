# Class Scheduler Backend

This project is for students who are taking courses from multiple years or curricula and are struggling to put together a good Individual Study Program (ISP) or class schedule.

## Tutorial

1. Make sure that you have [Docker](https://docs.docker.com/get-docker/) installed on your system. If you are on Windows or MacOS, Docker Desktop should be running.

2. Create a file called `secret-load.sql` at the top level of the project, with the following contents:

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

   As the name "init_data" indicates, this is the data that the database will be initialized with. This is also the only data that the application will have access to, so choose wisely! This simple dataset will do for now though.

3. Run the command

   ```bash
   docker compose up
   ```

   in your terminal. This will launch all the necessary Docker Containers and print their logs in the terminal.

4. Wait until you see the line

   ```
   Started ClassSchedulerBackendApplication in X seconds (process running for X)
   ```

   printed in the terminal. You should now be able to send GET requests to:

   <http://localhost:8080/api/class-schedule/courses>

   and receive a response similar to the following:

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

If you completed these steps, then your backend is running smoothly.

## REST API

### GET /api/class-schedule/proposals/:courseIds?count=1

Get all proposals for the given courses.
Example usage:
<http://localhost:8080/api/class-schedule/proposals/1,51>

#### Response

##### 200 OK

Returns `count` number of proposals. Defaults to 10 proposals if no `count` is specified.

```javascript
[
  {
    averageWeeklyOverlapCount: "0",
    combination: [
      {
        id: 51,
        course: {
          id: 1,
          name: "Testing",
        },
        group: {
          id: 1,
          name: "ME-2TI/5",
        },
      },
      {
        id: 101,
        course: {
          id: 101,
          name: "Basis van objectgericht programmeren",
        },
        group: {
          id: 51,
          name: "ME-1TI/2",
        },
      },
      {
        id: 501,
        course: {
          id: 51,
          name: "Algoritmisch denken",
        },
        group: {
          id: 101,
          name: "ME-1TI/3",
        },
      },
    ],
  },
];
```

##### 401 Unauthorized

If no course IDs are given.

##### 404 Not Found

If nonexistent course IDs are given.

```javascript
{
  "courseIds": "Course IDs don't exist"
}
```

## Explanation

- Which SQL dialect are you using?

  > ISO SQL is used whenever possible. This is to ensure easy portability to other SQL dialects with minimal changes. Any deviations from this rule are indicated by a comment.

## License

This project is licensed under the [GNU General Public License version 2](https://opensource.org/licenses/gpl-2.0.php)
