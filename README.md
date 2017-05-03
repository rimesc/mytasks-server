# MyTasks Server

Server side of a toy web application I'm writing as a learning exercise.  It's a variation on the traditional 'todo' application, but with some of the features of an issue tracker.

This part uses Spring Boot / Spring Web MVC and Hibernate.

## Developing

Use [this custom check style configuration](https://bitbucket.org/crimes/mytasks/downloads/custom-sun-checks.xml).

## Development server

Run `mvn -D run.profiles=dev spring-boot:run` to run in development mode (in-memory database with some sample data). The REST API is rooted at `http://localhost:8080/api`.

