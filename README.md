# School App Spring

School App Spring represents a school management application built with Java Spring Boot following the Service-Oriented Architecture (SOA) model. This application allows users to create accounts and log in as teachers or students. Teachers have the ability to perform CRUD operations related to students. The application is designed to support a role system in the future.

## Features

- **Authentication and Authorization:** Authentication and authorization are planned to be implemented in the future using JWT tokens.

- **Controller Classes:** These classes handle user requests and route them to the appropriate actions.

- **Service Classes:** Service classes manage the business logic of the application.

- **Repositories:** Repositories manage the database interactions.

- **DTOs:** Data Transfer Objects (DTOs) facilitate the exchange of data between different parts of the application.

- **Model:** The application model represents the structure and organization of data, including students, teachers, and roles.

- **Validators:** Validators ensure data input meets the required criteria.

- **Utility Methods:** Utility methods include configurations for date formats and password encryption.

- **Front-End:** The front-end is integrated with the web server, providing pages for teacher CRUD operations and login/register functionality. Styling is planned to be improved using HTML5, CSS, and jQuery.

## Gradle Configuration

The application is built using Gradle. The `build.gradle` file includes dependencies required for the project.

## Dependencies

- **spring-boot-starter-data-jpa:** Starter for using Spring Data JPA for database access.

- **spring-boot-starter-validation:** Starter for adding validation support.

- **spring-boot-starter-web:** Starter for building web applications with Spring MVC.

- **lombok:** Project Lombok for reducing boilerplate code.

- **mysql-connector-j:** Connector for MySQL database.

- **spring-boot-starter-test:** Starter for testing.

- **spring-security-test:** Starter for Spring Security testing.

- **springdoc-openapi-ui:** Starter for generating OpenAPI documentation.

- **jbcrypt:** Library for password hashing.

## Getting Started

1. Clone this repository to your local machine.

2. Build and run the application using Gradle.

3. Access the application in your browser.

## Future Enhancements

- Role-based access control for teachers.

- Course and meeting functionality for students.

- Implementation of authentication and authorization using JWT tokens.

