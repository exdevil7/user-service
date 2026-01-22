# Test Task for Java Developer

## Task Definition

You should create a service for aggregating user data from **multiple databases**.  
The application must provide a **single REST endpoint** that returns data aggregated from all configured data sources.

---

## REST API

### Get users

```
GET /users
```

### Successful response example

```json
[
  {
    "id": "example-user-id-1",
    "username": "user-1",
    "name": "User",
    "surname": "Userenko"
  },
  {
    "id": "example-user-id-2",
    "username": "user-2",
    "name": "Testuser",
    "surname": "Testov"
  }
]
```

---

## Data Source Configuration

The application must support **declarative configuration** for data sources.  
The number of data sources is **not limited**.

### Example configuration

```yaml
data-sources:
  - name: data-base-1
    strategy: postgres # optional, only if multiple database strategies are supported
    url: jdbc://.....
    table: users
    user: testuser
    password: testpass
    mapping:
      id: user_id
      username: login
      name: first_name
      surname: last_name

  - name: data-base-2
    strategy: postgres
    url: jdbc://.....
    table: user_table
    user: testuser
    password: testpass
    mapping:
      id: ldap_login
      username: ldap_login
      name: name
      surname: surname
```

---

## Strong Requirements

- Use **Spring Boot**
- Use **OpenAPI** specification to declare the endpoint definition
- Include a **README.md** with instructions on how to run the application (using **Docker Compose** is a big plus)
- Project must be pushed to a **Git repository** with the README included

---

## Optional Requirements

- Add **integration tests** using **Testcontainers**
- Add **selecting filters** in the API and database queries

---

