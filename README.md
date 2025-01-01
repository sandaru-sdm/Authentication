# Authentication Service

This is a **Spring Boot** project that provides user authentication functionality using JWT (JSON Web Tokens). It includes sign-up, login, account activation, password reset, and additional user-related APIs with JWT-based security.

---

## 1. Configuration

### Environment Variables
Create a `.env` file in the root directory with the following configuration:

```properties
# Database Configuration
DATASOURCE_URL=jdbc:mysql://localhost:3307/auth
DATASOURCE_USER=YOUR_DATASOURCE_USER
DATASOURCE_PASSWORD=YOUR_DATASOURCE_PASSWORD

# JWT Configuration
JWT_SECRET_KEY=YOUR_JWT_SECRET_KEY

# Email Configuration
MAIL_USERNAME=YOUR_EMAIL
MAIL_PASSWORD=YOUR_EMAIL_APP_PASSWORD

# URL Configuration
FRONTEND_URL=http://localhost:4200/login
BASE_URL=http://localhost:8080/api/auth/
```

### Database Configuration
The application will use the database configuration from your `.env` file. Make sure your MySQL server is running and accessible with the provided credentials.

### JWT Secret Key
The JWT secret key is now configured through the `.env` file. Make sure to use a strong secret key in production.

### Email Configuration
Email settings are now managed through the `.env` file. The application uses these credentials for sending activation and password reset emails.

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

### 2.4 Forgot Password API

**Endpoint:**
```
POST http://localhost:8080/api/auth/forgot-password
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
  "message": "Password reset link has been sent to your email."
}
```

---

### 2.5 Reset Password API

**Endpoint:**
```
POST http://localhost:8080/api/auth/reset-password?token=<RESET_TOKEN>
```

Example URL:
```
http://localhost:8080/api/auth/reset-password?token=29190c5e-55cf-4f49-93f0-f2d8e81245c7
```

**Request Body:**
```json
{
  "email": "test@example.com",
  "newPassword": "newPassword123",
  "confirmNewPassword": "newPassword123"
}
```

**Success Response:**
```json
{
  "message": "Password has been successfully reset."
}
```

**Error Response:**
```json
{
  "error": "Password and Confirm Password Not Match."
}
```
or
```json
{
  "error": "Invalid or expired token."
}
```

---

## 3. How to Run

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/sandaru-sdm/Authentication.git
   cd Authentication
   ```
2. **Create and configure the `.env` file** as described in the Configuration section.
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
  - Invalid password reset tokens
  - Password mismatch during reset

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
- Password reset tokens expire after 1 hour for security.

---

## 6. Tools Used

- **Spring Boot** for application development
- **JWT** for authentication
- **MySQL** for the database
- **Maven** for dependency management
- **Postman** for testing APIs

---

## 7. Contact
For queries, issues, or suggestions, contact me at:
- **Email**: `maduhansadilshan@gmail.com`
- **GitHub**: [sandaru-sdm](https://github.com/sandaru-sdm)

---

**Happy Coding!** ✨
