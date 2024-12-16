# Authentication Service

This is a **Spring Boot** project that provides user authentication functionality using JWT (JSON Web Tokens). It includes sign-up and login APIs with JWT-based security.

---

## 1. Configuration

### Database Configuration
Update the following fields in the `src/main/resources/application.properties` file with your database credentials:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/<DATABASE_NAME>
spring.datasource.username=<DATABASE_USERNAME>
spring.datasource.password=<DATABASE_PASSWORD>

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
```

Replace:
- `<DATABASE_NAME>`: Name of your MySQL database.
- `<DATABASE_USERNAME>`: Your MySQL database username.
- `<DATABASE_PASSWORD>`: Your MySQL database password.

---

### JWT Secret Key
Set the JWT secret key in the `application.properties` file:

```properties
# JWT Secret Key
jwt.secret=<YOUR_SECRET_KEY>
```

- The **JWT Secret Key** should be a random string of at least 32 characters (e.g., a base64-encoded string).
- Example of a strong secret key: `jSHd7238Kj21$@Ndjs82Gjs28!@jLkSd`.

To generate a random secure key, you can use [online tools](https://randomkeygen.com/) or libraries.

---

## 2. API Documentation

### Sign-Up API

**Endpoint:**
```
POST http://localhost:8080/api/auth/sign-up
```

**Request Body:**
```json
{
  "name": "testuser",
  "email": "test@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "message": "User registered successfully"
}
```

### Authentication (Login) API

**Endpoint:**
```
POST http://localhost:8080/api/auth/authenticate
```

**Request Body:**
```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "tokenType": "Bearer",
  "userId": 3,
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkaWxzaGFuQGdtYWlsLmNvbSIsImlhdCI6MTczNDM0OTA5OSwiZXhwIjoxNzM0NDM1NDk5fQ.L1YsxXlGuMjUH2YyWGitPk0Yitmii-Ku4bmdaalA7kE"
}
```

- Replace `token` with the returned JWT string.
- This token must be included in the `Authorization` header for accessing protected endpoints.

Example Header for Protected Requests:
```http
Authorization: Bearer <JWT_TOKEN>
```

---

## 3. How to Run

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/sandaru-sdm/Authentication.git
   cd Authentication
   ```
2. **Configure the `application.properties` file** with your database and JWT secret key as described above.
3. **Build the Project** using Maven:
   ```bash
   mvn clean install
   ```
4. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```
5. Access the application on:
   ```
   http://localhost:8080/api/auth/sign-up
   ```

---

## 4. Additional Notes

- **Dependencies:**
  - Spring Boot Web
  - Spring Boot Data JPA
  - MySQL Connector
  - JWT (io.jsonwebtoken)
- **Error Handling:**
  - Proper error responses are implemented for invalid credentials or user already exists.

---

## 5. Security Considerations

- Use a strong **JWT secret key** and do not expose it publicly.
- Use HTTPS in production to secure requests.
- Store passwords securely using hashing algorithms like BCrypt (already implemented).

---

## 6. Tools Used

- **Spring Boot**
- **JWT** for authentication
- **Maven** for dependency management
- **MySQL** for database

---

**Happy Coding!** ✨
