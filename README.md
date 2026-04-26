# Banking API

A production-grade REST API simulating core banking operations, built with Java and Spring Boot. Fully deployed on GCP Cloud Run with CI/CD automation.

## Live Demo
**Base URL:** `https://banking-api-118980396512.us-central1.run.app`

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3.5 |
| Security | Spring Security, JWT Authentication |
| Relational DB | PostgreSQL (Cloud SQL on GCP) |
| NoSQL DB | MongoDB Atlas |
| Containerization | Docker, Docker Compose |
| CI/CD | GitHub Actions |
| Cloud | GCP Cloud Run, GCP Container Registry |

## Features

- JWT based authentication — register, login, protected endpoints
- Account management — create accounts, check balances
- Transactions — deposits, withdrawals, transfers between accounts
- Audit logging — every action logged to MongoDB with timestamp and user
- Global error handling — clean JSON error responses
- Multi-stage Docker builds for optimized image size
- Automated CI/CD pipeline — builds and packages on every push to main

## Architecture

\```
Client (Postman/Frontend)
        ↓
GCP Cloud Run (Spring Boot App)
        ↓              ↓
Cloud SQL          MongoDB Atlas
(PostgreSQL)       (Audit Logs)
Accounts +
Transactions
\```

## API Endpoints

### Authentication
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | /api/auth/register | Register new user | No |
| POST | /api/auth/login | Login and get JWT token | No |

### Accounts
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | /api/accounts | Create account | Yes |
| GET | /api/accounts | Get all accounts | Yes |
| GET | /api/accounts/{id} | Get account by ID | Yes |
| GET | /api/accounts/{id}/balance | Check balance | Yes |

### Transactions
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | /api/transactions/deposit | Deposit money | Yes |
| POST | /api/transactions/withdraw | Withdraw money | Yes |
| POST | /api/transactions/transfer | Transfer between accounts | Yes |
| GET | /api/transactions/history/{accountId} | Transaction history | Yes |

## Getting Started Locally

### Prerequisites
- Java 21
- Maven
- Docker Desktop

### Run locally

\```bash
# Clone the repo
git clone https://github.com/kaushipsheridan/banking-api.git
cd banking-api/banking-api

# Start databases
docker compose up -d postgres mongodb

# Run the app
mvn spring-boot:run
\```

### Environment Variables

| Variable | Description |
|---|---|
| SPRING_DATASOURCE_URL | PostgreSQL connection URL |
| SPRING_DATASOURCE_USERNAME | PostgreSQL username |
| SPRING_DATASOURCE_PASSWORD | PostgreSQL password |
| SPRING_DATA_MONGODB_URI | MongoDB connection string |
| JWT_SECRET | JWT signing secret |
| JWT_EXPIRATION | Token expiration in milliseconds |

## Example Requests

**Register:**
\```bash
curl -X POST https://banking-api-118980396512.us-central1.run.app/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "john", "password": "password123"}'
\```

**Create Account:**
\```bash
curl -X POST https://banking-api-118980396512.us-central1.run.app/api/accounts \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"ownerName": "John Doe", "initialBalance": 1000.00, "accountType": "SAVINGS"}'
\```

**Deposit:**
\```bash
curl -X POST https://banking-api-118980396512.us-central1.run.app/api/transactions/deposit \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"accountNumber": "YOUR_ACCOUNT_NUMBER", "amount": 500.00}'
\```

## CI/CD Pipeline

Every push to `main` automatically:
1. Sets up Java 21
2. Builds the JAR with Maven
3. Builds the Docker image

Deployment to GCP Cloud Run is done via `gcloud` CLI.

## Database Design

**PostgreSQL — Relational Data**
- `users` — authentication credentials
- `accounts` — account details and balances
- `transactions` — full transaction history

**MongoDB — Audit Logs**
- `audit_logs` — every API action with timestamp, user, and result

## Security

- Passwords hashed with BCrypt
- Stateless JWT authentication
- All endpoints protected except `/api/auth/**`
- Environment variables for all sensitive configuration

## Author
Priyanshu Kaushik — [LinkedIn]([https://linkedin.com/in/YOUR_LINKEDIN](https://www.linkedin.com/in/priyanshu-kaushik/))
