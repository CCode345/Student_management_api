# Student Management System API

A **Spring Boot REST API** for managing students, courses, enrollments, and student validation with **JWT-based Authentication and Authorization**.

---

## Tech Stack

* Java 17+
* Spring Boot 3.2.5
* Spring Security 6
* JWT Authentication
* Spring Data JPA + Hibernate
* H2 In-Memory Database (Development)
* MySQL (Production Ready)
* Springdoc OpenAPI (Swagger UI)
* Lombok
* JUnit 5 + Mockito
* Maven

---

## Project Structure

```text
src/main/java/com/studentmgmt/
├── controller/        
├── service/          
├── serviceimpl/     
├── repository/        
├── model/            
├── dto/       
├── exception/        
├── security/         
├── config/       
└── StudentManagementApplication.java
```

---

## Features

* JWT Authentication & Authorization
* Role-Based Access Control (ADMIN)
* Student Management
* Course Management
* Student Validation API
* Course Assignment & Enrollment
* Global Exception Handling
* Swagger API Documentation
* Unit Testing using JUnit and Mockito

---

## Setup & Run

### Prerequisites

* Java 17+
* Maven 3.6+

### Run Application

```bash
mvn spring-boot:run
```

Application runs on:

```text
http://localhost:8080
```

---

## Authentication

The application uses **JWT Authentication**.

### Login

```http
POST /auth/login
```

Request Body:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Response:

```json
{
  "token": "can be generated on runtime"
}
```

Use the token in subsequent requests:

```text
Authorization: Bearer <JWT_TOKEN>
```

---

## API Endpoints

### Authentication

| Method | Endpoint      | Description        |
| ------ | ------------- | ------------------ |
| POST   | `/auth/login` | Generate JWT Token |

---

### Student APIs

| Method | Endpoint                                   | Description                   |
| ------ | ------------------------------------------ | ----------------------------- |
| POST   | `/api/students`                            | Add Student                   |
| GET    | `/api/students`                            | Get All Students              |
| GET    | `/api/students/{id}`                       | Get Student By Id             |
| GET    | `/api/students/code/{studentCode}`         | Get Student By Code           |
| PUT    | `/api/students/{id}/profile`               | Update Student Profile        |
| DELETE | `/api/students/{id}`                       | Delete Student                |
| GET    | `/api/students/search?name=`               | Search Student By Name        |
| GET    | `/api/students/by-course/{courseId}`       | Get Students By Course Id     |
| GET    | `/api/students/by-course-name?courseName=` | Get Students By Course Name   |
| POST   | `/api/students/validate`                   | Validate Student (Public API) |

---

### Course APIs

| Method | Endpoint                                     | Description                |
| ------ | -------------------------------------------- | -------------------------- |
| POST   | `/api/courses`                               | Create Course              |
| PUT    | `/api/courses/{id}`                          | Update Course              |
| DELETE | `/api/courses/{id}`                          | Delete Course              |
| GET    | `/api/courses`                               | Get All Courses            |
| GET    | `/api/courses/{id}`                          | Get Course By Id           |
| GET    | `/api/courses/search?name=`                  | Search Course By Name      |
| GET    | `/api/courses/search/topic?topic=`           | Search Course By Topic     |
| GET    | `/api/courses/type/{courseType}`             | Get Courses By Type        |
| POST   | `/api/courses/{courseId}/assign/{studentId}` | Assign Course To Student   |
| DELETE | `/api/courses/{courseId}/leave/{studentId}`  | Remove Student From Course |
| GET    | `/api/courses/student/{studentId}`           | Get Courses Of Student     |

---

## Swagger UI

Access Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

### Authorize Swagger

1. Call `/auth/login`
2. Copy JWT token
3. Click **Authorize**
4. Enter only the token value
5. Execute secured APIs

---

## H2 Database Console

```text
http://localhost:8080/h2-console
```

Configuration:

```text
JDBC URL : jdbc:h2:mem:studentdb
Username : sa
Password : no password just leave it blank.
```



## Sample Authenticated Request

```bash
curl -X GET http://localhost:8080/api/students \
-H "Authorization: Bearer <JWT_TOKEN>"
```

---

## Running Unit Tests

```bash
mvn test
```

## Security Implementation

* Spring Security 6
* JWT Token Generation
* JWT Validation Filter
* Stateless Session Management
* Password Encryption using BCrypt
* Role-Based Authorization
* Protected REST APIs

---

## Author

**Chandan Kumar Ram**
Java Backend Developer
