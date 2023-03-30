# Wallet Service💰

This is a Java 17 Spring Boot 3 project with a hexagonal architecture that includes features such as
records, streams API, Specifications, Paginable, JpaRepository, ControllerAdvice,
RestControllers, and enums among others.

## Technologies Used

- Docker
- Java 17
- Spring Boot 3
- PostgreSQL
- Mockito 5
- JUnit 5

## Architecture

This project follows a hexagonal architecture pattern, with ports and adapters.

## Design Patterns

## Running the Application with Docker Compose

To run the application using Docker Compose, follow these steps:

1. Clone the repository to your local machine.
2. Navigate to the project directory.
3. Run the following command to build the Docker image:
4. Run the following command to start the application and its database ```docker-compose up```
5. The application should now be running on `http://localhost:8001`.

## Dockerized Components

The entire application along with its PostgreSQL database is dockerized.

## Conclusion

With the help of Docker Compose, you can easily run the application and its database without the
need to install any dependencies on your local machine. The hexagonal architecture and design
patterns used in this project provide a clean and modular codebase that is easy to maintain and
extend.
