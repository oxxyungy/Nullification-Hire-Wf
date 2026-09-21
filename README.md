# Nullification Hire Workflow Service

Java/Spring Boot service that orchestrates the nullification (cancellation) of a hire process.

## Purpose

The service accepts a request to nullify a hire, validates whether the hire can be cancelled, tracks the workflow state, and provides an integration boundary for downstream HR systems.

## Initial workflow

1. A client submits a nullification request for a hire.
2. The service validates the request and creates a workflow record.
3. The workflow enters `PENDING_VALIDATION`.
4. After validation, the workflow is either approved for execution or rejected.
5. The service exposes the current workflow status to clients.

## API

### Create a nullification workflow

`POST /api/v1/hire-nullifications`

```json
{
  "hireId": "7b85bf4b-8b71-4d94-9f76-2ced7dfdcf80",
  "reason": "Candidate declined the offer",
  "requestedBy": "hr-specialist"
}
```

## Authorization

The service acts as an OAuth 2.0 Resource Server and validates JWT Bearer access tokens with Spring Security. To call `POST /api/v1/hire-nullifications`, a token must contain the `hire.nullification.write` scope; Spring maps it to the `SCOPE_hire.nullification.write` authority.

Configure the trusted issuer through `JWT_ISSUER_URI`, for example the URL of a Keycloak realm. Requests without a bearer token receive `401 Unauthorized`; authenticated tokens without the required scope receive `403 Forbidden`.

## Technology stack

- Java 21
- Spring Boot 3
- Spring Security and OAuth 2.0 Resource Server
- Spring Web and Bean Validation
- Spring Data JPA
- PostgreSQL
- Liquibase
- JUnit 5

## Package layout

```text
com.oxxyungy.nullificationhirewf
├── api              # HTTP controllers and request/response DTOs
├── application      # Use cases and workflow orchestration
├── domain           # Business model and rules
└── infrastructure   # Persistence, integrations, configuration
```
