# Wallet Service API

## Overview
Wallet Service API provides functionality for user account management and transaction handling, including balance updates and validation to prevent duplicate transactions. The API ensures that balance cannot be negative and supports integration with a database for persistence.

## Features
1. **User Management**
    - Create user accounts.
    - Retrieve user details by ID.
    - Edit user details.
    - Delete user accounts.

2. **Transaction Management**
    - Top-up user balance.
    - Process refunds.
    - Handle bill payments.

3. **Business Rules**
    - Prevent double deduction or addition of balance for duplicate transaction calls.
    - Ensure balance cannot be negative.

## Technologies Used
- **Java**: Backend language.
- **Spring Boot**: Framework for building RESTful services.
- **Jakarta Validation**: Input validation.
- **Lombok**: Simplifies boilerplate code.
- **Database**: For persistence (e.g., MySQL, PostgreSQL).

## Entity Relational Diagram
![Entity Relational Diagram](https://dwidi.com/wp-content/uploads/2024/12/wallet-service-ERD.png)

## API Endpoints

### User Controller
Base URL: `/api/v1/user`

| Method | Endpoint          | Description              |
|--------|-------------------|--------------------------|
| POST   | `/create`         | Create a new user.       |
| GET    | `/{userId}`       | Retrieve user by ID.     |
| PUT    | `/edit/{userId}`  | Edit user details.       |
| DELETE | `/delete/{userId}`| Delete user account.     |

### Transaction Controller
Base URL: `/api/v1/transaction`

| Method | Endpoint               | Description                   |
|--------|------------------------|-------------------------------|
| POST   | `/topup/{userId}`      | Top-up user balance.          |
| POST   | `/refund/{userId}`     | Refund user balance.          |
| POST   | `/bill/{userId}`       | Process a bill payment.       |

## Setup Instructions

### Prerequisites
- Java 21 or higher
- Maven
- PostgreSQL

### Steps
1. Clone the repository.
   ```sh
   gh repo clone dwididit/walletservice
   cd walletservice
   ```

2. Configure the database connection in `application.properties` or `application.yml`.
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/wallet_service
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   spring.datasource.driver-class-name=org.postgresql.Driver
   ```

3. Build and run the application.
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

## Validation Rules
- Ensure all request payloads are validated using Jakarta Validation.
- Duplicate transactions are prevented by checking transaction identifiers.
- Database constraints ensure balance integrity.

## Logging
- All incoming requests are logged for monitoring and debugging.
- Important operations are logged with appropriate log levels (INFO, ERROR).

