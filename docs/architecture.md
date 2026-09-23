# Nullification Hire Workflow: Architecture

## Purpose

`Nullification-Hire-Wf` manages cancellation of a hire process and coordinates
the resulting changes with downstream HR systems. The service is responsible for
validating whether cancellation is allowed, executing the workflow exactly once,
and publishing a business event for integration consumers.

## Scope

The service owns:

- receiving a nullification command for a hire process;
- validating the current workflow state and cancellation reason;
- recording the nullification result;
- initiating compensation or cancellation actions in dependent services;
- publishing an integration event after successful processing.

The service does not own candidate, employee, position, or organizational-unit
master data. Those entities are referenced by identifiers provided by upstream
systems.

## API Contract

### Nullify hire process

`POST /api/v1/hire-processes/{hireProcessId}/nullifications`

Headers:

```http
Content-Type: application/json
Idempotency-Key: 8c9a3bb0-9aa5-4dbd-9c7d-86f2507b798d
X-Correlation-Id: 4ee9a0cf-0e7b-4ce8-8a3d-4e50a1b8725f
```

Request body:

```json
{
  "reasonCode": "CANDIDATE_DECLINED_OFFER",
  "reasonComment": "Candidate declined the offer before the planned start date",
  "initiatorId": "hr-specialist-123",
  "sourceSystem": "hr-portal"
}
```

Successful response:

```http
202 Accepted
```

```json
{
  "nullificationId": "01JH7P5TQD7ZS0SGA9Q4K8M6AK",
  "hireProcessId": "hire-process-456",
  "status": "ACCEPTED",
  "correlationId": "4ee9a0cf-0e7b-4ce8-8a3d-4e50a1b8725f"
}
```

## Idempotency

A client must send `Idempotency-Key` for every nullification request.

- The key is unique within a hire process.
- Repeating a request with the same key and identical payload returns the
  previously created result.
- Reusing a key with a different payload returns `409 Conflict`.
- Idempotency records must be persisted before asynchronous processing begins.

This protects against retries caused by network timeouts, client restarts, and
at-least-once message delivery.

## Workflow States

| State | Description |
| --- | --- |
| `RECEIVED` | Command was accepted and stored |
| `VALIDATING` | Business and state-transition validation is in progress |
| `REJECTED` | Request cannot be processed |
| `NULLIFYING` | Compensation actions are being executed |
| `NULLIFIED` | Hire process was successfully nullified |
| `FAILED` | Processing failed; retry or manual resolution is required |

A nullification is allowed only for hire processes in a cancellable state.
The exact set of cancellable source states must be maintained as a business rule
and covered by automated tests.

## Events

After a successful state transition to `NULLIFIED`, publish
`hire-process.nullified.v1`.

```json
{
  "eventId": "01JH7P5TQD7ZS0SGA9Q4K8M6AK",
  "eventType": "hire-process.nullified.v1",
  "occurredAt": "2026-09-23T20:16:00Z",
  "correlationId": "4ee9a0cf-0e7b-4ce8-8a3d-4e50a1b8725f",
  "payload": {
    "hireProcessId": "hire-process-456",
    "nullificationId": "01JH7P5TQD7ZS0SGA9Q4K8M6AK",
    "reasonCode": "CANDIDATE_DECLINED_OFFER",
    "initiatorId": "hr-specialist-123",
    "sourceSystem": "hr-portal"
  }
}
```

Use an outbox pattern so database state and event publication remain consistent.
Consumers must treat the event as at-least-once delivered and be idempotent by
`eventId`.

## Errors

| HTTP status | Code | Meaning |
| --- | --- | --- |
| `400` | `VALIDATION_ERROR` | Request payload is invalid |
| `404` | `HIRE_PROCESS_NOT_FOUND` | Referenced hire process does not exist |
| `409` | `NULLIFICATION_NOT_ALLOWED` | Current state does not allow nullification |
| `409` | `IDEMPOTENCY_KEY_REUSED` | Same key was sent with a different payload |
| `422` | `DEPENDENCY_REJECTED` | A required downstream action was rejected |
| `503` | `DEPENDENCY_UNAVAILABLE` | A downstream service is temporarily unavailable |

All error responses should include `code`, `message`, `correlationId`, and,
when applicable, structured field-level validation details.

## Observability

Every request, workflow transition, integration call, and published event must
carry `correlationId`. At minimum, expose:

- request count and latency by endpoint and status;
- nullification count by reason code and final status;
- retries and failures by downstream dependency;
- outbox lag and unpublished-event count;
- structured logs with `hireProcessId`, `nullificationId`, `correlationId`,
  and `idempotencyKey`.

Sensitive HR data must not be written to logs or event payloads unless explicitly
approved by the data-protection policy.
