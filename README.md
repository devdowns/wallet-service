# Wallet Service💰

This is a Java 17 Spring Boot 3 project implemented using hexagonal architecture that includes
features such as
records, streams API, Specifications, Paginable, JpaRepository, ControllerAdvice,
RestControllers, and enums among others. All whilst
using [Google's Java Style Guide](https://google.github.io/styleguide/javaguide.html) you too can
get a format spec for your favourite IDE [here](https://github.com/google/styleguide) 😁

## Technologies Used

- Docker
- Java 17
- Spring Boot 3
- PostgreSQL
- Mockito 5
- JUnit 5

## Architecture

This project follows a hexagonal architecture pattern, with ports and adapters.

## Running the Application with Docker Compose

To run the application using Docker Compose, follow these steps:

1. Clone the repository to your local machine.
2. Navigate to the project directory.
3. Run the following command to start the application and its database ```docker-compose up```
4. The application should now be running on `http://localhost:8001`

## Dockerized Components

The entire application along with its PostgreSQL database is dockerized.

## Conclusion

With the help of Docker Compose, you can easily run the application and its database without the
need to install any dependencies on your local machine. The hexagonal architecture and design
patterns used in this project provide a clean and modular codebase that is easy to maintain and
extend.
