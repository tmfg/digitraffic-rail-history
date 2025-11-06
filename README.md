# Project

Contains the source code for the service
[rata.digitraffic.fi/history/](https://rata.digitraffic.fi/history/)

# Setting up the development environment

1. Clone the repository:
   `git clone https://github.com/tmfg/digitraffic-rail-history-private.git`
2. Create the database and run migrations:
   ```bash
   cd db
   docker compose up -d
   # or
   ./db-rm-build-up.sh
   ```
3. Create the file
   `train-history-updater/src/main/resources/application-localhost.properties` and include the db connection properties. The database username and password can be found in `db/docker-compose.yml` as `MYSQL_USER` and `MYSQL_PASSWORD`.
   ```properties
   spring.datasource.username=JOTAIN
   spring.datasource.password=JOTAIN
   spring.datasource.url=jdbc:mysql://localhost/trainhistoryjson?autoReconnect=true&rewriteBatchedStatements=true&cachePrepStmts=true&useTimezone=true&serverTimezone=UTC&useSSL=false
   ```
4. Do the same for
   `train-history-backend/src/main/resources/application-localhost.properties`
5. Start `TrainHistoryUpdaterApplication` using the profile `localhost`
   `mvn spring-boot:run -Dspring-boot.run.profiles=localhost` and you should start getting data into the database.
6. Start `TrainHistoryBackendApplication` using the profile `localhost`
   `mvn spring-boot:run -Dspring-boot.run.profiles=localhost`
7. Open a browser at  http://localhost:26087/history (port is the `server.port`)

# Tests

## train-history-backend

When the development environment is set up, including the database and application profile, run:

```
cd train-history-backend
mvn test -Dspring.profiles.active=localhost
```

## E2E tests

You need to have both `train-history-updater` and `train-history-backend` running with the `localhost` profile and
the database up and running.

Before running the tests, you probably need to make slight modifications to the test files themselves.
This is because they are using the current date in searches for train data and most likely
your local database does not have that data. At the time of writing, this means that you need to change
the arguments given to `submitTrainInfoForm` function with something that exists in your local database.

To run the E2E tests:
```
cd train-history-backend
cp .env.playwright.example .env.playwright
pnpm run e2e-ui
```

# Architecture

The history service consists of two components:

- train-history-backend
  - Backend system
- train-history-updater
  - Fetches and stores historical data for the backend application to read
