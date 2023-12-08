# Class Scheduler Backend

This project is for students who are taking courses from multiple years or curricula and are struggling to put together a good class schedule.

## Tutorial

1. [Install Docker Desktop.](https://docs.docker.com/get-docker/)

2. Start Docker Desktop.

3. Create a file called `secret-load.sql` at the top level of the project, with the following contents:

   ```sql
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
   ```

   This is the data that the database will be initialized with. It is also the **only** data that the application will have access to, so choose wisely! This simple dataset will do for now though.

4. Save `secret-load.sql` if you haven't already.

5. Run the command

   ```bash
   docker compose up
   ```

   in your terminal. This will launch all the necessary Docker Containers and print their logs in the terminal.

6. Wait until you see the line

   ```
   Started ClassSchedulerBackendApplication in 1.792 seconds (process running for 1.932)
   ```

   printed in the terminal. You should now be able to send GET requests to:

   <http://localhost:8080/api/class-schedule/courses>

   and receive a response similar to the following:

   ```json
   [
     {
       "id": 1,
       "name": "Design"
     },
     {
       "id": 51,
       "name": "Computer Systems"
     },
     {
       "id": 101,
       "name": "Web Development"
     },
     {
       "id": 151,
       "name": "Spaceship Technologies"
     },
     {
       "id": 201,
       "name": "Mobile Applications"
     }
   ]
   ```

If you completed these steps, then your backend is running smoothly.

## REST API

### GET /api/class-schedule/proposals/`:courseIds`?count=`number`

Get proposals for the given courses.
Example usage:
<http://localhost:8080/api/class-schedule/proposals/1,51?count=2>

#### Path Variables

- `courseIds`: a comma-separated list of course IDs in the format `courseId1,courseId2,courseId3`. These are the course IDs of the courses that you want to take.

#### Query Parameters (optional)

- `count`: the number of proposals that should be generated. Defaults to 10.

#### Response

##### 200 OK

Returns `count` number of proposals for the given courses.

```json
[
  {
    "averageWeeklyOverlapCount": "0",
    "combination": [
      {
        "id": 351,
        "course": {
          "id": 1,
          "name": "Design"
        },
        "group": {
          "id": 101,
          "name": "Y2G1"
        }
      },
      {
        "id": 51,
        "course": {
          "id": 51,
          "name": "Computer Systems"
        },
        "group": {
          "id": 1,
          "name": "Y1G1"
        }
      }
    ]
  },
  {
    "averageWeeklyOverlapCount": "0",
    "combination": [
      {
        "id": 351,
        "course": {
          "id": 1,
          "name": "Design"
        },
        "group": {
          "id": 101,
          "name": "Y2G1"
        }
      },
      {
        "id": 451,
        "course": {
          "id": 51,
          "name": "Computer Systems"
        },
        "group": {
          "id": 151,
          "name": "Y1G3"
        }
      }
    ]
  }
]
```

##### 401 Unauthorized

If no course IDs are given.

##### 404 Not Found

If nonexistent course IDs are given.

```json
{
  "courseIds": "Course IDs don't exist"
}
```

## Explanation

### Nullability

The `null` value is never used in our code. This is because Java is incapable of handling this edge case at compile time, thus resulting in NullPointerExceptions at runtime that are very hard to debug. As alternatives to `null`, Optional and empty Collections are used. The only exceptions are null checks like `if (o == null) ...` and tests. These tests will check our code's robustness against attacks from `null` values.

### The Independent Domain

Our domain is fully framework independent. In practice this means that our domain only consists of pure Java code. Any and all Spring Boot or Hibernate Persistence annotations, classes and interfaces only exist outside of the domain. The only dependencies that the domain has are libraries like Hibernate Validator, which are entirely separate from their framework.

### Which SQL Dialect Are You Using?

ISO SQL is used whenever possible. This is to ensure easy portability to other SQL dialects with minimal changes. Any deviations from this rule are indicated by a comment.

### Relational Diagram

Below are the database tables and relations represented in a relational model. These are the meanings:

- A trident means `many`.
- An empty circle means `zero`.
- A perpendicular line means `one`.

![Relational diagram of class schedule](./docs/Relational%20Diagram.png)

## License

This project is licensed under the [GNU General Public License version 2](https://opensource.org/licenses/gpl-2.0.php)
