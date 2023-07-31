# ISP Class Schedule

This project is for combi students who are struggling to put together a good Individual Study Program (ISP) or class schedule.

## Setup

1. Load in the Spring Boot project isp_class_schedule_backend.

2. Modify the application.yml file to match your configuration.

3. Run the Spring Boot project. This will:
   
   * create the necessary entities
   * fill these entities with the data in load.sql
   * start the webserver

4. You should now be able to send GET requests to:
   `localhost:8080/api/class-schedule/courses`

> Note: 
> All SQL instructions are written in ISO SQL whenever possible. This is to ensure easy portability to other SQL dialects with minimal changes.

## License

   This project is licensed under the [GNU General Public License version 2](https://opensource.org/licenses/gpl-2.0.php)
