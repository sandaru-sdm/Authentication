# Authentication Service

This is a **Spring Boot** project that provides user authentication functionality using JWT (JSON Web Tokens). It includes sign-up, login, account activation, and additional user-related APIs with JWT-based security.

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

---

### Email Configuration
For sending emails (e.g., activation and password reset), add the following SMTP settings to `application.properties`:

```properties
# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<YOUR_EMAIL>
spring.mail.password=<YOUR_APP_PASSWORD>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
```

Replace:
- `<YOUR_EMAIL>`: Your email address.
- `<YOUR_APP_PASSWORD>`: The app-specific password for your email.

---

## 2. API Documentation

### 2.1 Sign-Up API

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
  "message": "User registered! Check your email to activate your account."
}
```

---

### 2.2 Activate Account API

**Endpoint:**
```
GET http://localhost:8080/api/auth/activate?code=<ACTIVATION_CODE>
```

**Response:**
```json
{
  "message": "Account activated successfully!"
}
```

If the activation code is invalid or expired:
```json
{
  "error": "Invalid or expired activation code."
}
```

---

### 2.3 Authentication (Login) API

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
  "token": "<JWT_TOKEN>"
}
```

---

## 3. How to Run

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/sandaru-sdm/Authentication.git
   cd Authentication
   ```
2. **Configure the `application.properties` file** with your database, JWT secret key, and email settings as described above.
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

### Dependencies:
- **Spring Boot Web**
- **Spring Boot Data JPA**
- **Spring Boot Mail**
- **MySQL Connector**
- **JWT (io.jsonwebtoken)**
- **BCrypt for Password Hashing**

### Error Handling:
- Proper error responses are implemented for:
  - Invalid credentials
  - User already exists
  - Missing or invalid JWT tokens
  - Invalid activation codes

### Example Error Response:
```json
{
  "error": "Invalid credentials",
  "timestamp": "2024-07-12T12:34:56.789Z"
}
```

---

## 5. Security Considerations

- Use a strong **JWT secret key** and do not expose it publicly.
- Use **HTTPS** in production to secure API requests.
- Store passwords securely using **BCrypt** hashing (already implemented).
- Keep the **email username and password** secure. Use environment variables or secret vaults in production.

---

## 6. Tools Used

- **Spring Boot** for application development
- **JWT** for authentication
- **MySQL** for the database
- **Maven** for dependency management
- **Postman** for testing APIs

---

## 7. Future Improvements

### 2.4 Get User Details (Protected)

**Endpoint:**
```
GET http://localhost:8080/api/auth/user/{id}
```

**Headers:**
```http
Authorization: Bearer <JWT_TOKEN>
```

**Response:**
```json
{
  "id": 3,
  "name": "testuser",
  "email": "test@example.com"
}
```

### 2.5 Password Reset API (Email)

**Endpoint:**
```
POST http://localhost:8080/api/auth/password-reset
```

**Request Body:**
```json
{
  "email": "test@example.com"
}
```

**Response:**
```json
{
  "message": "Password reset email sent."
}
```
---

## 8. Contact
For queries, issues, or suggestions, contact me at:
- **Email**: `maduhansadilshan@gmail.com`
- **GitHub**: [sandaru-sdm](https://github.com/sandaru-sdm)

---

**Happy Coding!** ✨
