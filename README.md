# 🏫 School Management System

A comprehensive **School Management System** built with **Spring Boot**, featuring role-based access control, JWT authentication, data encryption, and a full REST API documented with Swagger. Designed to manage the complete lifecycle of a school — from students and teachers to library and fees.

---

## 🚀 Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend Framework | Spring Boot (Java) |
| Security | Spring Security + JWT |
| Database | Azure SQL / MySQL |
| API Documentation | Swagger / OpenAPI |
| Data Encryption | Checksum-based Encryption |
| Build Tool | Maven |

---

## 🔐 Authentication & Authorization

- **JWT-based Authentication** for secure, stateless API access
- **Role-Based Access Control (RBAC)** with Spring Security
- **Data Encryption** with checksum validation on sensitive fields
- **Input Validation** on all incoming request payloads

### Roles

| Role | Access Level |
|------|-------------|
| `ADMIN` | Full system access |
| `TEACHER` | Manage students, subjects, attendance |
| `STUDENT` | View own profile, results, library |
| `PARENT` | View child's progress |
| `LIBRARIAN` | Manage books and loans |

---

## 📌 API Modules

### 🔑 Auth (`/api/auth`)
- Register and Login for all roles
- JWT token generation and validation
- Checksum-based data encryption

### 👤 Admin (`/api/admin`)
- Manage all users (create, update, delete)
- System configuration and reporting

### 👨‍🎓 Student (`/api/student`)
- Student registration and profile management
- View subjects, attendance, and results

### 👨‍🏫 Teacher (`/api/teacher`)
- Teacher profile management
- Assign subjects and manage attendance

### 👨‍👩‍👦 Parent (`/api/parent`)
- View ward's academic progress
- Access attendance and fee records

### 📚 Library (`/api/library`)
- Manage book inventory
- Search and browse available books

### 📖 Book Loan (`/api/bookloan`)
- Issue and return books
- Track loan history per student

### 👨‍💼 Librarian (`/api/librarian`)
- Manage librarian profiles
- Oversee library operations

### 💰 Fees (`/api/fees`)
- Record and track fee payments
- Generate fee summaries per student

### 📖 Subject (`/api/subject`)
- CRUD operations for subjects
- Associate subjects with streams and teachers

### 🎓 Stream (`/api/stream`)
- Manage academic streams (Science, Commerce, Arts, etc.)
- Associate students and subjects with streams

### 👥 User (`/api/user`)
- User profile management
- Password and credential updates

---

## ⚙️ Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL / Azure SQL Database
- Postman or Swagger UI (for testing)

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/Vipankumar-benepik/SchoolManagement.git
cd SchoolManagement

# 2. Configure your database in application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/school_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# 3. Build the project
mvn clean install

# 4. Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

---

## 🗄️ Database Configuration

The project supports both **local MySQL** and **Azure SQL Server**.

```properties
# Local MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/school_db
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Azure SQL Server
spring.datasource.url=jdbc:sqlserver://<server>.database.windows.net:1433;database=<db>
spring.datasource.username=<azure-username>
spring.datasource.password=<azure-password>
```

---

## 📄 API Documentation (Swagger)

Once the application is running, visit:

```
http://localhost:8080/swagger-ui/index.html
```

All REST endpoints are documented with request/response schemas, authentication requirements, and example payloads.

---

## 🔒 Security Features

- Passwords stored using **BCrypt hashing**
- Sensitive data protected with **checksum-based encryption**
- JWT tokens with configurable expiry
- All endpoints protected by **role-based authorization**
- Input validation using **Jakarta Bean Validation**

---

## 🌐 Deployment

The project is configured for **Azure SQL Database** as the cloud database backend. To deploy:

1. Set up an Azure SQL Server instance
2. Update `application.properties` with Azure credentials
3. Build the JAR: `mvn clean package`
4. Deploy the JAR to your preferred hosting (Azure App Service, AWS EC2, etc.)

