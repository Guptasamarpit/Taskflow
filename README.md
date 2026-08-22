# TaskFlow — Spring Boot + React Full-Stack Project

A production-style learning project for a React Native developer moving into Java/Spring Boot full-stack development.

## Stack
- Java 21
- Spring Boot 3.5.x
- Spring Web, Validation, Data JPA, Security
- PostgreSQL
- JWT authentication
- React 19 + Vite
- React Router
- Axios

## Architecture
Browser -> React SPA -> REST API -> Spring Security/JWT -> Service -> JPA Repository -> PostgreSQL

The backend follows Controller -> Service -> Repository boundaries, with DTOs at the API boundary and entities kept internal to persistence.

## Run
### 1. Database
```bash
docker compose up -d postgres
```

### 2. Backend
```bash
cd backend
./mvnw spring-boot:run
```
Windows:
```powershell
mvnw.cmd spring-boot:run
```

### 3. Frontend
```bash
cd frontend
npm install
npm run dev
```

Open http://localhost:5173

Default API: http://localhost:8080/api

## First learning milestones
1. Understand the HTTP request lifecycle.
2. Trace Register -> JWT -> Axios -> protected API.
3. Trace Project CRUD from React component to PostgreSQL.
4. Add pagination and filtering.
5. Add tests.
6. Add Dockerized deployment.
