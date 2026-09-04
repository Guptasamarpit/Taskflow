# TaskFlow — Spring Boot + React Full-Stack Project

A production-style learning project for a React Native developer moving into Java/Spring Boot full-stack development.

## Stack
- Java 21
- Spring Boot 3.5.x
- Spring Web, Validation, Data JPA, Security
- PostgreSQL
- Apache Kafka 3.9.0
- Spring Kafka
- Kafka UI
- JWT authentication
- React 19 + Vite
- React Router
- Axios

## Architecture
Browser -> React SPA -> REST API -> Spring Security/JWT -> Service -> JPA Repository -> PostgreSQL

Project and task services also publish activity events through Kafka:

Project/Task Service -> Kafka Producer -> taskflow.activity topic -> Kafka Consumer -> Activity Service -> activities table

The backend follows Controller -> Service -> Repository boundaries, with DTOs at the API boundary and entities kept internal to persistence.

## Kafka Events

The application publishes activity events for project and task operations:

- `ProjectCreated`
- `ProjectUpdated`
- `ProjectDeleted`
- `TaskCreated`
- `TaskUpdated`
- `TaskDeleted`

Each event includes an event ID, event type, timestamp, user ID, project ID, task ID, and payload.

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

Open https://taskflow-lemon-beta.vercel.app/

Default API: http://localhost:8080/api

## Local Kafka Setup

Docker Desktop must be running. Start Kafka and Kafka UI from the project root:

```powershell
docker compose up -d kafka kafka-ui
```

Services:

| Service | Address | Purpose |
| --- | --- | --- |
| Kafka UI | http://localhost:8081 | Browser dashboard |
| Kafka external listener | `localhost:9094` | Spring Boot running on Windows |
| Kafka internal listener | `kafka:9092` | Kafka UI running inside Docker |
| Kafka controller | `kafka:9093` | Kafka broker management |

The application topic is:

```text
taskflow.activity
```

Create the topic manually if required:

```powershell
docker exec taskflow-kafka /opt/kafka/bin/kafka-topics.sh `
	--bootstrap-server localhost:9092 `
	--create `
	--if-not-exists `
	--topic taskflow.activity `
	--partitions 3 `
	--replication-factor 1
```

Verify the topic:

```powershell
docker exec taskflow-kafka /opt/kafka/bin/kafka-topics.sh `
	--bootstrap-server localhost:9092 `
	--describe `
	--topic taskflow.activity
```

Check the containers and logs:

```powershell
docker ps
docker logs taskflow-kafka
docker logs taskflow-kafka-ui
```

Open Kafka UI at http://localhost:8081 and select the `local` cluster.

## Environment Variables

Local Kafka configuration is stored in `backend/.env`:

```env
KAFKA_BOOTSTRAP_SERVERS=localhost:9094
```

Spring Boot reads it using:

```yaml
bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
```

The environment variable takes priority. `localhost:9092` is only the fallback value. Spring Boot runs on Windows and connects through `localhost:9094`, while Kafka UI runs inside Docker and connects through `kafka:9092`.

## Activity Flow

When a task is created:

1. React sends a request to the backend.
2. `TaskService` saves the task in PostgreSQL.
3. `TaskFlowEventProducer` publishes a `TaskCreated` event.
4. Kafka stores the event in `taskflow.activity`.
5. Kafka UI displays the event.
6. `ActivityEventConsumer` receives the event.
7. `ActivityService` saves the event in the `activities` table.
8. The activity is available through `GET /api/activities`.

Activity records are scoped to the authenticated user and support pagination.

Consume activity events from the command line:

```powershell
docker exec -it taskflow-kafka /opt/kafka/bin/kafka-console-consumer.sh `
	--bootstrap-server localhost:9092 `
	--topic taskflow.activity `
	--from-beginning
```

## Deployment

The first deployment phase uses:

1. React frontend deployed on Vercel.
2. Spring Boot backend deployed on Render.
3. PostgreSQL database deployed on Neon.

For production Kafka, use a managed Kafka provider and configure `KAFKA_BOOTSTRAP_SERVERS` in Render with the provider's bootstrap server. Do not use `localhost` in Render because it refers to the Render container itself.

The current implementation saves the business record and then publishes the Kafka event. PostgreSQL and Kafka are not part of one transaction. The recommended production improvement is a Transactional Outbox Pattern, where the business change and pending event are saved together in PostgreSQL before a background publisher sends the event to Kafka.

## First learning milestones
1. Understand the HTTP request lifecycle.
2. Trace Register -> JWT -> Axios -> protected API.
3. Trace Project CRUD from React component to PostgreSQL.
4. Add pagination and filtering.
5. Add tests.
6. Add Dockerized deployment.




