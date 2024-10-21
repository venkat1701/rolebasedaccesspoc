# Spring Boot Authentication, Authorization, and Role-Based Access Control

This project demonstrates **authentication**, **authorization**, and **role-based access control** using `@PreAuthorize` in a Spring Boot application. The system allows users to register, log in, and access different resources based on their assigned roles.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Endpoints](#endpoints)
- [Role-Based Access Control](#role-based-access-control)
- [PreAuthorize Annotations](#preauthorize-annotations)

## Features

- **User Registration**: Allows users to register with username, password, and roles.
- **JWT Authentication**: Users receive a JSON Web Token (JWT) upon login for secure authentication.
- **Role-Based Authorization**: Access to certain endpoints is restricted based on user roles.
- **PreAuthorize Annotations**: The `@PreAuthorize` annotation is used for controlling access at the method level.
- **Password Encryption**: User passwords are stored securely using BCrypt encryption.

## Technologies Used

- Java 17+
- Spring Boot 3.x
- Spring Security
- JWT (JSON Web Tokens)
- JPA/Hibernate
- PostgreSQL (or any relational database)
- Maven

## Getting Started

### Prerequisites

- Java 17 or higher installed
- PostgreSQL installed and running (or another relational database of your choice)
- Maven installed

### Setup

1. Clone the repository:
    ```bash
    git clone https://github.com/venkat1701/rolebasedaccesspoc
    cd auth-authorization-preauthorize
    ```

2. Configure the database in the `application.yml` or `application.properties` file:
    ```properties
         # Application Properties
      spring.application.name=rolebasedaccesscontrolpoc
      
      # PostgreSQL Datasource
      spring.datasource.url=jdbc:postgresql://localhost:5432/rolestest
      spring.datasource.username=postgres
      spring.datasource.password=password
      spring.datasource.driver-class-name=org.postgresql.Driver
      
      # JPA and Hibernate settings
      spring.jpa.show-sql=true
      spring.jpa.hibernate.ddl-auto=update
      
      # Disable Hibernate SQL logging (optional)
      logging.level.org.hibernate.SQL=OFF
      
      # Management settings
      management.endpoint.health.show-details=always
      management.endpoint.health.web.exposure.include=*
    ```

3. Build the project:
    ```bash
    mvn clean install
    ```

4. Run the application:
    ```bash
    mvn spring-boot:run
    ```


## Endpoints

- `POST /auth/signup`  
  Registers a new user.
  
- `POST /auth/signin`  
  Authenticates a user and returns a JWT token.

- `POST /admins`  
  Registers a new administrator. (**Requires Authentication**)

- `GET /users`  
  Access restricted to users with **ADMIN** and **SUPER_ADMIN** role.

- `GET /users/me`
  Returns the user authenticated.

## Role-Based Access Control

Roles are used to determine what resources a user can access. In this project, there are two primary roles:

1. `ROLE_USER` - Standard user with limited access to resources.
2. `ROLE_ADMIN` - Administrator with access to sensitive resources.
3. `ROLE_SUPER_ADMIN` - Super Admin with God level access.

The `@PreAuthorize` annotation is used to secure methods based on roles.

## PreAuthorize Annotations

Here are some examples of how `@PreAuthorize` is used in the project:

```java
 @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> authenticatedUser() {
        return null;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return null;
    }
```
