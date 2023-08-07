# ISP Class Schedule

This project is for students who are taking courses from multiple years or curricula and are struggling to put together a good Individual Study Program (ISP) or class schedule.

## Setup

1. Load in the Spring Boot project isp_class_schedule_backend.

2. Create the following environment variables, they are needed for the database connection:
   
   ```properties
   DB_HOST=jdbc:postgresql://localhost
   DB_PORT=5432
   DB_NAME=postgres
   DB_DEFAULT_SCHEMA=example_schedule
   DB_USERNAME=example_username
   DB_PASSWORD=example_password
   ```

3. Run the Spring Boot project. This will:
   
   * start the webserver
   
   * create the necessary entities
   
   * fill these entities with the data in load.sql

4. You should now be able to send GET requests to:
   `localhost:8080/api/class-schedule/courses`

## FAQ

* Which SQL dialect are you using?
  
  > ISO SQL is used whenever possible. This is to ensure easy portability to other SQL dialects with minimal changes. Any deviations from this rule are indicated by a comment.

## License

   This project is licensed under the [GNU General Public License version 2](https://opensource.org/licenses/gpl-2.0.php)
