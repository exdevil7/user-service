```text
 _   _  ____  _____ ____    ____  _____ ______     _____ ____ _____ 
| | | |/ ___|| ____|  _ \  / ___|| ____|  _ \ \   / /_ _/ ___| ____|
| | | |\___ \|  _| | |_) | \___ \|  _| | |_) \ \ / / | | |   |  _|  
| |_| | ___) | |___|  _ <   ___) | |___|  _ < \ V /  | | |___| |___ 
 \___/ |____/|_____|_| \_\ |____/|_____|_| \_\ \_/  |___\____|_____|
```

A Spring Boot application providing a unified API to access user data from multiple data sources (PostgreSQL and MySQL).

## Table of Contents

- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
    - [1. Configure Environment Variables](#1-configure-environment-variables)
    - [2. Run with Docker Compose (Recommended)](#2-run-with-docker-compose-recommended)
    - [3. Run Locally with Gradle](#3-run-locally-with-gradle)
- [API Documentation and Health](#api-documentation-and-health)
- [Data Consistency and Availability](#data-consistency-and-availability)
- [Search and Filtering](#search-and-filtering)
- [Data Source Configuration](#data-source-configuration)
    - [Adding a New Data Source](#adding-a-new-data-source)
- [Conclusion](#conclusion)

## Prerequisites

- **Java 25** (OpenJDK 25 used for development)
- **Docker** (including Docker Compose)
- **Gradle** (optional, wrapper provided)

## Getting Started

### 1. Configure Environment Variables

The application requires several environment variables to connect to databases. These are already configured in the `.env` file for Docker Compose.

If running locally, ensure you have a `.env` file or these variables set in your environment:
- `DB_USER`
- `DB_PASSWORD`
- `POSTGRES_DB_NAME`
- `POSTGRES_DB_URL`
- `MYSQL_DB_NAME`
- `MYSQL_URL`

> [!TIP]
> For testing and demonstration purposes, both databases share the same credentials (`DB_USER` and `DB_PASSWORD`).

### 2. Run with Docker Compose (Recommended)

The easiest way to run the application along with its PostgreSQL and MySQL databases is using Docker Compose:

Start the application:
```bash
docker compose up -d --build
```

This will:
- Build the `user-service` image using the `Dockerfile`.
- Start a PostgreSQL container (`postgres-db`) on port `5432`.
- Start a MySQL container (`mysql-db`) on port `3306`.
- Initialize databases with scripts found in `./docker/sql/postgres/init.sql` and `./docker/sql/mysql/init.sql`.
- Start the `user-service` container on port `8080`.

Stop the application:
```bash
# Stop the containers: 
docker compose down

#OR stop and wipe all database data (reset volumes)
docker compose down -v
```

### 3. Run Locally with Gradle

If you prefer to run the application locally without Docker for the service itself, you still need the databases running.

1. Start the databases:
   ```bash
   docker compose up -d postgres-db mysql-db
   ```

2. Run the application:
   ```bash
   ./gradlew bootRun
   ```

## API Documentation and Health

Once the application is running, you can access the following endpoints:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI Docs**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **Health Check**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

## Data Consistency and Availability

The User Service currently prioritizes **data consistency** across all sources. 

> [!IMPORTANT]
> To ensure a complete and consistent view of user data, the service will **fail the entire request** if any of the configured data sources are unreachable or throw an exception. This behavior applies both during startup (the service won't start if a connection fails) and during runtime queries.

### Choosing Availability over Consistency
If **availability** is more important for your use case (i.e., you'd rather see partial results than an error), the service can be adapted by updating the `MultiSourceUserRepository` to handle source failures gracefully.

> [!TIP]
> **A joke for the distributed systems nerds:**
> Why does the User Service always wear a **CAP**? Because it's too **Consistent** to ever lose its head over availability!

## Search and Filtering

Since filtering requirements were **too vague**, the current implementation supports the following logic:

- **Exact match** for the `username` field.
- **Starts-with** (`value%`) search for the `name` and `surname` fields (case-insensitive).

## Data Source Configuration

The application uses `src/main/resources/data-sources.yaml` to define external user data sources. The service dynamically maps fields from these sources to a common `User` model.

### Adding a New Data Source

To add a new data source (e.g., Oracle, SQL Server, etc.):

1. **Add Driver Dependency**: Add the required JDBC driver to `build.gradle`:
   ```gradle
   runtimeOnly 'com.oracle.database.jdbc:ojdbc11'
   ```

2. **Configure data-sources.yaml**: Add your connection details:
   ```yaml
   - name: "my-new-db"
     strategy: "my-custom-strategy" # a default strategy is used if not specified
     url: "jdbc:oracle:thin:@//localhost:1521/xe"
     table: "some_users"
     user: "admin"
     password: "password"
     mapping:
       id: "USER_ID"
       username: "LOGIN"
       name: "FIRST_NAME"
       surname: "LAST_NAME"
   ```

 > [!TIP]  
  > **Custom Strategy (Optional):**
  > If the default JDBC setup is not enough, implement the `DatabaseStrategy` interface and register it as a `@Component("my-custom-strategy")`.
  > Then reference it in the `strategy` field in `data-sources.yaml`.

## Conclusion

The **User Service** is designed to be a lightweight, extensible bridge between diverse user data sources. By following the instructions above, you can quickly spin up the environment, explore the API, and scale the service with additional databases as your requirements evolve.

```text
 _   _                     __             _ 
| | | | __ ___   _____    / _|_   _ _ __ | |
| |_| |/ _` \ \ / / _ \  | |_| | | | '_ \| |
|  _  | (_| |\ V /  __/  |  _| |_| | | | |_|
|_| |_|\__,_| \_/ \___|  |_|  \__,_|_| |_(_)
```
