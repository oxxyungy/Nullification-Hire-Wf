# Nullification Hire WF

Java 21 / Spring Boot workflow service for initiating and tracking hire nullification processes.

## Architecture

The service persists only data it owns: a workflow request and its nullification-specific attributes. Employee, hire, contract and document master data remain in their source systems and are referenced by external identifiers.

- `nullification_request` — workflow request, status and audit data
- `nullification_data` — one-to-one nullification attributes
- Liquibase owns database schema changes
- JPA validates the schema only (`ddl-auto=validate`)

## Local run

Prerequisites: Java 21, Maven 3.9+ and Docker Compose.

```bash
cp .env.example .env
docker compose up -d postgres
./mvnw spring-boot:run
```

If Maven Wrapper is unavailable, run:

```bash
mvn spring-boot:run
```

The application connects to PostgreSQL at `localhost:5432` by default. Override `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME` and `DB_PASSWORD` through environment variables; never commit real credentials.

## Create workflow

```http
POST /api/v1/hire-nullifications
Content-Type: application/json

{
  "hireId": "HIRE-2026-001245",
  "personId": "11111111-1111-1111-1111-111111111111",
  "reason": "CANDIDATE_WITHDREW",
  "requestedBy": "22222222-2222-2222-2222-222222222222",
  "comment": "Candidate withdrew consent before hire",
  "nullificationData": {
    "nullificationDate": "2026-09-22",
    "positionId": "37654321",
    "signerPersonId": null,
    "signerEmployeeId": null
  }
}
```

Successful creation returns `202 Accepted`:

```json
{
  "workflowId": "f4a3fd23-0392-4d8c-bb80-aa09f6ceee77",
  "status": "PENDING_VALIDATION",
  "createdAt": "2026-09-22T20:15:00Z"
}
```

A repeated request for the same `hireId` returns `409 Conflict`.
