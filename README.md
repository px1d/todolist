# todolist

## Features

### Core Functionality

- [x] todo list curd
- [x] todo list item curd
- [x] todo list item query (filter and sorting)
- [ ] todo list item pagination

### User Management

- [x] registration and login
- [x] authorization and authentication
- [ ] user curd
- [ ] user permission

## For Server Developers

### Requirements (for development)

- JDK 8
- Maven
- MySQL 8.0

### Run the project

```bash
mvn clean install
mvn spring-boot:run
```

### Run the test

```bash
mvn test
```

### Modules

- `todolist-common`: the model module, define the data structure and the database schema
- `todolist-auth`: the auth module, handle the user authentication and authorization
- `todolist-server`: the server module, provide the restful api

## For Frontend Developers

### Swagger

```bash
http://localhost:8080/swagger-ui/index.html
```

### JWT

Most of the endpoints are protected by authentication.

The token is sent in the Authorization header with the prefix `Bearer `.

```bash
Authorization: Bearer <token>
```

By logging in, a token will be returned.

You can use the following account to test the api.

```text
user: admin@local.host
pass: admin@SMJV*!KLWF=3
```
