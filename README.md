# ISP-class-schedule

This project is for combi students who are struggling to put together a good Individual Study Program (ISP) or class schedule.

## Setup

1. Create a database and run the create.sql file on it. This will initialise the necessary schema, tables and functions.
   
   > Note: 
   > All SQL instructions are written in ISO SQL whenever possible. This is to ensure easy portability to other SQL dialects with minimal changes. One of the exceptions is dynamic SQL. Here PostgreSQL's PL/pgSQL is used.

2. Load in the Spring Boot project isp_class_schedule_backend.

3. Modify the application.properties file to match your configuration.

4. Run the Spring Boot project. 

5. You should now be able to send GET requests to:
   
   > `localhost:8080/api/schedule/class_schedule`
   
   with in the body of the request a JSON array with the course ID's of the courses that you would like to take, e.g.
   
   > [7, 1, 5, 42, 6]

## License

   Everything in this project is licensed under the [GNU General Public License version 2](https://opensource.org/licenses/gpl-2.0.php)
