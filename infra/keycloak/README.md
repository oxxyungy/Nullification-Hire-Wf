# Local authorization infrastructure

## Start

```bash
docker compose up -d
```

Keycloak is available at `http://localhost:8081`.

- Administration console: `admin` / `admin`
- Realm: `nullification-hire`
- Database: PostgreSQL 16, database `keycloak`

## Realm roles

- `HR_SPECIALIST` — creates and manages nullification requests
- `HR_MANAGER` — approves and manages hire nullification requests
- `ADMIN` — administrative access

## Test users

- `hr-specialist` / `hr-specialist` — `HR_SPECIALIST`
- `hr-manager` / `hr-manager` — `HR_MANAGER`
- `admin-user` / `admin-user` — `ADMIN`
